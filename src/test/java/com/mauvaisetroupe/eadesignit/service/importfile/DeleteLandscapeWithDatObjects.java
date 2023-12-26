package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.CapabilityApplicationMappingRepository;
import com.mauvaisetroupe.eadesignit.repository.DataObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DeleteLandscapeWithDatObjects {

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
    void testDeleteLandscapeWithDataObject() throws EncryptedDocumentException, IOException {
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        // Create Landscape, load Capabilities
        mockservice.createLandscape();
        mockservice.createDataObjects();

        mockservice.checkNBDataObjects("Invest Landscape", 6);
        checkNbDataObjects(11);

        LandscapeView flw01 = landscapeViewRepository.findByDiagramNameIgnoreCase("Invest Landscape");
        mockservice.deleteLandscape(flw01.getId());

        flw01 = landscapeViewRepository.findByDiagramNameIgnoreCase("Invest Landscape");
        assertNull(flw01);
        //DataObjects not deleted when deleting Landscape due to parent relationship complexity
        checkNbDataObjects(11);

        mockservice.createLandscape();
        mockservice.createDataObjects();

        checkNbDataObjects(11);
    }

    protected void checkNbDataObjects(int expected) {
        assertEquals(expected, dataObjectRepository.findAll().size());
    }
}
