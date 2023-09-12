package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.util.CapabilityUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CapabilityImportService {

    public static final String CAPABILITY_SHEET_NAME = "Capabilities";

    private final Logger log = LoggerFactory.getLogger(CapabilityImportService.class);

    @Autowired
    private CapabilityRepository capabilityRepository;

    public static final String L0_NAME = "Capability L0";
    public static final String L0_DESCRIPTION = "L0 - Description";
    public static final String L1_NAME = "Capability L1";
    public static final String L1_DESCRIPTION = "L1 - Description";
    public static final String L2_NAME = "Capability L2";
    public static final String L2_DESCRIPTION = "L2 - Description";
    public static final String L3_NAME = "Capability L3";
    public static final String L3_DESCRIPTION = "L3 - Description";
    public static final String SUR_DOMAIN = "Sur-domaine";
    public static final String FULL_PATH = "full.path";

    public List<CapabilityImportDTO> importExcel(InputStream excel, String originalFilename)
        throws EncryptedDocumentException, IOException {
        capabilityRepository.deleteByCapabilityApplicationMappingsIsEmpty();

        ExcelReader capabilityFlowExcelReader = new ExcelReader(excel);

        List<Map<String, Object>> capabilitiesDF = capabilityFlowExcelReader.getSheet(CAPABILITY_SHEET_NAME);

        List<CapabilityImportDTO> result = new ArrayList<CapabilityImportDTO>();
        CapabilityUtil capabilityUtil = new CapabilityUtil();
        CapabilityDTO rootCapabilityDTO = new CapabilityDTO();
        rootCapabilityDTO.setName("ROOT");
        rootCapabilityDTO.setLevel(-2);
        Capability rootCapability = findOrCreateCapability(rootCapabilityDTO, null);
        capabilityRepository.save(rootCapability);

        for (Map<String, Object> map : capabilitiesDF) {
            CapabilityImportDTO capabilityImportDTO = new CapabilityImportDTO();
            // new capability created from excel, without parent assigned
            CapabilityDTO l0Import = capabilityUtil.mapArrayToCapability(map, L0_NAME, L0_DESCRIPTION, 0);
            CapabilityDTO l1Import = capabilityUtil.mapArrayToCapability(map, L1_NAME, L1_DESCRIPTION, 1);
            CapabilityDTO l2Import = capabilityUtil.mapArrayToCapability(map, L2_NAME, L2_DESCRIPTION, 2);
            CapabilityDTO l3Import = capabilityUtil.mapArrayToCapability(map, L3_NAME, L3_DESCRIPTION, 3);
            capabilityImportDTO = capabilityUtil.mappArrayToCapabilityImport(l0Import, l1Import, l2Import, l3Import);
            capabilityImportDTO.setDomain((String) map.get(SUR_DOMAIN));

            boolean lineIsValid = checkLineIsValid(capabilityImportDTO);

            if (lineIsValid) {
                try {
                    // Find L0 without parent (sur-domaine) to find goo L0 even if Sur-domaine not completed correctly
                    // Assumption : one L0 has a unique name
                    Capability l0 = findOrCreateCapability(l0Import, null);
                    CapabilityDTO surdomainDTO = new CapabilityDTO();
                    surdomainDTO.setName(capabilityImportDTO.getDomain() != null ? capabilityImportDTO.getDomain() : "UNKNOWN");
                    surdomainDTO.setLevel(-1);
                    Capability surDomainCapability = findOrCreateCapability(surdomainDTO, rootCapabilityDTO);
                    if (surDomainCapability.getParent() == null) {
                        rootCapability.addSubCapabilities(surDomainCapability);
                    }
                    if (l0.getParent() == null) {
                        // If L0 is created, then we need to assign a surDomain as parent
                        surDomainCapability.addSubCapabilities(l0);
                    }
                    capabilityRepository.save(rootCapability);
                    capabilityRepository.save(surDomainCapability);
                    capabilityRepository.save(l0);

                    // at least one capability not null
                    Capability parent = l0;
                    CapabilityDTO parentDTO = l0Import;
                    for (CapabilityDTO capabilityDTO : Arrays.asList(new CapabilityDTO[] { l1Import, l2Import, l3Import })) {
                        if (capabilityDTO != null) {
                            capabilityRepository.save(parent);
                            Capability capability = findOrCreateCapability(capabilityDTO, parentDTO);
                            parent.addSubCapabilities(capability);
                            capabilityRepository.save(capability);
                            capabilityRepository.save(parent);
                            parent = capability;
                            parentDTO = capabilityDTO;
                        }
                    }

                    capabilityImportDTO.setStatus(ImportStatus.NEW);
                } catch (Exception e) {
                    capabilityImportDTO.setStatus(ImportStatus.ERROR);
                    capabilityImportDTO.setError(e.toString());
                    e.printStackTrace();
                }
            } else {
                capabilityImportDTO.setStatus(ImportStatus.ERROR);
            }
            result.add(capabilityImportDTO);
        }
        return result;
    }

    private boolean checkLineIsValid(CapabilityImportDTO capabilityImportDTO) {
        // If a level exist, inferior level should exist
        if (capabilityImportDTO.getL0() == null) {
            capabilityImportDTO.setError("L0 should not be null");
            return false;
        } else if (capabilityImportDTO.getL3() != null) {
            if (capabilityImportDTO.getL2() == null || capabilityImportDTO.getL1() == null) {
                capabilityImportDTO.setError("L3 is not null, both L0, L1 & L2 should not be null");
                return false;
            }
        } else if (capabilityImportDTO.getL2() != null) {
            if (capabilityImportDTO.getL1() == null) {
                capabilityImportDTO.setError("L2 is not null, both L0 and L1 should not be null");
                return false;
            }
        }
        return true;
    }

    private Capability findOrCreateCapability(CapabilityDTO capabilityImport, CapabilityDTO parentImport) {
        if (capabilityImport == null) return null;
        List<Capability> potentials = new ArrayList<>();
        if (parentImport == null) {
            potentials = this.capabilityRepository.findByNameIgnoreCaseAndLevel(capabilityImport.getName(), capabilityImport.getLevel());
        } else {
            potentials =
                this.capabilityRepository.findByNameIgnoreCaseAndParentNameIgnoreCaseAndLevel(
                        capabilityImport.getName(),
                        parentImport.getName(),
                        capabilityImport.getLevel()
                    );
        }
        if (potentials.size() == 0) {
            Capability capability = createCapability(capabilityImport);
            return capability;
        }
        if (potentials.size() == 1) {
            return potentials.get(0);
        }
        throw new IllegalStateException("Could not find a unique Capability");
    }

    private Capability createCapability(CapabilityDTO capabilityDTO) {
        Capability capability = new Capability();
        capability.setName(capabilityDTO.getName());
        capability.setDescription(capabilityDTO.getDescription());
        capability.setLevel(capabilityDTO.getLevel());
        log.debug("Capabilty to be created : " + capability);
        return capability;
    }
}
