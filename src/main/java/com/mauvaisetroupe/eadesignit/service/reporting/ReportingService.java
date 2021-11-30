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
        DataFlowComparator comparator = new DataFlowComparator();

        // INPUTS
        FlowInterface interfaceToKeep = flowInterfaceRepository.findById(id).get();
        Set<FlowInterface> interfacesToMerge = flowInterfaceRepository.findByAliasIn(aliasToMerge);

        // Check functional flows (because I have a bug!!!)
        for (FunctionalFlow ff : interfaceToKeep.getFunctionalFlows()) {
            log.debug(" ### Functional Flow {} has Interfaces {} ... retrieve from {}", ff, ff.getInterfaces(), interfaceToKeep.getAlias());
            FunctionalFlow functionalFlow2 = functionalFlowRepository.findById(ff.getId()).get();
            log.debug(
                " ### Functional Flow {} has Interfaces {} ... retrieve in DB ",
                functionalFlow2.getAlias(),
                functionalFlow2.getInterfaces()
            );
        }
        for (FlowInterface intt : interfacesToMerge) {
            for (FunctionalFlow ff : intt.getFunctionalFlows()) {
                log.debug(
                    " ### Functional Flow {} has Interfaces {} ... retrieve from {}",
                    intt.getFunctionalFlows(),
                    ff.getInterfaces(),
                    intt.getAlias()
                );
                FunctionalFlow functionalFlow2 = functionalFlowRepository.findById(ff.getId()).get();
                log.debug(
                    " ### Functional Flow {} has Interfaces {} ... retrieve in DB ",
                    functionalFlow2.getAlias(),
                    functionalFlow2.getInterfaces()
                );
            }
        }
        // ENd check

        for (FlowInterface interfaceToMerge : interfacesToMerge) {
            log.debug(" ### ### ### About to merge interface :" + interfaceToMerge.getAlias());

            //application
            Assert.isTrue(
                interfaceToMerge.getSource().equals(interfaceToKeep.getSource()) &&
                interfaceToMerge.getTarget().equals(interfaceToKeep.getTarget()),
                "Should have same source & taget"
            );

            //application component
            Assert.isNull(interfaceToMerge.getSourceComponent(), "Not implemented");
            Assert.isNull(interfaceToMerge.getTargetComponent(), "Not implemented");

            //protocol
            Assert.isTrue(interfaceToMerge.getProtocol().equals(interfaceToKeep.getProtocol()), "Not implemented");

            //owner not check, could potentially be different

            //dataflow
            // make copy to avoid concurrent modification exception
            Set<DataFlow> dataFlowToprocess = new HashSet<>();
            dataFlowToprocess.addAll(interfaceToMerge.getDataFlows());

            for (DataFlow dataFlowToMerge : dataFlowToprocess) {
                log.debug(" ### ### Examining DataFlow :" + dataFlowToMerge.getId());

                // remove dataSet from interface
                log.debug(" ###  Detach DataFlow {} from interface {} ", dataFlowToMerge.getId(), interfaceToMerge.getAlias());
                interfaceToMerge.removeDataFlows(dataFlowToMerge);
                log.debug(" ### Save DataFlow : " + dataFlowToMerge.getId());
                dataFlowRepository.save(dataFlowToMerge);
                log.debug(" ### Save Interface : " + interfaceToMerge.getAlias());
                flowInterfaceRepository.save(interfaceToMerge);

                // Check if need to keep or delete (other dataflow equivalent?)
                boolean shouldKeepDataFlow = true;
                for (DataFlow dataFlowToKeep : interfaceToKeep.getDataFlows()) {
                    log.debug(" ### Comparing with DataFlow :" + dataFlowToKeep.getId());
                    if (comparator.areEquivalent(dataFlowToMerge, dataFlowToKeep)) {
                        shouldKeepDataFlow = false;
                        log.debug(" ### Equality with existing DataFlow {}.", dataFlowToKeep.getId());
                        log.debug(" ### Delete DataFlow : " + dataFlowToMerge.getId());
                        dataFlowRepository.delete(dataFlowToMerge);
                        break;
                    }
                }
                if (shouldKeepDataFlow) {
                    if (!interfaceToKeep.getDataFlows().contains(dataFlowToMerge)) {
                        // Move if necessary
                        log.debug(" ### Adding to interface : " + interfaceToKeep.getAlias() + " DataFlow :  " + dataFlowToMerge.getId());
                        interfaceToKeep.addDataFlows(dataFlowToMerge);
                        log.debug(" ### Save DataFlow : " + dataFlowToMerge.getId());
                        dataFlowRepository.save(dataFlowToMerge);
                        log.debug(" ### Save Interface : " + interfaceToKeep.getAlias());
                        flowInterfaceRepository.save(interfaceToKeep);
                    } else {
                        log.debug(
                            "### DataFlow :  " + dataFlowToMerge.getId() + " already in " + interfaceToKeep.getAlias() + " (not added)"
                        );
                    }
                }
            }

            // Functional Flows to move (avoid ConcurrentMofificationException)
            Set<FunctionalFlow> flowsToModify = new HashSet<>();
            flowsToModify.addAll(interfaceToMerge.getFunctionalFlows());

            for (FunctionalFlow functionalFlow : flowsToModify) {
                log.debug(" ### ### Examining Functional Flow :" + functionalFlow.getId());
                log.debug(" ### Functional Flow {} has Interfaces {}", functionalFlow.getAlias(), functionalFlow.getInterfaces());
                FunctionalFlow functionalFlow2 = functionalFlowRepository.findById(functionalFlow.getId()).get();
                log.debug(" ### Functional Flow {} has Interfaces {}", functionalFlow2.getAlias(), functionalFlow2.getInterfaces());

                log.debug(
                    " ### Removing interface " + interfaceToMerge.getAlias() + " from Functional Flow : " + functionalFlow.getAlias()
                );
                functionalFlow.removeInterfaces(interfaceToMerge);
                functionalFlowRepository.save(functionalFlow);
                flowInterfaceRepository.save(interfaceToMerge);

                if (!functionalFlow.getInterfaces().contains(interfaceToKeep)) {
                    //if (!functionalFlow2.getInterfaces().contains(interfaceToKeep)) {
                    log.debug(" ### Adding interface " + interfaceToKeep.getAlias() + " to Functional Flow : " + functionalFlow.getAlias());
                    functionalFlow.addInterfaces(interfaceToKeep);
                    log.debug(" ### Save Functional Flow : " + functionalFlow.getAlias());
                    functionalFlowRepository.save(functionalFlow);
                    log.debug(" ### Save Interface : " + interfaceToKeep.getAlias());
                    flowInterfaceRepository.save(interfaceToKeep);
                } else {
                    log.debug(
                        " ### Interface :  " + interfaceToKeep.getAlias() + " already in " + functionalFlow.getAlias() + " (not added)"
                    );
                }
                log.debug(" ### Functional Flow {} has now Interfaces {}", functionalFlow.getAlias(), functionalFlow.getInterfaces());
            }
            // Delete interface
            log.debug(" ### ### Delete " + interfaceToMerge.getAlias());
            flowInterfaceRepository.delete(interfaceToMerge);
        }
    }
}
