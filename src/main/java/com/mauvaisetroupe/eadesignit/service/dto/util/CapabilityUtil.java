package com.mauvaisetroupe.eadesignit.service.dto.util;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.dto.mapper.CapabilityMapper;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CapabilityUtil {
    
    @Autowired
    CapabilityRepository capabilityRepository;

    public Capability buildCapabilityTree(Collection<Capability> inputs) {
        if (inputs == null || inputs.isEmpty()) return null;
        List<Capability> listOfRoots = buildCapabilityTree(inputs, true);
        Assert.isTrue(listOfRoots.size() == 1, "We should have a unique ROOT element " + listOfRoots);
        return listOfRoots.iterator().next();
    }

    public List<Capability> buildCapabilityTreeWithoutRoot(Collection<Capability> inputs) {
        List<Capability> listOfRoots = buildCapabilityTree(inputs, false);
        return listOfRoots;
    }    

    private List<Capability> buildCapabilityTree(Collection<Capability> inputs, boolean includeRoots) {
        // Merge all capabilities finding common parents
        // Return merged capabilities by root (and not by leaf), inverse manyToOne relationship

        List<Capability> listOfRoots = new ArrayList<>();

        if (inputs == null || inputs.isEmpty()) return listOfRoots;
        
        CapabilityMapper mapper = new CapabilityMapper();
        // Fullpath is not a solution for key, 
        Map<String, Capability> capabilityByFullpath = new HashMap<>();

        for (Capability capability : inputs) {
            capability.getApplications();
            boolean found = false;
            Capability tmpCapability = capability;
            Capability childDTO = null;
            Capability dto = null;
            Capability root = null;
            while (tmpCapability != null && !found) {
                if (capabilityByFullpath.containsKey(getCapabilityFullPath(tmpCapability))) {
                    found = true;
                    dto = capabilityByFullpath.get(getCapabilityFullPath(tmpCapability));
                } else {
                    // Clone capability, mapping and application to avoid hibernate side effects
                    dto = mapper.clone(tmpCapability);
                    capabilityByFullpath.put(getCapabilityFullPath(tmpCapability), dto);
                }
                if (childDTO != null) {
                    dto.addSubCapabilities(childDTO);
                }
                childDTO = dto;
                if (includeRoots || contains(inputs, tmpCapability)) {
                    root = dto;
                }
                tmpCapability = tmpCapability.getParent();
            }
            addToRootList(listOfRoots, root);
        }
        return listOfRoots;
    }

    private void addToRootList(List<Capability> listOfRoots, Capability dto) {
        // COMMENT SAVOIR SI CEST UN ROOT LOCAL ?
        if (dto != null) {
            if (listOfRoots.isEmpty()) {
                listOfRoots.add(dto);                
            }
            else {
                Iterator<Capability> i = listOfRoots.iterator();
                boolean stopProcessing = false;
                while (i.hasNext() && !stopProcessing) {     
                    Capability rootCapability = i.next(); 
                    if (getCapabilityFullPath(rootCapability).contains(getCapabilityFullPath(dto))) {
                        // root is a child, replace root by DTO
                        i.remove();
                        listOfRoots.add(dto);  
                        stopProcessing = true;
                    } else if (getCapabilityFullPath(dto).contains(getCapabilityFullPath(rootCapability))) {
                        // dto is a child, we keep root and ignore dto
                        stopProcessing = true;             
                    }
                }
                if (!stopProcessing) {
                    listOfRoots.add(dto);
                }
            }
        }
    }
        
    private boolean contains(Collection<Capability> inputs, Capability tmpCapability) {
        if (tmpCapability == null) return false;
        for (Capability capability : inputs) {
            if (capability.getLevel().equals(tmpCapability.getLevel()) && capability.getName().equals(tmpCapability.getName())) return true;
        }
        return false;
    }

    public String getCapabilityFullPath(Capability capability) {
        StringBuilder buffer = new StringBuilder();
        String sep = "";
        Capability tmCapability = capability;
        while (tmCapability != null) {
            if (tmCapability == tmCapability.getParent()) {
                throw new IllegalStateException("Capability hah itself for parent");
            }
            buffer.insert(0, sep).insert(0, tmCapability.getName());
            if (tmCapability.getParent() == null) {
                Assert.isTrue(tmCapability.getName().equals("ROOT"), "Cannot compute full path if parents are not pessent until ROOT");
            }
            tmCapability = tmCapability.getParent();
            sep = " > ";
        }
        return buffer.toString();
    }        

    public String getCapabilityFullPath(CapabilityImportDTO importDTO) {
        StringBuilder buffer = new StringBuilder();
        String sep = "";
        for (Capability capability : importDTO.getCapabilityList()) {
            buffer.append(sep).append(capability != null ? capability.getName() : " --- ");
            sep = " > ";            
        }
        return buffer.toString();
    } 

    public Map<String,Capability>  initCapabilitiesByNameFromDB() {
        Map<String,Capability> capabilitiesByFllPath = new HashMap<>();
        List<Capability> allCapabilities = capabilityRepository.findAllWithSubCapabilities();
        for (Capability capability : allCapabilities) {
            capabilitiesByFllPath.put(getCapabilityFullPath(capability), capability);
        }  
        return capabilitiesByFllPath;      
    }    

    public CapabilityImportDTO getCapabilityImportDTO(String fullPath) {
        Capability domain = null, l0Import = null, l1Import = null, l2Import = null, l3Import = null;
        String[] capabilitiesName = fullPath.split(" > ");
        Capability root = new Capability("ROOT", -2);
        if (capabilitiesName.length > 0) domain   = new Capability(capabilitiesName[0], -1);
        if (capabilitiesName.length > 1) l0Import = new Capability(capabilitiesName[1], 0);
        if (capabilitiesName.length > 2) l1Import = new Capability(capabilitiesName[2], 1);
        if (capabilitiesName.length > 3) l2Import = new Capability(capabilitiesName[3], 2);
        if (capabilitiesName.length > 4) l3Import = new Capability(capabilitiesName[4], 3);
        CapabilityImportDTO capabilityImportDTO = new CapabilityImportDTO(root, domain, l0Import, l1Import, l2Import, l3Import);
        return capabilityImportDTO;
    }

    public CapabilityImportDTO buildImportDTO(Capability capability, String error, ImportStatus status) {
        Capability[] capabilities = {null, null, null, null, null, null};
        capabilities[0] = new Capability("ROOT",-2);
        Capability tmp = capability;
        for (int level = capability.getLevel(); level >= 0 ; level--) {
            capabilities[level+2] = tmp;
            tmp = capability.getParent();            
        }        

        CapabilityImportDTO capabilityImportDTO = new CapabilityImportDTO(
            capabilities[0], capabilities[1], capabilities[2], capabilities[3], capabilities[4], capabilities[5]
        );
        capabilityImportDTO.setError(error);
        capabilityImportDTO.setStatus(status);
        return capabilityImportDTO;
    }  

}
