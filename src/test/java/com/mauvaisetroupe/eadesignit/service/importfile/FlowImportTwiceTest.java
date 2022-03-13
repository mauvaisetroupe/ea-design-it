package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;

public class FlowImportTwiceTest extends ImportFlowTest {

    @Test
    void testImportTwice() throws EncryptedDocumentException, IOException {
        // Id flow	Alias flow	External	Source Element	Target Element
        // TRAD.001	CYP.01		APPLICATION-0001	APPLICATION-0002
        // TRAD.002	CYP.01		APPLICATION-0002	APPLICATION-0003
        // TRAD.003	CYP.01		APPLICATION-0003	APPLICATION-0004
        // TRAD.004	CYP.01		APPLICATION-0004	APPLICATION-0003
        // EXT.001	CYP.02	yes	APPLICATION-0004	APPLICATION-0003

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(0);

        InputStream file2 = this.getClass().getResourceAsStream("/junit/02-import-flows.xlsx");
        assertNotNull(file2);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 1);
        checkNbsteps("CYP.01", 4);

        file2 = this.getClass().getResourceAsStream("/junit/02-import-flows.xlsx");
        assertNotNull(file2);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 1);
        checkNbsteps("CYP.01", 4);
    }
}
