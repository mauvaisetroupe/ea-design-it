package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
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

    public List<CapabilityImportDTO> importExcel(InputStream excel, String originalFilename)
        throws EncryptedDocumentException, IOException {
        ExcelReader capabilityFlowExcelReader = new ExcelReader(excel);

        List<Map<String, Object>> capabilitiesDF = capabilityFlowExcelReader.getSheet(CAPABILITY_SHEET_NAME);

        List<CapabilityImportDTO> result = new ArrayList<CapabilityImportDTO>();
        for (Map<String, Object> map : capabilitiesDF) {
            CapabilityImportDTO capabilityImportDTO = new CapabilityImportDTO();
            Capability l0 = mapArrayToCapability(map, L0_NAME, L0_DESCRIPTION, 0);
            Capability l1 = mapArrayToCapability(map, L1_NAME, L1_DESCRIPTION, 1);
            Capability l2 = mapArrayToCapability(map, L2_NAME, L2_DESCRIPTION, 2);
            Capability l3 = mapArrayToCapability(map, L3_NAME, L3_DESCRIPTION, 3);
            capabilityImportDTO.setL0(l0);
            capabilityImportDTO.setL1(l1);
            capabilityImportDTO.setL2(l2);
            capabilityImportDTO.setL3(l3);
            if (l0 != null) {
                result.add(capabilityImportDTO);

                if (l1 != null) {
                    l0.addSubCapabilities(l1);
                    //                    capabilityRepository.save(l1);

                    if (l2 != null) {
                        l1.addSubCapabilities(l2);
                        //                        capabilityRepository.save(l2);

                        if (l3 != null) {
                            l2.addSubCapabilities(l3);
                            //                            capabilityRepository.save(l3);
                        }
                    }
                }
            }
        }
        return result;
    }

    private Capability mapArrayToCapability(Map<String, Object> map, String nameColumn, String descriptionColumn, Integer level) {
        Capability capability = null;
        try {
            String name = map.get(nameColumn).toString();
            capability = this.capabilityRepository.findByNameIgnoreCaseAndLevel(name, level);
            if (capability == null) {
                log.debug("Capabilty to be created : " + capability);
                capability = new Capability();
                capability.setName(name);
                capability.setDescription((String) map.get(descriptionColumn));
                capability.setLevel(level);
                capabilityRepository.save(capability);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return capability;
    }
}
