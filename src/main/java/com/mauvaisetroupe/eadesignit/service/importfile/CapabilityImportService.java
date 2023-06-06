package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.util.CapabilityUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CapabilityImportService {

    private static final String CAPABILITY_SHEET_NAME = "Capabilities";

    private final Logger log = LoggerFactory.getLogger(CapabilityImportService.class);

    @Autowired
    private CapabilityRepository capabilityRepository;

    private static final String L0_NAME = "Capability L0";
    private static final String L0_DESCRIPTION = "L0 - Description";
    private static final String L1_NAME = "Capability L1";
    private static final String L1_DESCRIPTION = "L1 - Description";
    private static final String L2_NAME = "Capability L2";
    private static final String L2_DESCRIPTION = "L2 - Description";
    private static final String L3_NAME = "Capability L3";
    private static final String L3_DESCRIPTION = "L3 - Description";
    private static final String SUR_DOMAIN = "Sur-domaine";

    public List<CapabilityImportDTO> importExcel(InputStream excel, String originalFilename)
        throws EncryptedDocumentException, IOException {
        ExcelReader capabilityFlowExcelReader = new ExcelReader(excel);

        List<Map<String, Object>> capabilitiesDF = capabilityFlowExcelReader.getSheet(CAPABILITY_SHEET_NAME);

        List<CapabilityImportDTO> result = new ArrayList<CapabilityImportDTO>();
        CapabilityUtil capabilityUtil = new CapabilityUtil();
        CapabilityDTO rootCapabilityDTO = new CapabilityDTO();
        rootCapabilityDTO.setName("ROOT");
        rootCapabilityDTO.setLevel(-2);
        Capability rootCapability = findOrCreateCapability(rootCapabilityDTO, null);

        for (Map<String, Object> map : capabilitiesDF) {
            CapabilityImportDTO capabilityImportDTO = new CapabilityImportDTO();
            // new capability created from excel, without parent assigned
            CapabilityDTO l0Import = capabilityUtil.mapArrayToCapability(map, L0_NAME, L0_DESCRIPTION, 0, SUR_DOMAIN);
            CapabilityDTO l1Import = capabilityUtil.mapArrayToCapability(map, L1_NAME, L1_DESCRIPTION, 1, SUR_DOMAIN);
            CapabilityDTO l2Import = capabilityUtil.mapArrayToCapability(map, L2_NAME, L2_DESCRIPTION, 2, SUR_DOMAIN);
            CapabilityDTO l3Import = capabilityUtil.mapArrayToCapability(map, L3_NAME, L3_DESCRIPTION, 3, SUR_DOMAIN);
            capabilityImportDTO = capabilityUtil.mappArrayToCapabilityImport(l0Import, l1Import, l2Import, l3Import);

            CapabilityDTO surdomainDTO = new CapabilityDTO();
            if (l0Import != null) {
                surdomainDTO.setName(l0Import.getSurDomain());
            } else if (l1Import != null) {
                surdomainDTO.setName(l1Import.getSurDomain());
            } else if (l2Import != null) {
                surdomainDTO.setName(l2Import.getSurDomain());
            } else if (l3Import != null) {
                surdomainDTO.setName(l3Import.getSurDomain());
            } else {
                surdomainDTO.setName("UNKNOWN");
            }
            surdomainDTO.setLevel(-1);

            Capability surdomain = findOrCreateCapability(surdomainDTO, rootCapabilityDTO);
            Capability l0 = findOrCreateCapability(l0Import, surdomainDTO);

            if (l0 != null) {
                if (l0.getId() == null) {
                    surdomain.addSubCapabilities(l0);
                }
                rootCapability.addSubCapabilities(surdomain);
                surdomain.addSubCapabilities(l0);
                // at least one capability not null
                result.add(capabilityImportDTO);
                if (l1Import != null) {
                    Capability l1 = findOrCreateCapability(l1Import, l0Import);
                    l0.addSubCapabilities(l1);
                    if (l2Import != null) {
                        Capability l2 = findOrCreateCapability(l2Import, l1Import);
                        l1.addSubCapabilities(l2);
                        if (l3Import != null) {
                            Capability l3 = findOrCreateCapability(l3Import, l2Import);
                            l2.addSubCapabilities(l3);
                        }
                    }
                }
            }
            //save is automatically done by @Transactional
        }
        return result;
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
            capabilityRepository.save(capability);
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
