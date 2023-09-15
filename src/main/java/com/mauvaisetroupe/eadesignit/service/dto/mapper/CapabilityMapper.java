package com.mauvaisetroupe.eadesignit.service.dto.mapper;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.service.dto.CapabilityDTO;

public class CapabilityMapper {

    public CapabilityDTO clone(Capability capability) {
        CapabilityDTO capabilityDTO = new CapabilityDTO();
        capabilityDTO.setId(capability.getId());
        capabilityDTO.setName(capability.getName());
        capabilityDTO.setDescription(capability.getDescription());
        capabilityDTO.setComment(capability.getComment());
        capabilityDTO.setLevel(capability.getLevel());
        return capabilityDTO;
    }
}
