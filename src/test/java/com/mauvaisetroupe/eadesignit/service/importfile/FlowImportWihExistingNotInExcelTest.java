package com.mauvaisetroupe.eadesignit.service.importfile;

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
public class FlowImportWihExistingNotInExcelTest extends ImportFlowTest {

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
    void testShouldRemoveFlowIfNotInExecl() throws EncryptedDocumentException, IOException {
        // Id flow	Alias flow	External	Source Element	Target Element
        // TRAD.001	CYP.01		APPLICATION-0001	APPLICATION-0002
        // TRAD.002	CYP.01		APPLICATION-0002	APPLICATION-0003
        // TRAD.003	CYP.01		APPLICATION-0003	APPLICATION-0004
        // TRAD.004	CYP.01		APPLICATION-0004	APPLICATION-0003
        // EXT.001	CYP.02	yes	APPLICATION-0004	APPLICATION-0003

        InputStream file1 = this.getClass().getResourceAsStream("/junit/01-import-applications.xlsx");
        applicationImportService.importExcel(file1, "my-applications.xlsx");

        InputStream file2 = this.getClass().getResourceAsStream("/junit/02-import-flows.xlsx");
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        List<LandscapeView> landscapes = checkNbLandscapes(1);
        LandscapeView landscapeView = landscapes.get(0);
        checkNbFlows(landscapeView, 1);
        checkNbFlows(1);
        checkNbsteps("CYP.01", 4);

        // create nimp and add to landscape, should be removed after import
        FunctionalFlow nimp = new FunctionalFlow();
        nimp.setAlias("NIMP.001");
        functionalFlowRepository.save(nimp);
        landscapeView.addFlows(nimp);
        landscapeViewRepository.save(landscapeView);

        checkNbFlows(landscapeView, 2);

        file2 = this.getClass().getResourceAsStream("/junit/02-import-flows.xlsx");
        flowImportService.importExcel(file2, "my-landscape.xlsx");

        landscapes = checkNbLandscapes(1);
        landscapeView = landscapes.get(0);
        checkNbFlows(2);
        checkNbFlows(landscapeView, 1);
        checkNbsteps("CYP.01", 4);
    }
}
