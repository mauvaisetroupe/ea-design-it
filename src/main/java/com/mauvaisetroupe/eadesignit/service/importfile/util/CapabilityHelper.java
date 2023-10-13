package com.mauvaisetroupe.eadesignit.service.importfile.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;

@Component
public class CapabilityHelper {

    @Autowired
    CapabilityRepository capabilityRepository;
    
    public String getCapabilityFullPath(Capability capability) {
        StringBuilder buffer = new StringBuilder();
        String sep = "";
        while (capability.getParent() != null) {
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
