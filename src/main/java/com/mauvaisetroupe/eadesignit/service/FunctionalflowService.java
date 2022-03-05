package com.mauvaisetroupe.eadesignit.service;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowStepRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LandscapeView}.
 */
@Service
@Transactional
public class FunctionalflowService {

    private final Logger log = LoggerFactory.getLogger(FunctionalflowService.class);

    @Autowired
    private FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private FunctionalFlowStepRepository functionalFlowStepRepository;

    @Autowired
    private DataFlowRepository dataFlowRepository;

    /**
     * Delete the landscapeView by id.
     *
     * @param id the id of the entity.
     * @param deleteDatas
     * @param deleteFlowInterfaces
     */
    public void delete(Long id, boolean deleteFlowInterfaces, boolean deleteDatas) {
        log.debug("Request to delete FunctionalFlow : {}", id);
        // delete landscape and all entities without references to other landscape

        FunctionalFlow flow = functionalFlowRepository.getById(id);

        Set<FlowInterface> allInterfaces = flow.getInterfaces();

        Set<DataFlow> allDataFromFlows = flow.getDataFlows();

        Set<DataFlow> allDataFromInterfaces = allInterfaces
            .stream()
            .map(i -> i.getDataFlows())
            .flatMap(f -> f.stream())
            .collect(Collectors.toSet());

        Set<DataFlow> allData = new HashSet<>();
        allData.addAll(allDataFromFlows);
        allData.addAll(allDataFromInterfaces);

        if (flow.getLandscapes() == null || flow.getLandscapes().isEmpty()) {
            // detach and delete steps
            for (FunctionalFlowStep step : new HashSet<>(flow.getSteps())) {
                FlowInterface interface1 = step.getFlowInterface();
                interface1.removeSteps(step);
                flow.removeSteps(step);
                flowInterfaceRepository.save(interface1);
                functionalFlowRepository.save(flow);
                functionalFlowStepRepository.delete(step);
            }
            // detach dataflow
            for (DataFlow dataFlow : new HashSet<>(flow.getDataFlows())) {
                System.out.println("before" + flow.getDataFlows().size());
                System.out.println("before" + dataFlow.getFunctionalFlows().size());

                flow.removeDataFlows(dataFlow);
                dataFlowRepository.save(dataFlow);
                functionalFlowRepository.save(flow);
                System.out.println("after" + flow.getDataFlows().size());
                System.out.println("after" + dataFlow.getFunctionalFlows().size());
            }
            // delete Flow
            functionalFlowRepository.delete(flow);
        }

        // delete Interfaces not linked to other Flow
        if (deleteFlowInterfaces) {
            for (FlowInterface interface1 : allInterfaces) {
                if (interface1.getSteps() == null || interface1.getSteps().isEmpty()) {
                    // detach dataflow
                    for (DataFlow dataFlow : new HashSet<>(interface1.getDataFlows())) {
                        interface1.removeDataFlows(dataFlow);
                        dataFlowRepository.save(dataFlow);
                    }
                    // delete Interface
                    log.debug("about to delete interface " + interface1);
                    flowInterfaceRepository.delete(interface1);
                }
            }
        }

        // delete DataFlow not linked to
        if (deleteFlowInterfaces && deleteDatas) {
            for (DataFlow dataFlow : allData) {
                if (
                    dataFlow.getFlowInterface() == null &&
                    (dataFlow.getFunctionalFlows() == null || dataFlow.getFunctionalFlows().isEmpty()) &&
                    (dataFlow.getItems() == null || dataFlow.getItems().isEmpty())
                ) {
                    log.debug("About to delete " + dataFlow);
                    dataFlowRepository.delete(dataFlow);
                }
            }
        }
    }
}
