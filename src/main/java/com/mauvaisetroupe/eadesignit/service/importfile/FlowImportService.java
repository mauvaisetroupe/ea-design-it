package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Protocol;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    private static final String FLOW_BLUEPRINT = "Blueprint";
    private static final String FLOW_BLUEPRINT_STATUS = "Blueprint status";
    private static final String FLOW_STATUS_FLOW = "Status flow";
    private static final String FLOW_COMMENT = "Comment";
    private static final String FLOW_ADD_CORRESPONDENT_ID = "ADD correspondent ID";

    private static final String FLOW_SHEET_NAME = "Message_Flow";

    private final Logger log = LoggerFactory.getLogger(FlowImportService.class);

    private final FunctionalFlowRepository flowRepository;
    private final FlowInterfaceRepository interfaceRepository;
    private final ApplicationRepository applicationRepository;
    private final DataFlowRepository dataFlowRepository;
    private final OwnerRepository ownerRepository;
    private final LandscapeViewRepository landscapeViewRepository;

    public FlowImportService(
        FunctionalFlowRepository flowRepository,
        FlowInterfaceRepository interfaceRepository,
        ApplicationRepository applicationRepository,
        DataFlowRepository dataFlowRepository,
        OwnerRepository ownerRepository,
        LandscapeViewRepository landscapeViewRepository
    ) {
        this.flowRepository = flowRepository;
        this.interfaceRepository = interfaceRepository;
        this.applicationRepository = applicationRepository;
        this.dataFlowRepository = dataFlowRepository;
        this.ownerRepository = ownerRepository;
        this.landscapeViewRepository = landscapeViewRepository;
    }

    public List<FlowImport> importExcel(MultipartFile file) throws Exception {
        List<FlowImport> result = new ArrayList<FlowImport>();

        ExcelReader excelReader = new ExcelReader(file);
        List<Map<String, Object>> flowsDF = excelReader.getSheet(FLOW_SHEET_NAME);

        String lowerCaseFileName = file.getOriginalFilename().toLowerCase();

        for (Map<String, Object> map : flowsDF) {
            FlowImport flowImport = mapArrayToFlowImport(map);
            Optional<FunctionalFlow> functionalFlowOption = flowRepository.findById(flowImport.getFlowAlias());

            FunctionalFlow functionalFlow;

            if (!functionalFlowOption.isPresent()) {
                flowImport.setImportFunctionalFlowStatus(ImportStatus.NEW);

                // Landscape
                LandscapeView landscapeView = landscapeViewRepository.findByDiagramNameIgnoreCase(lowerCaseFileName);
                if (landscapeView == null) {
                    landscapeView = mapToLandscapeView(lowerCaseFileName);
                    landscapeViewRepository.save(landscapeView);
                }

                // FunctionalFlow
                functionalFlow = mapToFunctionalFlow(flowImport);
                functionalFlow.setLandscape(landscapeView);
                flowRepository.save(functionalFlow);
            } else {
                functionalFlow = functionalFlowOption.get();
                flowImport.setImportFunctionalFlowStatus(ImportStatus.EXISTING);
            }

            // FlowInterface
            Optional<FlowInterface> flowInterfaceOption = interfaceRepository.findById(flowImport.getIdFlowFromExcel());
            FlowInterface flowInterface;
            if (!flowInterfaceOption.isPresent()) {
                flowInterface = mapToFlowInterface(flowImport);
                functionalFlow.addInterfaces(flowInterface);
                Assert.isTrue(
                    flowInterface.getSource() != null && flowInterface.getTarget() != null,
                    "Flow need a source and a target, pb with " + flowImport
                );
                interfaceRepository.save(flowInterface);
                flowRepository.save(functionalFlow);
            } else {
                flowInterface = flowInterfaceOption.get();
                if (!functionalFlow.getInterfaces().contains(flowInterface)) {
                    System.out.println(functionalFlow.getInterfaces().size());
                    functionalFlow.addInterfaces(flowInterface);
                    flowRepository.save(functionalFlow);
                }
            }

            result.add(flowImport);
        }
        return result;
    }

    private FunctionalFlow mapToFunctionalFlow(FlowImport flowImport) {
        FunctionalFlow functionalFlow = new FunctionalFlow();
        // use alias as ID
        functionalFlow.setId(flowImport.getFlowAlias());
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
        flowImport.setBlueprint((String) map.get(FLOW_BLUEPRINT));
        flowImport.setBlueprintStatus((String) map.get(FLOW_BLUEPRINT_STATUS));
        flowImport.setFlowStatus((String) map.get(FLOW_STATUS_FLOW));
        flowImport.setComment((String) map.get(FLOW_COMMENT));
        flowImport.setDocumentName((String) map.get(FLOW_ADD_CORRESPONDENT_ID));
        Assert.isTrue(flowImport.getIdFlowFromExcel() != null, "Error with map : " + map);
        return flowImport;
    }

    private FlowInterface mapToFlowInterface(FlowImport flowImport) {
        Application source = applicationRepository.findByNameIgnoreCase(flowImport.getSourceElement());
        Application target = applicationRepository.findByNameIgnoreCase(flowImport.getTargetElement());
        FlowInterface flowInterface = new FlowInterface();
        flowInterface.setId(flowImport.getIdFlowFromExcel());
        flowInterface.setSource(source);
        flowInterface.setTarget(target);
        if (StringUtils.hasText(flowImport.getIntegrationPattern())) {
            flowInterface.setProtocol(ObjectUtils.caseInsensitiveValueOf(Protocol.values(), clean(flowImport.getIntegrationPattern())));
        }
        return flowInterface;
    }

    private String clean(String value) {
        return value.replace('/', '_').replace(' ', '_');
    }
}
