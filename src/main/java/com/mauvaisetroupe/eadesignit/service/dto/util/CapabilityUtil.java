package com.mauvaisetroupe.eadesignit.service.dto.util;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.dto.mapper.CapabilityMapper;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CapabilityUtil {
    
    @Autowired
    CapabilityRepository capabilityRepository;

    public Capability buildCapabilityTree(Collection<Capability> inputs) {
        // Merge all capabilities finding common parents
        // Return merged capabilities by root (and not by leaf), inverse manyToOne relationship

        if (inputs == null || inputs.isEmpty()) return null;
        
        Set<Capability> listOfRoots = new HashSet<>();
        CapabilityMapper mapper = new CapabilityMapper();
        Map<Long, Capability> capabilityById = new HashMap<>();

        for (Capability capability : inputs) {
            capability.getApplications();
            boolean found = false;
            Capability tmpCapability = capability;
            Capability childDTO = null;
            Capability dto = null;
            while (tmpCapability != null && !found) {
                if (capabilityById.containsKey(tmpCapability.getId())) {
                    found = true;
                    dto = capabilityById.get(tmpCapability.getId());
                } else {
                    // Clone capability, mapping and application to avoid hibernate side effects
                    dto = mapper.clone(tmpCapability);
                    capabilityById.put(dto.getId(), dto);
                }
                if (childDTO != null) {
                    dto.addSubCapabilities(childDTO);
                }
                childDTO = dto;
                tmpCapability = tmpCapability.getParent();
            }
            if (dto != null && dto.getParent() == null) {
                listOfRoots.add(dto);
            }
        }
        Assert.isTrue(listOfRoots.size() == 1, "We should have a unique ROOT element");
        return listOfRoots.iterator().next();
    }

    public String getCapabilityFullPath(Capability capability) {
        StringBuilder buffer = new StringBuilder();
        String sep = "";
        while (capability.getParent() != null) {
            if (capability == capability.getParent()) {
                throw new IllegalStateException("Capability hah itself for parent");
            }
            buffer.insert(0, sep).insert(0, capability.getName());
            capability = capability.getParent();
            sep = " > ";
        }
        return buffer.toString();
    }        

    public Map<String,Capability>  initCapabilitiesByNameFromDB() {
        Map<String,Capability> capabilitiesByFllPath = new HashMap<>();
        Set<Capability> allCapabilities = capabilityRepository.findAllWithSubCapabilities();
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
        
}
