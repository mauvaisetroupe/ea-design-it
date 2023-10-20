package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.CapabilityApplicationMappingRepository;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.dto.util.CapabilityUtil;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityAction;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportAnalysisDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityAction.Action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CapabilityImportService {

    public static final String CAPABILITY_SHEET_NAME = "Capabilities";

    private final Logger log = LoggerFactory.getLogger(CapabilityImportService.class);

    @Autowired
    private CapabilityRepository capabilityRepository;


    @Autowired
    private CapabilityApplicationMappingRepository applicationMappingRepository;

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

        Assert.isTrue(capabilitiesFromDBByFullPath.size() == inBothExcelAndDatabase.size() + onlyInDatabase.size(), "Should have same size");
        Assert.isTrue(capabilitiesFromExcelByFullPath.size() == inBothExcelAndDatabase.size() + onlyInExcel.size() , "Should have same size");

        /////////////// UPDATE DSECRIPTION
        for (String fullpath : inBothExcelAndDatabase) {
            Capability capaFromExcel = capabilitiesFromExcelByFullPath.get(fullpath);
            Capability capaFromDB = capabilitiesFromDBByFullPath.get(fullpath);
            if (StringUtils.equals(capaFromDB.getDescription(), capaFromExcel.getDescription())) {
                capaFromDB.setDescription(capaFromExcel.getDescription());
                capabilityRepository.save(capaFromDB);
            }
        }

        /////////////// CAPABILITIES TO ADD

        analysisDTO.setCapabilitiesToAdd(
            capabilityUtil.buildCapabilityTreeWithoutRoot(
                onlyInExcel.stream()
                .map(fullpath -> capabilitiesFromExcelByFullPath.get(fullpath))
                .collect(Collectors.toList()))
            .stream()
            .sorted(Comparator.comparing(c -> capabilityUtil.getCapabilityFullPath(c)))
            .map(c -> new CapabilityAction(c, Action.ADD))
            .collect(Collectors.toList())
        );
        
        // Prepare Capabilities to delete

        List<Capability> capabilitiesToDeleteWithMapping = 
            onlyInDatabase.stream()
            .map(fullpath -> capabilitiesFromDBByFullPath.get(fullpath))
            .filter(capability -> hasMapping(capability))
            .collect(Collectors.toList());

        List<Capability> capabilitiesToDeleteWithChildWithMapping =
            onlyInDatabase.stream()
            .map(fullpath -> capabilitiesFromDBByFullPath.get(fullpath))
            .filter(capability -> hasChildToDeleteWithMapping(capability, capabilitiesToDeleteWithMapping))
            .collect(Collectors.toList());


        List<Capability> capabilitiesToDelete = onlyInDatabase.stream()
            .map(fullpath -> capabilitiesFromDBByFullPath.get(fullpath))
            .filter(capability -> !hasMapping(capability))
            .filter(capability -> !hasChildToDeleteWithMapping(capability, capabilitiesToDeleteWithMapping))
            .collect(Collectors.toList());

        //////////////// CAPABILITIES TO DELETE

        analysisDTO.setCapabilitiesToDelete(
            capabilityUtil.buildCapabilityTreeWithoutRoot(capabilitiesToDelete)
            .stream()
            .sorted(Comparator.comparing(c -> capabilityUtil.getCapabilityFullPath(c)))
            .map(c -> new CapabilityAction(c, Action.DELETE))
            .collect(Collectors.toList())            
        );

        //////////////// CAPABILITIES TO DELETE WITH MAPPING

        analysisDTO.setCapabilitiesToDeleteWithMappings(
            capabilitiesToDeleteWithMapping
            .stream()
            .sorted(Comparator.comparing(c -> capabilityUtil.getCapabilityFullPath(c)))
            .map(c -> new CapabilityAction(c, Action.FORCE_DELETE))
            .collect(Collectors.toList())              
            );
            // do not group in order to move separately 

        ////////////////// ANCESTORS OF CAPABILITIES WITH MAPPPINGS
        
        analysisDTO.setAncestorsOfCapabilitiesWithMappings(
            capabilityUtil.buildCapabilityTreeWithoutRoot(capabilitiesToDeleteWithChildWithMapping)
            .stream()
            .sorted(Comparator.comparing(c -> capabilityUtil.getCapabilityFullPath(c)))
            .map(c -> new CapabilityAction(c, Action.IGNORE))
            .collect(Collectors.toList())              
        );
        return analysisDTO;
    }
    
    private boolean hasChildToDeleteWithMapping(Capability capability,  List<Capability> capabilitiesToDeleteWithMapping) {
        return hasChildToDeleteWithMapping(capability, capabilitiesToDeleteWithMapping, true);
    }

    private boolean hasChildToDeleteWithMapping(Capability capability,  List<Capability> capabilitiesToDeleteWithMapping, boolean isFirstCapability) {
        if (!isFirstCapability && capabilityUtil.contains(capabilitiesToDeleteWithMapping, capability)) {
            return true;
        }
        if (capability.getSubCapabilities() != null && !capability.getSubCapabilities().isEmpty()) {
            for (Capability child : capability.getSubCapabilities()) { 
                if (hasChildToDeleteWithMapping(child, capabilitiesToDeleteWithMapping, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasMapping(Capability capability) {
        return capability.getCapabilityApplicationMappings() != null && !capability.getCapabilityApplicationMappings().isEmpty();
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


    public List<CapabilityImportDTO>  confirmImport(CapabilityImportAnalysisDTO analysisDTO) {
        List<CapabilityImportDTO> capabilityImportDTOs = new ArrayList<>();
        
        // UPDATE existing

        // Add new capabilities        
        for (CapabilityAction capabilityAction : analysisDTO.getCapabilitiesToAdd()) {
            if (capabilityAction.getAction() == Action.ADD) {                
                // Find a persisted parent
                capabilityAction.getCapability().setParent(findPersistedParent(capabilityAction.getCapability()));
                persistCapabilityTree(capabilityAction.getCapability());
                capabilityImportDTOs.add(capabilityUtil.buildImportDTO(capabilityAction.getCapability(), "ADDED", ImportStatus.NEW));
            }
            else if (capabilityAction.getAction() == Action.IGNORE){
                capabilityImportDTOs.add(capabilityUtil.buildImportDTO(capabilityAction.getCapability(), "Ignored", ImportStatus.ERROR));
            }
        }

        // Delete capabilities        
        List<Capability> doDelete = new ArrayList<>();
        for (CapabilityAction capabilityAction : analysisDTO.getCapabilitiesToDelete()) {
            if (capabilityAction.getAction() == Action.DELETE) {                
                // Find a persisted parent
                capabilityAction.getCapability().setParent(findPersistedParent(capabilityAction.getCapability()));
                capabilityImportDTOs.add(capabilityUtil.buildImportDTO(capabilityAction.getCapability(), "DELETED", ImportStatus.UPDATED));
                doDelete.add(capabilityAction.getCapability());
            }
            else if (capabilityAction.getAction() == Action.IGNORE){
                capabilityImportDTOs.add(capabilityUtil.buildImportDTO(capabilityAction.getCapability(), "Ignored", ImportStatus.ERROR));
            }
        }
        deleteCapabilityTrees(doDelete, false);

        // Delete capabilities with mappings 
        // doDelete = new ArrayList<>();
        for (CapabilityAction capabilityAction : analysisDTO.getCapabilitiesToDeleteWithMappings()) {
            if (capabilityAction.getAction() == Action.FORCE_DELETE) {                
                // Find a persisted parent
                capabilityAction.getCapability().setParent(findPersistedParent(capabilityAction.getCapability()));
                capabilityImportDTOs.add(capabilityUtil.buildImportDTO(capabilityAction.getCapability(), "DELETED", ImportStatus.UPDATED));
                doDelete.add(capabilityAction.getCapability());
            }
            else if (capabilityAction.getAction() == Action.IGNORE){
                capabilityImportDTOs.add(capabilityUtil.buildImportDTO(capabilityAction.getCapability(), "Ignored", ImportStatus.ERROR));
            }
        }
        deleteCapabilityTrees(doDelete, true);

        return capabilityImportDTOs;
    }

    private void deleteCapabilityTrees(List<Capability> doDelete, boolean deleteMappings) {
        for (Capability capability : doDelete) {
            deleteCapabilityTrees(capability, deleteMappings);
        }
    }

    private void deleteCapabilityTrees(Capability capability,  boolean deleteMappings) {

        //Delete children recursively
        List<Capability> children = new ArrayList<>(capability.getSubCapabilities());
        if (children!=null) {
            for (Capability child : children) {
                deleteCapabilityTrees(child, deleteMappings);
            }
        }
        
        // Detach parent 
        Capability parent = capability.getParent();
        if (parent!=null)  {
            parent.removeSubCapabilities(capability);
            capabilityRepository.save(parent);
            capabilityRepository.save(capability);
        }

        // Remove mappings if needed
        if (deleteMappings) {
            Set<CapabilityApplicationMapping> mappings = new HashSet<>(capability.getCapabilityApplicationMappings());
            for (CapabilityApplicationMapping mapping : mappings) {
                capability.removeCapabilityApplicationMapping(mapping);
                capabilityRepository.save(capability);
                applicationMappingRepository.save(mapping);
                // if capabiltyMapping have no other landscape, delete it
                if (mapping.getLandscapes() == null || mapping.getLandscapes().isEmpty()) {
                    applicationMappingRepository.delete(mapping);
                }
            }
        }
    
        // Delete capability
        capabilityRepository.delete(capability);
    }

    private void persistCapabilityTree(Capability capability) {
        capabilityRepository.save(capability);
        if (capability!=null && capability.getSubCapabilities()!=null) {
            for (Capability child : capability.getSubCapabilities()) {
                persistCapabilityTree(child);                
            }
        }
    }

    private Capability findPersistedParent(Capability capability) {
        if (capability.getParent()!=null) {
            List<Capability> potemtials = capabilityRepository.findByNameIgnoreCaseAndLevel(
                    capability.getParent().getName(), 
                    capability.getParent().getLevel()
            );
            if (potemtials.size() == 1) {
                return potemtials.get(0);
            }
            else if (potemtials.size() > 1) {
                for (Capability potential : potemtials) {
                  if (potential.getParent().getName().equals(capability.getParent().getParent().getName())) {
                    return potential;
                  }
                }
                throw new IllegalStateException("Multiple potential parents");
            }
        }     
        return null;
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
