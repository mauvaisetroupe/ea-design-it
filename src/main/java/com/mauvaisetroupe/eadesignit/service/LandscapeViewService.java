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
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
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
public class LandscapeViewService {

    private final Logger log = LoggerFactory.getLogger(LandscapeViewService.class);

    @Autowired
    private LandscapeViewRepository landscapeViewRepository;

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
     */
    public void delete(Long id) {
        log.debug("Request to delete LandscapeView : {}", id);
        // delete landscape and all entities without references to other landscape

        LandscapeView landscapeView = landscapeViewRepository.getById(id);

        Set<FunctionalFlow> allFlows = new HashSet<>(landscapeView.getFlows());

        Set<FlowInterface> allInterfaces = allFlows
            .stream()
            .map(f -> f.getInterfaces())
            .flatMap(f -> f.stream())
            .collect(Collectors.toSet());

        Set<DataFlow> allDataFromFlows = allFlows.stream().map(i -> i.getDataFlows()).flatMap(f -> f.stream()).collect(Collectors.toSet());

        Set<DataFlow> allDataFromInterfaces = allInterfaces
            .stream()
            .map(i -> i.getDataFlows())
            .flatMap(f -> f.stream())
            .collect(Collectors.toSet());

        Set<DataFlow> allData = new HashSet<>();
        allData.addAll(allDataFromFlows);
        allData.addAll(allDataFromInterfaces);

        // Detach all flows from landscape
        for (FunctionalFlow flow : allFlows) {
            landscapeView.removeFlows(flow);
            functionalFlowRepository.save(flow);
        }
        // delete landscape
        landscapeViewRepository.deleteById(id);

        // if flow is not referenced in another landscape, delete flow
        for (FunctionalFlow flow : allFlows) {
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
                    flow.removeDataFlows(dataFlow);
                    dataFlowRepository.save(dataFlow);
                }
                // delete Flow
                functionalFlowRepository.delete(flow);
            }
        }

        // delete Interfcaes not linked to other Flow
        for (FlowInterface interface1 : allInterfaces) {
            if (interface1.getSteps() == null || interface1.getSteps().isEmpty()) {
                // detach dataflow
                for (DataFlow dataFlow : new HashSet<>(interface1.getDataFlows())) {
                    interface1.removeDataFlows(dataFlow);
                    dataFlowRepository.save(dataFlow);
                }
                // delete Flow
                flowInterfaceRepository.delete(interface1);
            }
        }

        // delete DataFlow not linked to
        for (DataFlow dataFlow : allData) {
            if (dataFlow.getFlowInterface() == null && (dataFlow.getFunctionalFlows() == null || dataFlow.getFunctionalFlows().isEmpty())) {
                dataFlowRepository.delete(dataFlow);
            }
        }
    }
}
