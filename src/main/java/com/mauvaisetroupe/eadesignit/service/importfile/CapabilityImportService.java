package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.dto.util.CapabilityUtil;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityAction;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportAnalysisDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityAction.Action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private CapabilityUtil capabilityUtil;

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

    public CapabilityImportAnalysisDTO  analyzeExcel(InputStream excel, String originalFilename) throws EncryptedDocumentException, IOException {

        CapabilityImportAnalysisDTO analysisDTO = new CapabilityImportAnalysisDTO();
        ExcelReader capabilityFlowExcelReader = new ExcelReader(excel);
        List<Map<String, Object>> capabilitiesDF = capabilityFlowExcelReader.getSheet(CAPABILITY_SHEET_NAME);


        // Capabilities from DB
        Map<String,Capability> capabilitiesFromDBByFullPath  = capabilityUtil.initCapabilitiesByNameFromDB();
        
        // Capabilities fromExcel
        Map<String,Capability> capabilitiesFromExcelByFullPath  = initCapabilitiesByNameFromExcel(capabilitiesDF);

        // Errors from Excel 
        analysisDTO.setErrorLines(getErrorLines(capabilitiesDF));

        List<String> onlyInExcel = new ArrayList<>(capabilitiesFromExcelByFullPath.keySet());
        onlyInExcel.removeAll(capabilitiesFromDBByFullPath.keySet());

        List<String> onlyInDatabase = new ArrayList<>(capabilitiesFromDBByFullPath.keySet());
        onlyInDatabase.removeAll(capabilitiesFromExcelByFullPath.keySet());    
        
        List<String> inBothExcelAndDatabase = new ArrayList<>(capabilitiesFromDBByFullPath.keySet());
        inBothExcelAndDatabase.removeAll(onlyInDatabase);
        inBothExcelAndDatabase.removeAll(onlyInExcel);
        
        analysisDTO.setCapabilitiesToAdd(
            capabilityUtil.buildCapabilityTreeWithoutRoot(
                onlyInExcel.stream()
                .map(fullpath -> capabilitiesFromExcelByFullPath.get(fullpath))
                .collect(Collectors.toList()))
            .stream()
            .map(c -> new CapabilityAction(c, Action.ADD))
            .collect(Collectors.toList())
        );
        
        analysisDTO.setCapabilitiesToDelete(
            capabilityUtil.buildCapabilityTreeWithoutRoot(
                onlyInDatabase.stream()
                .filter(fullpath -> capabilitiesFromDBByFullPath.get(fullpath).getCapabilityApplicationMappings() == null || capabilitiesFromDBByFullPath.get(fullpath).getCapabilityApplicationMappings().isEmpty())
                .map(fullpath -> capabilitiesFromDBByFullPath.get(fullpath))
                .collect(Collectors.toList()))
            .stream()
            .map(c -> new CapabilityAction(c, Action.DELETE))
            .collect(Collectors.toList())            
        );


        analysisDTO.setCapabilitiesToDeleteWithMappings(
            onlyInDatabase.stream()
            .filter(fullpath -> capabilitiesFromDBByFullPath.get(fullpath).getCapabilityApplicationMappings() != null && !capabilitiesFromDBByFullPath.get(fullpath).getCapabilityApplicationMappings().isEmpty())
            .map(fullpath -> capabilitiesFromDBByFullPath.get(fullpath))
            .map(c -> new CapabilityAction(c, Action.FORCE_DELETE))
            .collect(Collectors.toList())              
        );
        // do not group in order to move separately 
        //analysisDTO.setCapabilitiesToDeleteWithMappings(capabilityUtil.buildCapabilityTreeWithoutRoot(analysisDTO.getCapabilitiesToDeleteWithMappings()));
        
        return analysisDTO;

    }    

    private List<String> getErrorLines(List<Map<String, Object>> capabilitiesDF) {
        
        List<String> errors = new ArrayList<>();

        Capability rootImport = new Capability("ROOT", -2);        

        for (Map<String, Object> map : capabilitiesDF) {
            CapabilityImportDTO capabilityImportDTO = getDTOFromExcel(rootImport, map);
            if (!checkLineIsValid(capabilityImportDTO)) {
                errors.add(capabilityUtil.getCapabilityFullPath(capabilityImportDTO));
            }
        }

        return errors;
    }

    private Map<String, Capability> initCapabilitiesByNameFromExcel(List<Map<String, Object>> capabilitiesDF) throws EncryptedDocumentException, IOException {
        
        Map<String,Capability> capabilitiesByFullPath = new HashMap<>(); 
        
        Capability rootImport = new Capability("ROOT", -2);        
        for (Map<String, Object> map : capabilitiesDF) {
            CapabilityImportDTO capabilityImportDTO = getDTOFromExcel(rootImport, map);
            if (checkLineIsValid(capabilityImportDTO)) {
                List<Capability> capabilities = capabilityImportDTO.getCapabilityList();
                Capability parent = null;
                for (Capability capability : capabilities) {
                    if (capability != null) {
                        if (parent!=null) {
                            parent.addSubCapabilities(capability);
                        }
                        parent = capability;
                        capabilitiesByFullPath.put(capabilityUtil.getCapabilityFullPath(capability),capability);
                    }
                }
            }
        }

        return capabilitiesByFullPath;
    }    

    public List<CapabilityImportDTO> importExcel(InputStream excel, String originalFilename) throws EncryptedDocumentException, IOException {
        // this line generates error beacause a deleted capability could be a parent of a one non deleted
        // capabilityRepository.deleteByCapabilityApplicationMappingsIsEmpty();
        Map<String,Capability> capabilitiesByFullPath  = capabilityUtil.initCapabilitiesByNameFromDB();
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
                            Capability capability = capabilitiesByFullPath.get(capabilityUtil.getCapabilityFullPath(capabilityImport));
                            if (capability == null) {
                                capability = capabilityImport;
                                capabilityRepository.save(capability);
                                capabilitiesByFullPath.put(capabilityUtil.getCapabilityFullPath(capability), capability);
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

}
