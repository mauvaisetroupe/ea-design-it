package com.mauvaisetroupe.eadesignit.service.importfile.util;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.mapper.CapabilityMapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapabilityUtil {

    private final Logger log = LoggerFactory.getLogger(CapabilityUtil.class);

    public CapabilityDTO mapArrayToCapability(Map<String, Object> map, String nameColumn, String descriptionColumn, Integer level) {
        CapabilityDTO capability = null;
        try {
            Object cellValue = map.get(nameColumn);
            if (cellValue != null && cellValue.toString().trim().length() > 2) {
                capability = new CapabilityDTO();
                String name = map.get(nameColumn).toString();
                capability.setName(name);
                capability.setDescription((String) map.get(descriptionColumn));
                capability.setLevel(level);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return capability;
    }

    public CapabilityImportDTO mappArrayToCapabilityImport(
        CapabilityDTO l0Import,
        CapabilityDTO l1Import,
        CapabilityDTO l2Import,
        CapabilityDTO l3Import
    ) {
        CapabilityImportDTO capabilityImportDTO = new CapabilityImportDTO();
        capabilityImportDTO.setL0(l0Import);
        capabilityImportDTO.setL1(l1Import);
        capabilityImportDTO.setL2(l2Import);
        capabilityImportDTO.setL3(l3Import);
        return capabilityImportDTO;
    }

    public Set<CapabilityDTO> getRoot(Collection<Capability> inputs) {
        // Merge all capabilities finding common parents
        // Return merged capabilities by root (and not by leaf), inverse manyToOne relationship

        Set<CapabilityDTO> listOfRoots = new HashSet<>();
        CapabilityMapper mapper = new CapabilityMapper();
        Map<Long, CapabilityDTO> capabilityById = new HashMap<>();

        for (Capability applicationCapability : inputs) {
            boolean found = false;
            Capability tmpCapability = applicationCapability;
            CapabilityDTO childDTO = null;
            CapabilityDTO dto = null;
            while (tmpCapability != null && !found) {
                if (capabilityById.containsKey(tmpCapability.getId())) {
                    found = true;
                    dto = capabilityById.get(tmpCapability.getId());
                } else {
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
        return listOfRoots;
    }
}
