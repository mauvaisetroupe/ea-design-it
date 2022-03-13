package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public class FlowImportOneTest extends ImportFlowTest {

    @Autowired
    FlowImportService flowImportService;

    @Autowired
    ApplicationImportService applicationImportService;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    @Autowired
    FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Test
    void testNullable() throws EncryptedDocumentException, IOException {
        ExcelReader flowImportService = new ExcelReader(null);
        assertTrue(flowImportService.isNull("?"), "should be null");
        assertTrue(flowImportService.isNull("??"), "should be null");
        assertTrue(flowImportService.isNull("???"), "should be null");
    }

    @Test
    void testImport() throws EncryptedDocumentException, IOException {
        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        assertNotNull(file1);
        applicationImportService.importExcel(file1, "my-applications.xlsx");

        assertEquals(4, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        InputStream file2 = this.getClass().getResourceAsStream("/junit/02-import-flows.xlsx");
        assertNotNull(file2);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 1);
        checkNbsteps("CYP.01", 4);
    }

    @Test
    void shouldImportExternal() throws EncryptedDocumentException, IOException {
        // Id flow	Alias flow	External	Source Element	Target Element
        // TRAD.001	CYP.01		APPLICATION-0001	APPLICATION-0002
        // TRAD.002	CYP.01		APPLICATION-0002	APPLICATION-0003
        // TRAD.003	CYP.01		APPLICATION-0003	APPLICATION-0004
        // TRAD.004	CYP.01		APPLICATION-0004	APPLICATION-0003
        // EXT.001	CYP.02	yes	APPLICATION-0004	APPLICATION-0003

        // creat CYP.02 marked as external... should now be added during import
        FunctionalFlow cyp02 = new FunctionalFlow();
        cyp02.setAlias("CYP.02");
        functionalFlowRepository.save(cyp02);

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");
        InputStream file2 = this.getClass().getResourceAsStream("/junit/02-import-flows.xlsx");
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 2);
        checkNbsteps("CYP.01", 4);
        checkNbsteps("CYP.02", 0);
    }
}
