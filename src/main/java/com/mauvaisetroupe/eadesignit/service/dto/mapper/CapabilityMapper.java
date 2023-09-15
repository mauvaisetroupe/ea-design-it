package com.mauvaisetroupe.eadesignit.service.dto.mapper;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.service.dto.CapabilityDTO;

public class CapabilityMapper {

    public CapabilityDTO clone(Capability capability) {
        CapabilityDTO capabilityDTO = new CapabilityDTO(
            capability.getId(),
            capability.getName(),
            capability.getDescription(),
            capability.getComment(),
            capability.getLevel()
        );
        return capabilityDTO;
    }
}
