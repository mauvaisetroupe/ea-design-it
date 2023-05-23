package com.mauvaisetroupe.eadesignit.service;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowGroupRepository;
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

    public static final String WAS_LINKED_TO = "WAS LINKED TO ";
    public static final String SHOULD_BE_LINKED_TO = "SHOULD BE LINKED TO ";

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

    @Autowired
    private FlowGroupRepository flowGroupRepository;

    /**
     * Delete the landscapeView by id.
     *
     * @param id the id of the entity.
     * @param deleteDatas
     * @param deleteFlowInterface
     * @param deleteFunctionalFlow
     */
    public void delete(Long id, boolean deleteFunctionalFlow, boolean deleteFlowInterface, boolean deleteDatas) {
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
        if (deleteFunctionalFlow) {
            //Detach groups that refeence a flow that will be deleted
            for (FunctionalFlow flow : allFlows) {
                Set<FlowGroup> groups = flowGroupRepository.findByFlowId(flow.getId());
                for (FlowGroup group : groups) {
                    group.setFlow(null);
                    if (group.getDescription() == null) {
                        group.setDescription(WAS_LINKED_TO + flow.getAlias());
                    }
                    flowGroupRepository.save(group);
                }
            }

            for (FunctionalFlow flow : allFlows) {
                if (flow.getLandscapes() == null || flow.getLandscapes().isEmpty()) {
                    // detach and delete steps
                    Set<FlowGroup> groupsToDelete = new HashSet<>();
                    for (FunctionalFlowStep step : new HashSet<>(flow.getSteps())) {
                        // Delete Group inside current FunctionlaFlow
                        FlowGroup group = step.getGroup();
                        if (group != null) {
                            // Detach group from steps
                            group.removeSteps(step);
                            // Detach group from Flow
                            group.setFlow(null);
                            groupsToDelete.add(group);
                        }

                        FlowInterface interface1 = step.getFlowInterface();
                        interface1.removeSteps(step);
                        flow.removeSteps(step);
                        flowInterfaceRepository.save(interface1);
                        functionalFlowRepository.save(flow);
                        functionalFlowStepRepository.delete(step);
                    }
                    // delete group
                    for (FlowGroup group : groupsToDelete) {
                        flowGroupRepository.delete(group);
                    }

                    // detach dataflow
                    for (DataFlow dataFlow : new HashSet<>(flow.getDataFlows())) {
                        flow.removeDataFlows(dataFlow);
                        dataFlowRepository.save(dataFlow);
                        functionalFlowRepository.save(flow);
                    }
                    // delete Flow
                    functionalFlowRepository.delete(flow);
                }
            }
        }

        // delete Interfcaes not linked to other Flow
        if (deleteFunctionalFlow && deleteFlowInterface) {
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

        // delete dataflows not linked to flow or interfaces
        if (deleteFunctionalFlow && deleteFlowInterface && deleteDatas) {
            for (DataFlow dataFlow : allData) {
                if (
                    dataFlow.getFlowInterface() == null &&
                    (dataFlow.getFunctionalFlows() == null || dataFlow.getFunctionalFlows().isEmpty()) &&
                    (dataFlow.getItems() == null || dataFlow.getItems().isEmpty())
                ) {
                    dataFlowRepository.delete(dataFlow);
                }
            }
        }
    }
}
