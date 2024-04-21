package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ErrorLineException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;

public class FlowImportTwiceTest extends ImportFlowTest {

    @Test
    void testImportTwice1() throws EncryptedDocumentException, IOException, ErrorLineException {
        String filename = "02-import-flows.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(0);

        InputStream file2 = this.getClass().getResourceAsStream("/junit/" + filename);
        assertNotNull(file2);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 1);
        checkNbFlows(1);
        checkNbsteps("CYP.01", 4);

        file2 = this.getClass().getResourceAsStream("/junit/" + filename);
        assertNotNull(file2);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 1);
        checkNbFlows(1);
        checkNbsteps("CYP.01", 4);
    }

    @Test
    void testImportTwice2() throws EncryptedDocumentException, IOException, ErrorLineException {
        String filename = "02-import-flows-02.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(0);

        InputStream file2 = this.getClass().getResourceAsStream("/junit/" + filename);
        assertNotNull(file2);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        landscapes = checkNbLandscapes(1);
        checkNbFlows(4);
        checkNbFlows(landscapes.get(0), 4);

        file2 = this.getClass().getResourceAsStream("/junit/" + filename);
        assertNotNull(file2);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 4);
        checkNbFlows(4);
    }
}
