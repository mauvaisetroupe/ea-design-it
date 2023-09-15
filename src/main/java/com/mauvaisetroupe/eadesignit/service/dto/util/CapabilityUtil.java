package com.mauvaisetroupe.eadesignit.service.dto.util;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.service.dto.CapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.dto.mapper.CapabilityMapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CapabilityUtil {

    public Set<CapabilityDTO> getRoot(Collection<Capability> inputs) {
        // Merge all capabilities finding common parents
        // Return merged capabilities by root (and not by leaf), inverse manyToOne relationship

        Set<CapabilityDTO> listOfRoots = new HashSet<>();
        CapabilityMapper mapper = new CapabilityMapper();
        Map<Long, CapabilityDTO> capabilityById = new HashMap<>();

        for (Capability applicationCapability : inputs) {
            applicationCapability.getApplications();
            boolean found = false;
            Capability tmpCapability = applicationCapability;
            CapabilityDTO childDTO = null;
            CapabilityDTO dto = null;
            while (tmpCapability != null && !found) {
                if (capabilityById.containsKey(tmpCapability.getId())) {
                    found = true;
                    dto = capabilityById.get(tmpCapability.getId());
                    dto.setCapabilityApplicationMappings(tmpCapability.getCapabilityApplicationMappings());
                } else {
                    dto = mapper.clone(tmpCapability);
                    capabilityById.put(dto.getId(), dto);
                    dto.setCapabilityApplicationMappings(tmpCapability.getCapabilityApplicationMappings());
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
        return listOfRoots;
    }
}
