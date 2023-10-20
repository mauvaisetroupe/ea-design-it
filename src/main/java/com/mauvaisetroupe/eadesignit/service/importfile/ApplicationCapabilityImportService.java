package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.CapabilityApplicationMappingRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.dto.util.CapabilityUtil;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ApplicationCapabilityItemDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.util.SummaryImporterService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationCapabilityImportService {

    private final Logger log = LoggerFactory.getLogger(ApplicationCapabilityImportService.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CapabilityApplicationMappingRepository capabilityApplicationMappingRepository;

    @Autowired
    private LandscapeViewRepository landscapeViewRepository;

    @Autowired
    private SummaryImporterService summaryImporterService;

    @Autowired
    private CapabilityUtil capabilityUtil;

    public static final String APP_NAME_1 = "application.name.1";
    public static final String APP_NAME_2 = "application.name.2";
    public static final String APP_NAME_3 = "application.name.3";
    public static final String APP_NAME_4 = "application.name.4";
    public static final String APP_NAME_5 = "application.name.5";
    public static final String FULL_PATH = "full.path";

    public List<ApplicationCapabilityItemDTO> importExcel(InputStream excel, String sheetname)
        throws EncryptedDocumentException, IOException {
        ExcelReader capabilityFlowExcelReader = new ExcelReader(excel);
        List<ApplicationCapabilityItemDTO> result = new ArrayList<ApplicationCapabilityItemDTO>();

        String diagramName = summaryImporterService.findLandscape(capabilityFlowExcelReader, sheetname);
        LandscapeView landscape = landscapeViewRepository.findByDiagramNameIgnoreCase(diagramName);

        // remove mapping from landscape and delete  CapabilityMapping if not refernced by another landscape
        Set<CapabilityApplicationMapping> capabilityApplicationMappings = new HashSet<>(landscape.getCapabilityApplicationMappings());
        for (CapabilityApplicationMapping cm : capabilityApplicationMappings) {            
            
            // remove capabilityMapping from landscape
            landscape.removeCapabilityApplicationMapping(cm);

            // if capabiltyMapping have no other landscape, delete it
            if (cm.getLandscapes() == null || cm.getLandscapes().isEmpty()) {
                capabilityApplicationMappingRepository.delete(cm);
            }

        }

        Map<String,Capability> capabilitiesByFullPath  = capabilityUtil.initCapabilitiesByNameFromDB();

        List<Map<String, Object>> capabilitiesDF = capabilityFlowExcelReader.getSheet(sheetname);
        for (Map<String, Object> map : capabilitiesDF) {
            ApplicationCapabilityItemDTO itemDTO = new ApplicationCapabilityItemDTO();

            String fullPath = (String) map.get(FULL_PATH);
            CapabilityImportDTO capabilityImportDTO = capabilityUtil.getCapabilityImportDTO(fullPath);
            itemDTO.setCapabilityImportDTO(capabilityImportDTO);
            itemDTO.setApplicationNames(mapArrayToString(map));

            // fullpath from ecel does not inclute ROOT >
            Capability capability = capabilitiesByFullPath.get(capabilityUtil.getCapabilityFullPath(capabilityImportDTO));
            List<Application> applications = findApplication(map, itemDTO);

            if (applications.isEmpty()) {
                String error = "No application found : " + map;
                itemDTO.setErrorMessage(error);
                log.error(error);
            } else if (capability == null) {
                String error = "No Capability found : " + map + " - " + fullPath;
                itemDTO.setErrorMessage(error);
                log.error(error);
            } else {
                for (Application application : applications) {
                    CapabilityApplicationMapping capabilityApplicationMapping = capabilityApplicationMappingRepository.findByApplicationAndCapability(
                        application,
                        capability
                    );
                    if (capabilityApplicationMapping == null) {
                        capabilityApplicationMapping = new CapabilityApplicationMapping();
                        capabilityApplicationMappingRepository.save(capabilityApplicationMapping);
                    }
                    application.addCapabilityApplicationMapping(capabilityApplicationMapping);
                    capability.addCapabilityApplicationMapping(capabilityApplicationMapping);
                    if (landscape != null) {
                        landscape.addCapabilityApplicationMapping(capabilityApplicationMapping);
                    }
                }
                if (itemDTO.getImportStatus() == null) {
                    itemDTO.setImportStatus(ImportStatus.NEW);
                }
            }
            result.add(itemDTO);
        }
        return result;
    }

    private List<Application> findApplication(Map<String, Object> map, ApplicationCapabilityItemDTO applicationCapability) {
        String[] apps = { APP_NAME_1, APP_NAME_2, APP_NAME_3, APP_NAME_4, APP_NAME_5 };
        List<Application> result = new ArrayList<>();
        for (String appName : apps) {
            String app1 = getApplicationName(map, appName);
            if (app1 != null) {
                Application application = applicationRepository.findByNameIgnoreCase(app1);
                if (application != null) {
                    result.add(application);
                } else {
                    processError(applicationCapability, "Can not find application : [" + app1 + "]");
                }
            }
        }
        return result;
    }

    private List<String> mapArrayToString(Map<String, Object> map) {
        String[] apps = { APP_NAME_1, APP_NAME_2, APP_NAME_3, APP_NAME_4, APP_NAME_5 };
        List<String> result = new ArrayList<>();
        for (String appName : apps) {
            result.add(getApplicationName(map, appName));
        }
        return result;
    }

    private String getApplicationName(Map<String, Object> map, String appName1) {
        Object obj = map.get(appName1);
        if (obj != null) {
            return obj.toString().trim();
        }
        return null;
    }

    private void processError(ApplicationCapabilityItemDTO applicationCapability, String error) {
        log.error(error);
        applicationCapability.setImportStatus(ImportStatus.ERROR);
        applicationCapability.setErrorMessage(error);
    }
}
