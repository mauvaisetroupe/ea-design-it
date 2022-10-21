package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

public class FlowImportSubComponentTest extends ImportFlowTest {

    @Autowired
    FlowImportService flowImportService;

    @Autowired
    ApplicationImportService applicationImportService;

    @Autowired
    ComponentImportService componentImportService;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    @Autowired
    FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Test
    void testImport2() throws EncryptedDocumentException, IOException {
        String filemame = "03-import-flows.xlsx";

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");
        file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        componentImportService.importExcel(file1, "my-applications.xlsx");

        InputStream file2 = this.getClass().getResourceAsStream("/junit/" + filemame);
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 4);

        assertEquals(
            "COMPONENT-0001-0001",
            landscapes.get(0).getFlows().first().getSteps().iterator().next().getFlowInterface().getSourceComponent().getName()
        );
    }
}
