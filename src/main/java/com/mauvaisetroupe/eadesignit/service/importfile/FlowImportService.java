package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.DataFormat;
import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ProtocolType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ViewPoint;
import com.mauvaisetroupe.eadesignit.domain.util.DataFlowComparator;
import com.mauvaisetroupe.eadesignit.repository.ApplicationComponentRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFormatRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowGroupRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowStepRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.ProtocolRepository;
import com.mauvaisetroupe.eadesignit.service.FunctionalflowService;
import com.mauvaisetroupe.eadesignit.service.LandscapeViewService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service
public class FlowImportService {

    private static final String GENERIC_DATA_FLOW = "GENERIC DATAFLOW from ADD";
    private static final String GENERIC_FILE_FROM_ADD = "GENERIC FILE from ADD";
    private static final String GENERIC_EVENT_FROM_ADD = "GENERIC EVENT from ADD";

    protected static final String FLOW_ID_FLOW = "Id flow";
    protected static final String FLOW_ALIAS_FLOW = "Alias flow";
    protected static final String FLOW_SOURCE_ELEMENT = "Source Element";
    protected static final String FLOW_TARGET_ELEMENT = "Target Element";
    protected static final String FLOW_DESCRIPTION = "Description";
    protected static final String FLOW_INTEGRATION_PATTERN = "Integration pattern";
    protected static final String FLOW_FREQUENCY = "Frequency";
    protected static final String FLOW_FORMAT = "Format";
    protected static final String FLOW_SWAGGER = "Swagger";
    protected static final String FLOW_BLUEPRINT_SOURCE = "Blueprint From Source";
    protected static final String FLOW_BLUEPRINT_TARGET = "Blueprint From Target";
    protected static final String FLOW_BLUEPRINT_SOURCE_STATUS = "Status Blueprint From Source";
    protected static final String FLOW_BLUEPRINT_TARGET_STATUS = "Status Blueprint From Target";
    protected static final String FLOW_STATUS_FLOW = "Status flow";
    protected static final String FLOW_COMMENT = "Comment";
    protected static final String FLOW_ADD_CORRESPONDENT_ID = "ADD correspondent ID";
    protected static final String FLOW_STEP_NUMBER = "Step";
    protected static final String FLOW_STEP_DESCRIPTION = "Step description";
    protected static final String FLOW_EXTERNAL = "External";
    protected static final String FLOW_GROUP_FLOW_ALIAS = "Group.flow";
    protected static final String FLOW_GROUP_ORDER = "Group.order";
    protected static final String FLOW_GROUP_TITLE = "Group.title";
    protected static final String FLOW_GROUP_URL = "Group.url";

    private final List<String> columnsArray = new ArrayList<String>();

    private static final String FLOW_SHEET_NAME = "Message_Flow";

    private final Logger log = LoggerFactory.getLogger(FlowImportService.class);

    @Autowired
    private FunctionalFlowRepository flowRepository;

    @Autowired
    private FlowInterfaceRepository interfaceRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationComponentRepository applicationComponentRepository;

    @Autowired
    private DataFlowRepository dataFlowRepository;

    @Autowired
    private LandscapeViewRepository landscapeViewRepository;

    @Autowired
    private ProtocolRepository protocolRepository;

    @Autowired
    private DataFormatRepository dataFormatRepository;

    @Autowired
    private FunctionalFlowStepRepository functionalFlowStepRepository;

    @Autowired
    private FunctionalflowService functionalflowService;

    @Autowired
    private FlowGroupRepository flowGroupRepository;

    public FlowImportService() {
        this.columnsArray.add(FLOW_ID_FLOW);
        this.columnsArray.add(FLOW_ALIAS_FLOW);
        this.columnsArray.add(FLOW_SOURCE_ELEMENT);
        this.columnsArray.add(FLOW_TARGET_ELEMENT);
        this.columnsArray.add(FLOW_DESCRIPTION);
        this.columnsArray.add(FLOW_INTEGRATION_PATTERN);
        this.columnsArray.add(FLOW_FREQUENCY);
        this.columnsArray.add(FLOW_FORMAT);
        this.columnsArray.add(FLOW_SWAGGER);
        this.columnsArray.add(FLOW_BLUEPRINT_SOURCE);
        this.columnsArray.add(FLOW_BLUEPRINT_TARGET);
        this.columnsArray.add(FLOW_BLUEPRINT_SOURCE_STATUS);
        this.columnsArray.add(FLOW_BLUEPRINT_TARGET_STATUS);
        this.columnsArray.add(FLOW_STATUS_FLOW);
        this.columnsArray.add(FLOW_COMMENT);
        this.columnsArray.add(FLOW_ADD_CORRESPONDENT_ID);
        this.columnsArray.add(FLOW_STEP_DESCRIPTION);
        this.columnsArray.add(FLOW_EXTERNAL);
    }

    /**
     *
     * Unlike the delete method in LandscapeViewService, this method do not delete all FunctionalFlows
     * and associated objects (DataFlow, Groups, etc.)
     * Instead it empties flows (deleting steps)
     * That mean that groups in other Landscapes are not detached from FunctionalFlows
     * that could significantly change during import
     *
     **/
    @Transactional
    public List<FlowImport> importExcel(InputStream file, String filename) throws EncryptedDocumentException, IOException {
        List<FlowImport> result = new ArrayList<FlowImport>();

        ExcelReader excelReader = new ExcelReader(file);
        List<Map<String, Object>> flowsDF = excelReader.getSheet(FLOW_SHEET_NAME);
        String diagramName = filename.substring(0, filename.lastIndexOf(".")).replace("_", " ").replace("-", " ");

        // Find flows markes as external in Excel File
        Set<String> externalFlows = new TreeSet<>();
        Set<FunctionalFlow> allFlows = new TreeSet<>();
        for (Map<String, Object> map : flowsDF) {
            FlowImport flowImport = mapArrayToFlowImport(map);
            if (flowImport.getFlowAlias() != null) {
                Optional<FunctionalFlow> optional = flowRepository.findByAlias(flowImport.getFlowAlias());
                if (optional.isPresent()) {
                    allFlows.add(optional.get());
                }
                if (flowImport.isExternal()) {
                    externalFlows.add(flowImport.getFlowAlias());
                }
            }
        }
        // Complete by all Flows from Landscape
        LandscapeView landscapeView = findOrCreateLandscape(diagramName);
        for (FunctionalFlow flow : landscapeView.getFlows()) {
            allFlows.add(flow);
        }

        // Detach all Flows from landscape
        // And delete if anonymous
        for (FunctionalFlow functionalFlow : allFlows) {
            landscapeView.removeFlows(functionalFlow);
            flowRepository.save(functionalFlow);
            landscapeViewRepository.save(landscapeView);
            if (functionalFlow.getAlias() == null) {
                // anonymous, so not used in another landscape
                functionalflowService.delete(functionalFlow.getId(), true, true);
            }
        }

        // Delete all steps from all FunctionalFlows except the ones markes as external
        Set<FlowGroup> groupsToDelete = new HashSet<>();
        for (FunctionalFlow functionalFlow : allFlows) {
            if (functionalFlow.getAlias() == null || !externalFlows.contains(functionalFlow.getAlias())) {
                ArrayList<FunctionalFlowStep> list = new ArrayList<>();
                list.addAll(functionalFlow.getSteps());
                for (FunctionalFlowStep step : list) {
                    if (step.getGroup() != null) {
                        groupsToDelete.add(step.getGroup());
                    }
                    FunctionalFlow flow = step.getFlow();
                    flow.removeSteps(step);
                    flowRepository.save(flow);

                    FlowInterface interface1 = step.getFlowInterface();
                    interface1.removeSteps(step);
                    interfaceRepository.save(interface1);
                    functionalFlowStepRepository.delete(step);
                }
            }
        }

        // Delete associated FlowGroups
        for (FlowGroup flowGroup : groupsToDelete) {
            flowGroupRepository.delete(flowGroup);
        }

        FlowGroup flowGroup = null;
        for (Map<String, Object> map : flowsDF) {
            FlowImport flowImport = mapArrayToFlowImport(map);

            if (flowImport.getIdFlowFromExcel() == null) {
                flowImport.setImportFunctionalFlowStatus(ImportStatus.ERROR);
                flowImport.setImportInterfaceStatus(ImportStatus.ERROR);
                flowImport.setImportDataFlowStatus(ImportStatus.ERROR);
                flowImport.setImportStatusMessage("IdFlow (interface alias) should not be null");
            } else if (flowImport.isExternal()) {
                if (flowImport.getFlowAlias() != null) {
                    Optional<FunctionalFlow> functionalFlowOption = flowRepository.findByAlias(flowImport.getFlowAlias());
                    if (functionalFlowOption.isPresent()) {
                        FunctionalFlow functionalFlow = functionalFlowOption.get();
                        functionalFlow.addLandscape(landscapeView);
                        flowRepository.save(functionalFlow);
                        landscapeViewRepository.save(landscapeView);
                    }
                } else {
                    flowImport.setImportFunctionalFlowStatus(ImportStatus.ERROR);
                    flowImport.setImportInterfaceStatus(ImportStatus.ERROR);
                    flowImport.setImportDataFlowStatus(ImportStatus.ERROR);
                    flowImport.setImportStatusMessage("Alias should not be null when external is true");
                }
            } else {
                FunctionalFlow functionalFlow = findOrCreateFunctionalFlow(flowImport);
                FlowInterface flowInterface = findOrCreateInterface(flowImport);
                Protocol protocol = findOrCreateProtocol(flowImport);
                DataFlow dataFlow = findOrCreateDataFlow(flowImport, protocol);

                if (landscapeView != null && functionalFlow != null && flowInterface != null) {
                    // Set<>, so could add even if already associated
                    functionalFlow.addLandscape(landscapeView);
                    // For first persistence
                    interfaceRepository.save(flowInterface);
                    flowRepository.save(functionalFlow);

                    // Add step
                    FunctionalFlowStep step = findOrCreateStep(flowImport, functionalFlow, flowInterface);
                    step.setFlowInterface(flowInterface);
                    functionalFlow.addSteps(step);
                    functionalFlowStepRepository.save(step);

                    if (flowImport.getGroupOrder() != null && flowImport.getGroupOrder().intValue() == 1) {
                        //start the group
                        flowGroup = new FlowGroup();
                    }
                    if (flowGroup != null) {
                        if (flowImport.getGroupOrder() == null) {
                            // close de group
                            flowGroup = null;
                        } else {
                            flowGroup.setTitle(flowImport.getGroupTitle());
                            flowGroup.setUrl(flowImport.getGroupURL());
                            flowGroup.addSteps(step);
                            functionalFlowStepRepository.save(step);
                            flowGroupRepository.save(flowGroup);

                            if (flowImport.getGroupFlowAlias() != null) {
                                Optional<FunctionalFlow> option = flowRepository.findByAlias(flowImport.getGroupFlowAlias());
                                if (option.isPresent()) {
                                    flowGroup.setFlow(option.get());
                                    functionalFlowStepRepository.save(step);
                                    flowGroupRepository.save(flowGroup);
                                } else {
                                    flowGroup.setDescription(LandscapeViewService.SHOULD_BE_LINKED_TO + flowImport.getGroupFlowAlias());
                                }
                            }
                        }
                    }

                    landscapeViewRepository.save(landscapeView);
                    if (dataFlow != null) {
                        functionalFlow.addDataFlows(dataFlow);
                        flowInterface.addDataFlows(dataFlow);
                        // validate here beacause relationship should not be null
                        validateBean(dataFlow);
                        dataFlowRepository.save(dataFlow);
                        interfaceRepository.save(flowInterface);
                    }
                    if (protocol != null) {
                        flowInterface.setProtocol(protocol);
                        // validate here beacause relationship should not be null
                        //validateBean(protocol);
                        protocolRepository.save(protocol);
                        interfaceRepository.save(flowInterface);
                    }
                } else {
                    flowInterface = null;
                    functionalFlow = null;
                }
            }

            result.add(flowImport);
        }
        return result;
    }

    private FunctionalFlowStep findOrCreateStep(FlowImport flowImport, FunctionalFlow functionalFlow, FlowInterface flowInterface) {
        FunctionalFlowStep step = new FunctionalFlowStep();
        if (functionalFlow.getSteps() == null || functionalFlow.getSteps().size() == 0) {
            step.setStepOrder(1);
        } else {
            FunctionalFlowStep lastStep = ((SortedSet<FunctionalFlowStep>) functionalFlow.getSteps()).last();
            Integer lastOrder = lastStep.getStepOrder();
            if (lastOrder != null) {
                step.setStepOrder(lastOrder + 1);
            }
        }
        step.setDescription(flowImport.getStepDescription());
        return step;
    }

    private LandscapeView findOrCreateLandscape(String diagramName) {
        LandscapeView landscapeView = landscapeViewRepository.findByDiagramNameIgnoreCase(diagramName);
        if (landscapeView == null) {
            landscapeView = mapToLandscapeView(diagramName);
            landscapeView.setViewpoint(ViewPoint.APPLICATION_LANDSCAPE);
            landscapeViewRepository.save(landscapeView);
        } else {
            Assert.isTrue(
                diagramName.equals(landscapeView.getDiagramName()),
                "Diagram exist with a different name : " + diagramName + " / " + landscapeView.getDiagramName()
            );
        }
        return landscapeView;
    }

    private FunctionalFlow findOrCreateFunctionalFlow(FlowImport flowImport) {
        FunctionalFlow functionalFlow = null;
        try {
            if (StringUtils.hasText(flowImport.getFlowAlias())) {
                Optional<FunctionalFlow> functionalFlowOption = flowRepository.findByAlias(flowImport.getFlowAlias());
                if (functionalFlowOption.isPresent()) {
                    flowImport.setImportFunctionalFlowStatus(ImportStatus.EXISTING);
                    functionalFlow = functionalFlowOption.get();
                }
            }
            if (functionalFlow == null) {
                flowImport.setImportFunctionalFlowStatus(ImportStatus.NEW);
                functionalFlow = mapToFunctionalFlow(flowImport);
            }
            setDocumentation(flowImport, functionalFlow);
            validateBean(functionalFlow);
        } catch (Exception e) {
            log.error("Error with row " + flowImport + " " + e.getMessage(), e);
            flowImport.setImportFunctionalFlowStatus(ImportStatus.ERROR);
            addError(flowImport, e);
            functionalFlow = null;
        }
        return functionalFlow;
    }

    private void setDocumentation(FlowImport flowImport, FunctionalFlow functionalFlow) {
        if (StringUtils.hasText(flowImport.getSourceURLDocumentation())) {
            functionalFlow.setDocumentationURL(flowImport.getSourceURLDocumentation());
            if (StringUtils.hasText(flowImport.getTargetURLDocumentation())) {
                functionalFlow.setDocumentationURL2(flowImport.getSourceURLDocumentation());
            }
        } else {
            if (StringUtils.hasText(flowImport.getTargetURLDocumentation())) {
                functionalFlow.setDocumentationURL(flowImport.getSourceURLDocumentation());
            }
        }
    }

    private FlowInterface findOrCreateInterface(FlowImport flowImport) {
        FlowInterface flowInterface = null;
        try {
            Optional<FlowInterface> flowInterfaceOption = interfaceRepository.findByAlias(flowImport.getIdFlowFromExcel());
            if (!flowInterfaceOption.isPresent()) {
                flowImport.setImportInterfaceStatus(ImportStatus.NEW);
                flowInterface = mapToFlowInterface(flowImport);
                Assert.isTrue(flowInterface.getSource() != null, "Source doesn't exist, pb with:" + flowImport.getSourceElement());
                Assert.isTrue(flowInterface.getTarget() != null, "Target doesn't exist, pb with:" + flowImport.getTargetElement());
            } else {
                flowImport.setImportInterfaceStatus(ImportStatus.EXISTING);
                flowInterface = flowInterfaceOption.get();
                Assert.isTrue(
                    flowInterface.getSource().getName().toLowerCase().equals(flowImport.getSourceElement().toLowerCase()) ||
                    (
                        flowInterface.getSourceComponent() != null &&
                        flowInterface.getSourceComponent().getName().toLowerCase().equals(flowImport.getSourceElement().toLowerCase())
                    ),
                    "Source of interface doesn't match for interface Id='" +
                    flowInterface.getId() +
                    "', source= [" +
                    flowInterface.getSource().getName() +
                    "], source=[" +
                    flowImport.getSourceElement() +
                    "]"
                );

                Assert.isTrue(
                    flowInterface.getTarget().getName().toLowerCase().equals(flowImport.getTargetElement().toLowerCase()) ||
                    (
                        flowInterface.getTargetComponent() != null &&
                        flowInterface.getTargetComponent().getName().toLowerCase().equals(flowImport.getTargetElement().toLowerCase())
                    ),
                    "Target of interface doesn't match for interface Id='" +
                    flowInterface.getId() +
                    "', target= [" +
                    flowInterface.getTarget().getName() +
                    "], target=[" +
                    flowImport.getTargetElement() +
                    "]"
                );
            }

            validateBean(flowInterface);
        } catch (Exception e) {
            log.error("Error with row " + flowImport + " " + e.getMessage(), e);
            flowInterface = null;
            flowImport.setImportInterfaceStatus(ImportStatus.ERROR);
            addError(flowImport, e);
        }
        return flowInterface;
    }

    private Protocol findOrCreateProtocol(FlowImport flowImport) {
        Protocol protocol = null;
        if (StringUtils.hasText(flowImport.getIntegrationPattern())) {
            protocol = protocolRepository.findByNameIgnoreCase(flowImport.getIntegrationPattern());
            if (protocol == null) {
                protocol = new Protocol();
                protocol.setName(flowImport.getIntegrationPattern());
                protocol.setType(guessPtotocolType(flowImport.getIntegrationPattern()));
                protocolRepository.save(protocol);
            } else {
                System.out.println(protocol);
                System.out.println(protocol.getId());
                System.out.println(protocol.getName());
            }
        }
        return protocol;
    }

    private void addError(FlowImport flowImport, Exception e) {
        String previous = flowImport.getImportStatusMessage() != null ? flowImport.getImportStatusMessage() : "";
        flowImport.setImportStatusMessage(previous + e.getMessage() + "  \n");
    }

    private DataFlow findOrCreateDataFlow(FlowImport flowImport, Protocol protocol) {
        DataFlow dataFlow = null;
        Set<DataFlow> potentialDataFlows = new HashSet<>();
        DataFlowComparator comparator = new DataFlowComparator();
        try {
            // Find dataflows with same functional flow and same interface
            potentialDataFlows =
                dataFlowRepository.findByFlowInterface_AliasAndFunctionalFlows_Alias(
                    flowImport.getIdFlowFromExcel(),
                    flowImport.getFlowAlias()
                );

            dataFlow = comparator.findEquivalentInCollection(flowImport, potentialDataFlows);

            if (dataFlow == null) {
                // if a dataflow exist for same interface, with same characteristics,
                // then use it adding reference to this functional flow
                potentialDataFlows = dataFlowRepository.findByFlowInterface_Alias(flowImport.getIdFlowFromExcel());
                dataFlow = comparator.findEquivalentInCollection(flowImport, potentialDataFlows);
            }

            if (dataFlow == null) {
                if (!comparator.isDataFlowEmpty(flowImport)) {
                    // Nothing found, than create a new one if necessary (frequency, format or protocol to store)
                    dataFlow = new DataFlow();
                    flowImport.setImportDataFlowStatus(ImportStatus.NEW);
                }
            } else {
                flowImport.setImportDataFlowStatus(ImportStatus.EXISTING);
            }

            // DataFlow has been found or created, update frequency, format and protocol
            if (dataFlow != null) {
                // Frequency
                if (StringUtils.hasText(flowImport.getFrequency())) {
                    dataFlow.setFrequency(comparator.getFrequency(flowImport.getFrequency()));
                }
                // Format
                if (StringUtils.hasText(flowImport.getFormat())) {
                    DataFormat dataFormat = dataFormatRepository.findByNameIgnoreCase(flowImport.getFormat());
                    if (dataFormat == null) {
                        dataFormat = new DataFormat();
                        dataFormat.setName(flowImport.getFormat());
                        dataFormatRepository.save(dataFormat);
                    }
                    dataFlow.setFormat(dataFormat);
                }
                // Contract
                if (StringUtils.hasText(flowImport.getSwagger())) {
                    dataFlow.setContractURL(flowImport.getSwagger());
                }

                // add Generic resourcename depending on protocol
                dataFlow = setGenericResourceNameIfEmpty(flowImport, protocol, dataFlow);
            }
        } catch (Exception e) {
            log.error("Error with row " + flowImport + " " + e.getMessage(), e);
            dataFlow = null;
            flowImport.setImportDataFlowStatus(ImportStatus.ERROR);
            addError(flowImport, e);
        }
        return dataFlow;
    }

    private DataFlow setGenericResourceNameIfEmpty(FlowImport flowImport, Protocol protocol, DataFlow dataFlow) {
        if (dataFlow != null && !StringUtils.hasText(dataFlow.getResourceName())) {
            if (protocol != null) {
                if (protocol.getType().equals(ProtocolType.API)) {
                    dataFlow.setResourceName("REST API Call " + flowImport.getSourceElement() + " / " + flowImport.getTargetElement());
                } else if (protocol.getType().equals(ProtocolType.EVENT)) {
                    dataFlow.setResourceName(GENERIC_EVENT_FROM_ADD);
                } else if (protocol.getType().equals(ProtocolType.FILE)) {
                    dataFlow.setResourceName(GENERIC_FILE_FROM_ADD);
                } else {
                    dataFlow.setResourceName(GENERIC_DATA_FLOW + " - " + protocol.getType());
                }
            } else {
                // Resource name is required
                dataFlow.setResourceName(GENERIC_DATA_FLOW);
            }
        }
        return dataFlow;
    }

    private FunctionalFlow mapToFunctionalFlow(FlowImport flowImport) {
        FunctionalFlow functionalFlow = new FunctionalFlow();
        functionalFlow.setAlias(flowImport.getFlowAlias());
        functionalFlow.setDescription(flowImport.getDescription());
        functionalFlow.setComment(flowImport.getComment());
        functionalFlow.setStatus(flowImport.getFlowStatus());
        return functionalFlow;
    }

    private LandscapeView mapToLandscapeView(String diagramName) {
        LandscapeView landscapeView = new LandscapeView();
        landscapeView.setDiagramName(diagramName);
        return landscapeView;
    }

    private FlowImport mapArrayToFlowImport(Map<String, Object> map) {
        FlowImport flowImport = new FlowImport();
        flowImport.setIdFlowFromExcel((String) map.get(FLOW_ID_FLOW));
        flowImport.setFlowAlias((String) map.get(FLOW_ALIAS_FLOW));
        flowImport.setSourceElement((String) map.get(FLOW_SOURCE_ELEMENT));
        flowImport.setTargetElement((String) map.get(FLOW_TARGET_ELEMENT));
        flowImport.setDescription((String) map.get(FLOW_DESCRIPTION));
        flowImport.setIntegrationPattern((String) map.get(FLOW_INTEGRATION_PATTERN));
        flowImport.setFrequency((String) map.get(FLOW_FREQUENCY));
        flowImport.setFormat((String) map.get(FLOW_FORMAT));
        flowImport.setSwagger((String) map.get(FLOW_SWAGGER));
        flowImport.setSourceURLDocumentation((String) map.get(FLOW_BLUEPRINT_SOURCE));
        flowImport.setTargetURLDocumentation((String) map.get(FLOW_BLUEPRINT_TARGET));
        flowImport.setSourceDocumentationStatus((String) map.get(FLOW_BLUEPRINT_SOURCE_STATUS));
        flowImport.setTargetDocumentationStatus((String) map.get(FLOW_BLUEPRINT_TARGET_STATUS));
        flowImport.setFlowStatus((String) map.get(FLOW_STATUS_FLOW));
        flowImport.setComment((String) map.get(FLOW_COMMENT));
        flowImport.setDocumentName((String) map.get(FLOW_ADD_CORRESPONDENT_ID));
        flowImport.setStepDescription((String) map.get(FLOW_STEP_DESCRIPTION));
        String _external = (String) map.get(FLOW_EXTERNAL);
        if (_external != null && _external.toLowerCase().trim().equals("yes")) {
            flowImport.setExternal(true);
        } else {
            flowImport.setExternal(false);
        }
        flowImport.setGroupOrder(getIntValue(map.get(FLOW_GROUP_ORDER)));
        flowImport.setGroupTitle((String) map.get(FLOW_GROUP_TITLE));
        flowImport.setGroupURL((String) map.get(FLOW_GROUP_URL));
        flowImport.setGroupFlowAlias((String) map.get(FLOW_GROUP_FLOW_ALIAS));

        return flowImport;
    }

    private Integer getIntValue(Object object) {
        if (object == null) return null;
        if (object instanceof Integer) return (Integer) object;
        if (object instanceof Double) return ((Double) object).intValue();

        throw new IllegalArgumentException();
    }

    private FlowInterface mapToFlowInterface(FlowImport flowImport) {
        Application source = applicationRepository.findByNameIgnoreCase(flowImport.getSourceElement());
        ApplicationComponent sourceComponent = null;
        if (source == null) {
            sourceComponent = applicationComponentRepository.findByNameIgnoreCase(flowImport.getSourceElement());
            if (sourceComponent != null) {
                source = sourceComponent.getApplication();
            }
        }
        Application target = applicationRepository.findByNameIgnoreCase(flowImport.getTargetElement());
        ApplicationComponent targetComponent = null;
        if (target == null) {
            targetComponent = applicationComponentRepository.findByNameIgnoreCase(flowImport.getTargetElement());
            if (targetComponent != null) {
                target = targetComponent.getApplication();
            }
        }
        FlowInterface flowInterface = new FlowInterface();
        flowInterface.setAlias(flowImport.getIdFlowFromExcel());
        flowInterface.setSource(source);
        flowInterface.setSourceComponent(sourceComponent);
        flowInterface.setTarget(target);
        flowInterface.setTargetComponent(targetComponent);
        if (StringUtils.hasText(flowImport.getIntegrationPattern())) {
            Protocol protocol = protocolRepository.findByNameIgnoreCase(flowImport.getIntegrationPattern());
            if (protocol != null) {
                flowInterface.setProtocol(protocol);
            }
        }
        return flowInterface;
    }

    private ProtocolType guessPtotocolType(String protocolName) {
        if (!StringUtils.hasText(protocolName)) return null;
        if (protocolName.toLowerCase().contains("api")) return ProtocolType.API;
        if (protocolName.toLowerCase().contains("event")) return ProtocolType.EVENT;

        if (protocolName.toLowerCase().contains("file")) return ProtocolType.FILE;
        if (protocolName.toLowerCase().contains("nas")) return ProtocolType.FILE;
        if (protocolName.toLowerCase().contains("ftp")) return ProtocolType.FILE;
        if (protocolName.toLowerCase().contains("mft")) return ProtocolType.FILE;

        if (protocolName.toLowerCase().contains("queue")) return ProtocolType.MESSAGING;
        if (protocolName.toLowerCase().contains("esb")) return ProtocolType.MESSAGING;

        return ProtocolType.OTHER;
    }

    private void validateBean(Object bean) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(bean);
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
