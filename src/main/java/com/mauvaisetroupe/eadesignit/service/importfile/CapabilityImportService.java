package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CapabilityImportService {

    public static final String CAPABILITY_SHEET_NAME = "Capabilities";

    private final Logger log = LoggerFactory.getLogger(CapabilityImportService.class);

    @Autowired
    private CapabilityRepository capabilityRepository;

    public static final String L0_NAME = "Capability L0";
    public static final String L0_DESCRIPTION = "L0 - Description";
    public static final String L1_NAME = "Capability L1";
    public static final String L1_DESCRIPTION = "L1 - Description";
    public static final String L2_NAME = "Capability L2";
    public static final String L2_DESCRIPTION = "L2 - Description";
    public static final String L3_NAME = "Capability L3";
    public static final String L3_DESCRIPTION = "L3 - Description";
    public static final String SUR_DOMAIN = "Sur-domaine";
    public static final String SUR_DOMAIN_DESCRIPTION = "Sur-domaine Description";    
    public static final String FULL_PATH = "full.path";

    public List<CapabilityImportDTO> importExcel(InputStream excel, String originalFilename) throws EncryptedDocumentException, IOException {
        // this line generates error beacause a deleted capability could be a parent of a one non deleted
        // capabilityRepository.deleteByCapabilityApplicationMappingsIsEmpty();
        Map<String,Capability> capabilitiesByFullPath  = initCapabilitiesByName();
        ExcelReader capabilityFlowExcelReader = new ExcelReader(excel);
        List<Map<String, Object>> capabilitiesDF = capabilityFlowExcelReader.getSheet(CAPABILITY_SHEET_NAME);
        Capability rootImport = new Capability("ROOT", -2);
        
        List<CapabilityImportDTO> result = new ArrayList<CapabilityImportDTO>();
        for (Map<String, Object> map : capabilitiesDF) {
            CapabilityImportDTO capabilityImportDTO = getDTOFromExcel(rootImport, map);
            if (checkLineIsValid(capabilityImportDTO)) {
                try {
                    List<Capability> capabilitiesImports = capabilityImportDTO.getCapabilityList();
                    boolean somethingNew = false;
                    Capability parent = null;
                    for (Capability capabilityImport : capabilitiesImports) {
                        if (capabilityImport != null) {
                            if (parent!=null) {
                                parent.addSubCapabilities(capabilityImport);
                            }                            
                            Capability capability = capabilitiesByFullPath.get(getFullPath(capabilityImport));
                            if (capability == null) {
                                capability = capabilityImport;
                                capabilityRepository.save(capability);
                                capabilitiesByFullPath.put(getFullPath(capability), capability);
                                somethingNew = true;                            
                            }
                            parent = capability;     
                        }
                    }
                    capabilityImportDTO.setStatus(somethingNew ? ImportStatus.NEW : ImportStatus.EXISTING);
                } catch (Exception e) {
                    capabilityImportDTO.setStatus(ImportStatus.ERROR);
                    capabilityImportDTO.setError(e.toString());
                    e.printStackTrace();
                }
            } else {
                capabilityImportDTO.setStatus(ImportStatus.ERROR);
                capabilityImportDTO.setError("Line is not vallid");
            }
            //Transforming JSON based on modified DTO, with ROOT, parent and subcapabilities create a nver-ending process
            //So create a neww one withous any relationships
            CapabilityImportDTO capabilityImportDTO2 = getDTOFromExcel(null, map);
            capabilityImportDTO2.setError(capabilityImportDTO.getError());
            capabilityImportDTO2.setStatus(capabilityImportDTO.getStatus()); 
            result.add(capabilityImportDTO2);
        }
        return result;
    }

    private CapabilityImportDTO getDTOFromExcel(Capability rootImport, Map<String, Object> map) {
        // new capability created from excel, without parent assigned
        Capability domainImport = null, l0Import = null, l1Import = null, l2Import = null, l3Import = null;
        if (map.get(SUR_DOMAIN) != null) domainImport = new Capability((String) map.get(SUR_DOMAIN), -1, (String) map.get(SUR_DOMAIN_DESCRIPTION)); 
        if (map.get(L0_NAME) != null) l0Import = new Capability((String) map.get(L0_NAME), 0, (String) map.get(L0_DESCRIPTION));
        if (map.get(L1_NAME) != null) l1Import = new Capability((String) map.get(L1_NAME), 1, (String) map.get(L1_DESCRIPTION));
        if (map.get(L2_NAME) != null) l2Import = new Capability((String) map.get(L2_NAME), 2, (String) map.get(L2_DESCRIPTION));
        if (map.get(L3_NAME) != null) l3Import = new Capability((String) map.get(L3_NAME), 3, (String) map.get(L3_DESCRIPTION));
        CapabilityImportDTO capabilityImportDTO = new CapabilityImportDTO(rootImport, domainImport,l0Import, l1Import, l2Import, l3Import);
        return capabilityImportDTO;
    }

    private boolean checkLineIsValid(CapabilityImportDTO capabilityImport) {
        boolean nullFound = false;
        for (Capability capability : capabilityImport.getCapabilityList()) {
            if (capability!=null && nullFound) return false;
            if (capability == null) nullFound = true;
        }
        return true;
    }
    
    // fullpath helpers
    
    private Map<String,Capability>  initCapabilitiesByName() {
        Map<String,Capability> capabilitiesByFllPath = new HashMap<>();
        Set<Capability> allCapabilities = capabilityRepository.findAllWithSubCapabilities();
        for (Capability capability : allCapabilities) {
            capabilitiesByFllPath.put(getFullPath(capability), capability);
        }  
        return capabilitiesByFllPath;      
    }

    private String getFullPath(Capability capability) {
        String separator = "";
        StringBuilder builder = new StringBuilder();
        while (capability != null) {
            builder.insert(0,separator);
            builder.insert(0,capability.getName());
            separator = " > ";
            capability = capability.getParent();
            
        }
        return builder.toString().toLowerCase();
    }
}
