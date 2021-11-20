package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.repository.ProtocolRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class FlowImportService {

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
    private static final String FLOW_CHECK_COLUMN_1 = "Source Element in application list";
    private static final String FLOW_CHECK_COLUMN_2 = "Target Element in application list";
    private static final String FLOW_CHECK_COLUMN_3 = "Integration pattern in pattern list";

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

    public FlowImportService(
        FunctionalFlowRepository flowRepository,
        FlowInterfaceRepository interfaceRepository,
        ApplicationRepository applicationRepository,
        DataFlowRepository dataFlowRepository,
        OwnerRepository ownerRepository,
        LandscapeViewRepository landscapeViewRepository,
        ProtocolRepository protocolRepository
    ) {
        this.flowRepository = flowRepository;
        this.interfaceRepository = interfaceRepository;
        this.applicationRepository = applicationRepository;
        this.dataFlowRepository = dataFlowRepository;
        this.ownerRepository = ownerRepository;
        this.landscapeViewRepository = landscapeViewRepository;
        this.protocolRepository = protocolRepository;

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
        //      this.columnsArray.add(FLOW_CHECK_COLUMN_1);
        //      this.columnsArray.add(FLOW_CHECK_COLUMN_2);
        //      this.columnsArray.add(FLOW_CHECK_COLUMN_3);
    }

    public List<FlowImport> importExcel(InputStream file, String diagramName) throws EncryptedDocumentException, IOException {
        List<FlowImport> result = new ArrayList<FlowImport>();

        ExcelReader excelReader = new ExcelReader(file, this.columnsArray, FLOW_SHEET_NAME);

        List<Map<String, Object>> flowsDF = excelReader.getSheet(FLOW_SHEET_NAME);

        for (Map<String, Object> map : flowsDF) {
            FlowImport flowImport = mapArrayToFlowImport(map);

            LandscapeView landscapeView = findOrCreateLandscape(diagramName);
            landscapeViewRepository.save(landscapeView);
            FunctionalFlow functionalFlow = findOrCreateFunctionalFlow(flowImport);
            if (functionalFlow != null) {
                functionalFlow.addLandscape(landscapeView);
                flowRepository.save(functionalFlow);
            }
            FlowInterface flowInterface = findOrCreateInterface(flowImport);
            if (flowInterface != null) {
                if (!functionalFlow.getInterfaces().contains(flowInterface)) {
                    functionalFlow.addInterfaces(flowInterface);
                    interfaceRepository.save(flowInterface);
                    flowRepository.save(functionalFlow);
                }
            }
            result.add(flowImport);
        }
        return result;
    }

    private LandscapeView findOrCreateLandscape(String diagramName) {
        LandscapeView landscapeView = landscapeViewRepository.findByDiagramNameIgnoreCase(diagramName);
        if (landscapeView == null) {
            landscapeView = mapToLandscapeView(diagramName);
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
        Optional<FunctionalFlow> functionalFlowOption = flowRepository.findByAlias(flowImport.getFlowAlias());
        FunctionalFlow functionalFlow = null;
        try {
            if (!functionalFlowOption.isPresent()) {
                flowImport.setImportFunctionalFlowStatus(ImportStatus.NEW);
                functionalFlow = mapToFunctionalFlow(flowImport);
            } else {
                flowImport.setImportFunctionalFlowStatus(ImportStatus.EXISTING);
                functionalFlow = functionalFlowOption.get();
            }
            setDocumentation(flowImport, functionalFlow);
            flowRepository.save(functionalFlow);
        } catch (Exception e) {
            log.error("Error with row " + flowImport, e);
            flowImport.setImportFunctionalFlowStatus(ImportStatus.ERROR);
            flowImport.setImportStatusMessage(e.getMessage());
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
        Optional<FlowInterface> flowInterfaceOption = interfaceRepository.findByAlias(flowImport.getIdFlowFromExcel());
        FlowInterface flowInterface = null;
        try {
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
        } catch (Exception e) {
            log.error("Error with row " + flowImport, e);
            flowInterface = null;
            flowImport.setImportInterfaceStatus(ImportStatus.ERROR);
            flowImport.setImportStatusMessage(e.getMessage());
        }
        return flowInterface;
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
        Assert.isTrue(flowImport.getIdFlowFromExcel() != null, "No ID found, Error with map : " + map);
        return flowImport;
    }

    private FlowInterface mapToFlowInterface(FlowImport flowImport) {
        Application source = applicationRepository.findByNameIgnoreCase(flowImport.getSourceElement());
        Application target = applicationRepository.findByNameIgnoreCase(flowImport.getTargetElement());
        FlowInterface flowInterface = new FlowInterface();
        flowInterface.setAlias(flowImport.getIdFlowFromExcel());
        flowInterface.setSource(source);
        flowInterface.setTarget(target);
        if (!nullable(flowImport.getIntegrationPattern())) {
            Protocol protocol = protocolRepository.findByNameIgnoreCase(flowImport.getIntegrationPattern());
            if (protocol != null) {
                flowInterface.setProtocol(protocol);
            }
        }
        return flowInterface;
    }

    private boolean nullable(String value) {
        if (!StringUtils.hasText(value)) return true;
        if ("?".equals(value)) return true;
        return false;
    }
}
