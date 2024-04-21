package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.CapabilityApplicationMappingRepository;
import com.mauvaisetroupe.eadesignit.repository.DataObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ErrorLineException;
import java.io.IOException;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DeleteLandscapeWithCapa {

    @Autowired
    Mockservice mockservice;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    CapabilityApplicationMappingRepository capabilityApplicationMappingRepository;

    @Autowired
    DataObjectRepository dataObjectRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void testDeleteLandscapeWithCapabilities() throws EncryptedDocumentException, IOException, ErrorLineException {
        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        // Create Landscape, load Capabilities
        mockservice.createLandscape();
        mockservice.createCapailitiesAndMapping("CPB.01");

        checkNbCapabilityMappings(9);

        LandscapeView flw01 = landscapeViewRepository.findByDiagramNameIgnoreCase("Invest Landscape");
        mockservice.deleteLandscape(flw01.getId());

        checkNbCapabilityMappings(0);

        flw01 = landscapeViewRepository.findByDiagramNameIgnoreCase("Invest Landscape");
        assertNull(flw01);

        mockservice.createLandscape();
        mockservice.createCapailitiesAndMapping("CPB.01");
        mockservice.createCapailitiesAndMapping("CPB.02");
        checkNbCapabilityMappings(10);

        flw01 = landscapeViewRepository.findByDiagramNameIgnoreCase("Invest Landscape");
        mockservice.deleteLandscape(flw01.getId());

        checkNbCapabilityMappings(2);
    }

    protected void checkLandscapeMapping(String landscapeName, int expectedCapabilityMappingNb) {
        LandscapeView flw01 = landscapeViewRepository.findByDiagramNameIgnoreCase(landscapeName);
        landscapeViewRepository.save(flw01);
        assertEquals(expectedCapabilityMappingNb, flw01.getCapabilityApplicationMappings().size());
    }

    protected List<CapabilityApplicationMapping> checkNbCapabilityMappings(int nbMappings) {
        List<CapabilityApplicationMapping> applicationMappings = capabilityApplicationMappingRepository.findAll();
        assertEquals(nbMappings, applicationMappings.size());
        return applicationMappings;
    }
}
