package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.DataFormat;
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
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFormatRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowStepRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.repository.ProtocolRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class FlowImportService {

    private static final String GENERIC_DATA_FLOW = "GENERIC DATAFLOW from ADD";
    private static final String GENERIC_FILE_FROM_ADD = "GENERIC FILE from ADD";
    private static final String GENERIC_EVENT_FROM_ADD = "GENERIC EVENT from ADD";

    private static final String FLOW_ID_FLOW = "Id flow";
    private static final String FLOW_ALIAS_FLOW = "Alias flow";
    private static final String FLOW_SOURCE_ELEMENT = "Source Element";
    private static final String FLOW_TARGET_ELEMENT = "Target Element";
    private static final String FLOW_DESCRIPTION = "Description";
    private static final String FLOW_INTEGRATION_PATTERN = "Integration pattern";
    private static final String FLOW_FREQUENCY = "Frequency";
    private static final String FLOW_FORMAT = "Format";
    private static final String FLOW_SWAGGER = "Swagger";
    private static final String FLOW_BLUEPRINT_SOURCE = "Blueprint From Source";
    private static final String FLOW_BLUEPRINT_TARGET = "Blueprint From Target";
    private static final String FLOW_BLUEPRINT_SOURCE_STATUS = "Status Blueprint From Source";
    private static final String FLOW_BLUEPRINT_TARGET_STATUS = "Status Blueprint From Target";
    private static final String FLOW_STATUS_FLOW = "Status flow";
    private static final String FLOW_COMMENT = "Comment";
    private static final String FLOW_ADD_CORRESPONDENT_ID = "ADD correspondent ID";
    private static final String FLOW_STEP_DESCRIPTION = "Step description";

    private final List<String> columnsArray = new ArrayList<String>();

    private static final String FLOW_SHEET_NAME = "Message_Flow";

    private final Logger log = LoggerFactory.getLogger(FlowImportService.class);

    private final FunctionalFlowRepository flowRepository;
    private final FlowInterfaceRepository interfaceRepository;
    private final ApplicationRepository applicationRepository;
    private final DataFlowRepository dataFlowRepository;
    private final OwnerRepository ownerRepository;
    private final LandscapeViewRepository landscapeViewRepository;
    private final ProtocolRepository protocolRepository;
    private final DataFormatRepository dataFormatRepository;
    private final FunctionalFlowStepRepository functionalFlowStepRepository;

    public FlowImportService(
        FunctionalFlowRepository flowRepository,
        FlowInterfaceRepository interfaceRepository,
        ApplicationRepository applicationRepository,
        DataFlowRepository dataFlowRepository,
        OwnerRepository ownerRepository,
        LandscapeViewRepository landscapeViewRepository,
        ProtocolRepository protocolRepository,
        DataFormatRepository dataFormatRepository,
        FunctionalFlowStepRepository functionalFlowStepRepository
    ) {
        this.flowRepository = flowRepository;
        this.interfaceRepository = interfaceRepository;
        this.applicationRepository = applicationRepository;
        this.dataFlowRepository = dataFlowRepository;
        this.ownerRepository = ownerRepository;
        this.landscapeViewRepository = landscapeViewRepository;
        this.protocolRepository = protocolRepository;
        this.dataFormatRepository = dataFormatRepository;
        this.functionalFlowStepRepository = functionalFlowStepRepository;

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
    }

    public List<FlowImport> importExcel(InputStream file, String filename) throws EncryptedDocumentException, IOException {
        List<FlowImport> result = new ArrayList<FlowImport>();

        ExcelReader excelReader = new ExcelReader(file);
        List<Map<String, Object>> flowsDF = excelReader.getSheet(FLOW_SHEET_NAME);
        String diagramName = filename.substring(0, filename.lastIndexOf(".")).replace("_", " ").replace("-", " ");

        for (Map<String, Object> map : flowsDF) {
            FlowImport flowImport = mapArrayToFlowImport(map);

            if (flowImport.getIdFlowFromExcel() != null && flowImport.getFlowAlias() != null) {
                LandscapeView landscapeView = findOrCreateLandscape(diagramName);
                FunctionalFlow functionalFlow = findOrCreateFunctionalFlow(flowImport);
                FlowInterface flowInterface = findOrCreateInterface(flowImport);
                Protocol protocol = findOrCreateProtocol(flowImport);
                DataFlow dataFlow = findOrCreateDataFlow(flowImport, protocol);

                if (landscapeView != null && functionalFlow != null && flowInterface != null) {
                    // Set<>, so could add even if already associated
                    functionalFlow.addLandscape(landscapeView);

                    // Add step
                    FunctionalFlowStep step = findOrCreateStep(flowImport, functionalFlow, flowInterface);
                    step.setFlowInterface(flowInterface);
                    functionalFlow.addSteps(step);
                    interfaceRepository.save(flowInterface);
                    flowRepository.save(functionalFlow);
                    functionalFlowStepRepository.save(step);

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
                        validateBean(protocol);
                        protocolRepository.save(protocol);
                        interfaceRepository.save(flowInterface);
                    }
                } else {
                    flowInterface = null;
                    functionalFlow = null;
                }
            } else {
                flowImport.setImportFunctionalFlowStatus(ImportStatus.ERROR);
                flowImport.setImportInterfaceStatus(ImportStatus.ERROR);
                flowImport.setImportDataFlowStatus(ImportStatus.ERROR);
                flowImport.setImportStatusMessage("IdFlow & Alias should not be null");
            }

            result.add(flowImport);
        }
        return result;
    }

    private FunctionalFlowStep findOrCreateStep(FlowImport flowImport, FunctionalFlow functionalFlow, FlowInterface flowInterface) {
        Optional<FunctionalFlowStep> optional = functionalFlowStepRepository.findByFlowAliasAndFlowInterfaceAliasIgnoreCase(
            functionalFlow.getAlias(),
            flowInterface.getAlias()
        );
        if (optional.isPresent()) {
            return optional.get();
        } else {
            FunctionalFlowStep step = new FunctionalFlowStep();
            return step;
        }
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
            Optional<FunctionalFlow> functionalFlowOption = flowRepository.findByAlias(flowImport.getFlowAlias());
            if (!functionalFlowOption.isPresent()) {
                flowImport.setImportFunctionalFlowStatus(ImportStatus.NEW);
                functionalFlow = mapToFunctionalFlow(flowImport);
            } else {
                flowImport.setImportFunctionalFlowStatus(ImportStatus.EXISTING);
                functionalFlow = functionalFlowOption.get();
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
                    flowInterface.getSource().getName().toLowerCase().equals(flowImport.getSourceElement().toLowerCase()),
                    "Source of interface doesn't match for interface Id='" +
                    flowInterface.getId() +
                    "', source= [" +
                    flowInterface.getSource().getName() +
                    "], source=[" +
                    flowImport.getSourceElement() +
                    "]"
                );

                Assert.isTrue(
                    flowInterface.getTarget().getName().toLowerCase().equals(flowImport.getTargetElement().toLowerCase()),
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
            log.error("Error with row " + flowImport + " " + e.getMessage());
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
        return flowImport;
    }

    private FlowInterface mapToFlowInterface(FlowImport flowImport) {
        Application source = applicationRepository.findByNameIgnoreCase(flowImport.getSourceElement());
        Application target = applicationRepository.findByNameIgnoreCase(flowImport.getTargetElement());
        FlowInterface flowInterface = new FlowInterface();
        flowInterface.setAlias(flowImport.getIdFlowFromExcel());
        flowInterface.setSource(source);
        flowInterface.setTarget(target);
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
