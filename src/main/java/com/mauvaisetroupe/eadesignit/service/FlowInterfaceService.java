package com.mauvaisetroupe.eadesignit.service;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import java.util.HashSet;
import java.util.Set;
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
public class FlowInterfaceService {

    private final Logger log = LoggerFactory.getLogger(FunctionalflowService.class);

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private DataFlowRepository dataFlowRepository;

    /**
     * Delete the landscapeView by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        FlowInterface interface1 = flowInterfaceRepository.getById(id);

        Set<DataFlow> allData = interface1.getDataFlows();

        if (interface1.getSteps() == null || interface1.getSteps().isEmpty()) {
            // detach dataflow
            for (DataFlow dataFlow : new HashSet<>(interface1.getDataFlows())) {
                interface1.removeDataFlows(dataFlow);
                dataFlowRepository.save(dataFlow);
            }
            // delete Interface
            log.debug("about to delete interface " + interface1);
            flowInterfaceRepository.delete(interface1);
        } else {
            throw new IllegalStateException("Cannot delete Interface used by Functional Flow");
        }

        // delete DataFlow not linked to
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
