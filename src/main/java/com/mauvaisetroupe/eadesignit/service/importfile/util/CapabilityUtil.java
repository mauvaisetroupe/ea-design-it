package com.mauvaisetroupe.eadesignit.service.importfile.util;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.mapper.CapabilityMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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

    public List<CapabilityDTO> getRoot(Collection<Capability> inputs) {
        // Merge all capabilities finding common parents
        // Return merged capabilities by root (and not by leaf), inverse manyToOne relationship

        List<CapabilityDTO> listOfLeaves = new ArrayList<>();
        CapabilityMapper mapper = new CapabilityMapper();

        for (Capability applicationCapability : inputs) {
            boolean found = false;
            for (CapabilityDTO capabilityInResult : listOfLeaves) {
                while (capabilityInResult.getLevel() >= applicationCapability.getLevel()) {
                    capabilityInResult = capabilityInResult.getParent();
                }
                Assert.isTrue(capabilityInResult.getLevel() == applicationCapability.getLevel() - 1, "compare prarent");
                if (capabilityInResult.getId() == applicationCapability.getParent().getId()) {
                    CapabilityDTO capabilityDTO = mapper.clone(applicationCapability);
                    capabilityInResult.addSubCapabilities(capabilityDTO);
                    log.debug("Addind " + capabilityDTO + " to parent " + capabilityInResult);
                    found = true;
                }
            }
            if (!found) {
                // deeply clone capabilities to DTO fome leaf to root

                Capability tmpCapability = applicationCapability;
                CapabilityDTO tmpDTO = mapper.clone(applicationCapability);
                CapabilityDTO leafDTO = tmpDTO;
                while (tmpCapability.getParent() != null) {
                    tmpCapability = tmpCapability.getParent();
                    CapabilityDTO parentDTO = mapper.clone(tmpCapability);
                    parentDTO.addSubCapabilities(tmpDTO);
                    tmpDTO = parentDTO;
                }
                log.debug("Adding " + leafDTO);
                listOfLeaves.add(leafDTO);
            }
        }

        // browse lead and put root as entry
        List<CapabilityDTO> rootDTO = new ArrayList<>();
        for (CapabilityDTO capabilityDTO : listOfLeaves) {
            CapabilityDTO root = capabilityDTO;
            while (root.getParent() != null) {
                root = root.getParent();
            }
            rootDTO.add(root);
        }
        return rootDTO;
    }
}
