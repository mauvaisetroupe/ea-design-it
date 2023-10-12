package com.mauvaisetroupe.eadesignit.service.dto.util;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.service.dto.mapper.CapabilityMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

public class CapabilityUtil {

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
}
