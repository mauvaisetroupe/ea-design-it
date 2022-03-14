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
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    void testImport1() throws EncryptedDocumentException, IOException {
        String filemame = "02-import-flows.xlsx";

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");
        InputStream file2 = this.getClass().getResourceAsStream("/junit/" + filemame);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 1);
        checkNbsteps("CYP.01", 4);
    }

    @Test
    void testImport2() throws EncryptedDocumentException, IOException {
        String filemame = "02-import-flows-02.xlsx";

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");
        InputStream file2 = this.getClass().getResourceAsStream("/junit/" + filemame);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 4);
    }

    @Test
    void shouldImportExternal1() throws EncryptedDocumentException, IOException {
        String filename = "02-import-flows.xlsx";

        // creat CYP.02 marked as external... should now be added during import
        FunctionalFlow cyp02 = new FunctionalFlow();
        cyp02.setAlias("CYP.02");
        functionalFlowRepository.save(cyp02);

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");
        InputStream file2 = this.getClass().getResourceAsStream("/junit/" + filename);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 2);
        checkNbsteps("CYP.01", 4);
        checkNbsteps("CYP.02", 0);
    }

    @Test
    void shouldImportExternal2() throws EncryptedDocumentException, IOException {
        String filename = "02-import-flows-02.xlsx";

        // creat CYP.02 marked as external... should now be added during import
        FunctionalFlow cyp02 = new FunctionalFlow();
        cyp02.setAlias("CYP.02");
        functionalFlowRepository.save(cyp02);

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");
        InputStream file2 = this.getClass().getResourceAsStream("/junit/" + filename);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 5);
        checkNbsteps("CYP.02", 0);
    }
}
