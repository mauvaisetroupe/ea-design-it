package com.mauvaisetroupe.eadesignit.service.reporting;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.util.DataFlowComparator;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ReportingService {

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private DataFlowRepository dataFlowRepository;

    public void mergeInterfaces(Long id, @NotNull List<String> aliasToMerge) {
        FlowInterface toKeep = flowInterfaceRepository.findById(id).get();
        Set<FlowInterface> interfacesToMerge = flowInterfaceRepository.findByAliasIn(aliasToMerge);

        DataFlowComparator comparator = new DataFlowComparator();
        for (FlowInterface toMerge : interfacesToMerge) {
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
                boolean shouldKeepDataFlow = true;
                for (DataFlow dataFlowToKeep : toKeep.getDataFlows()) {
                    if (comparator.areEquivalent(dataFlowToMerge, dataFlowToKeep)) {
                        shouldKeepDataFlow = false;
                    }
                }
                if (shouldKeepDataFlow) {
                    toKeep.addDataFlows(dataFlowToMerge);
                } else {
                    dataFlowRepository.delete(dataFlowToMerge);
                }
            }
            flowInterfaceRepository.delete(toMerge);

            // Functional Flows to move (avoid ConcurrentMofificationException)
            Set<FunctionalFlow> flowsToModify = new HashSet<>();
            flowsToModify.addAll(toMerge.getFunctionalFlows());

            for (FunctionalFlow functionalFlow : flowsToModify) {
                functionalFlow.addInterfaces(toKeep);
                functionalFlow.removeInterfaces(toMerge);
            }
        }
    }
}
