package com.mauvaisetroupe.eadesignit.service.reporting;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.util.DataFlowComparator;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ReportingService {

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    private DataFlowRepository dataFlowRepository;

    private final Logger log = LoggerFactory.getLogger(ReportingService.class);

    public void mergeInterfaces(Long id, @NotNull List<String> aliasToMerge) {
        FlowInterface toKeep = flowInterfaceRepository.findById(id).get();
        Set<FlowInterface> interfacesToMerge = flowInterfaceRepository.findByAliasIn(aliasToMerge);

        DataFlowComparator comparator = new DataFlowComparator();
        Set<FlowInterface> toDelete = new HashSet<>();
        for (FlowInterface toMerge : interfacesToMerge) {
            log.debug("About to merge interface :" + toMerge.getAlias());
            //application
            Assert.isTrue(
                toMerge.getSource().equals(toKeep.getSource()) && toMerge.getTarget().equals(toKeep.getTarget()),
                "Should have same source & taget"
            );

            //application component
            Assert.isNull(toMerge.getSourceComponent(), "Not implemented");
            Assert.isNull(toMerge.getTargetComponent(), "Not implemented");

            //protocol
            Assert.isTrue(toMerge.getProtocol().equals(toKeep.getProtocol()), "Not implemented");

            //owner not check, could potentially be different

            //dataflow
            for (DataFlow dataFlowToMerge : toMerge.getDataFlows()) {
                log.debug("Examining DataFlow :" + dataFlowToMerge);
                boolean shouldKeepDataFlow = true;
                for (DataFlow dataFlowToKeep : toKeep.getDataFlows()) {
                    log.debug("Comparing with DataFlow :" + dataFlowToKeep);
                    if (comparator.areEquivalent(dataFlowToMerge, dataFlowToKeep)) {
                        shouldKeepDataFlow = false;
                        log.debug("Equality between DataFlow, should not keep");
                    }
                }
                if (shouldKeepDataFlow) {
                    log.debug("Adding to interface : " + toKeep + " DataFlow :  " + dataFlowToMerge);
                    toKeep.addDataFlows(dataFlowToMerge);
                } else {
                    log.debug("Delete DataFlow : " + dataFlowToMerge);
                    dataFlowRepository.delete(dataFlowToMerge);
                }
            }

            // Functional Flows to move (avoid ConcurrentMofificationException)
            Set<FunctionalFlow> flowsToModify = new HashSet<>();
            flowsToModify.addAll(toMerge.getFunctionalFlows());

            for (FunctionalFlow functionalFlow : flowsToModify) {
                if (!functionalFlow.getInterfaces().contains(toKeep)) {
                    log.debug("Adding to Flow : " + functionalFlow + " interface " + toKeep);
                    functionalFlow.addInterfaces(toKeep);
                }
                log.debug("Removing from Flow : " + functionalFlow + " interface " + toMerge);
                functionalFlow.removeInterfaces(toMerge);
                functionalFlowRepository.save(functionalFlow);
            }
            toDelete.add(toMerge);
        }

        for (FlowInterface inter : toDelete) {
            if (!inter.equals(toKeep)) {
                log.debug("Delete " + inter);
                flowInterfaceRepository.delete(inter);
            } else {
                log.debug("Won't delete " + inter);
            }
        }
    }
}
