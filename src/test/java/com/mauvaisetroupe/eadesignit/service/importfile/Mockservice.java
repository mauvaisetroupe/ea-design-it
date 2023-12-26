package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.LandscapeViewService;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportAnalysisDTO;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Mockservice {

    @Autowired
    ApplicationImportService applicationImportService;

    @Autowired
    FlowImportService flowImportService;

    @Autowired
    CapabilityImportService capabilityImportService;

    @Autowired
    ApplicationCapabilityImportService applicationCapabilityImportService;

    @Autowired
    DataObjectImportService dataObjectImportService;

    @Autowired
    LandscapeViewService landscapeViewService;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    String filename = "/junit/05-import-multi-flows-with-capas-and-dataobjects.xlsx";

    public void createLandscape() throws EncryptedDocumentException, IOException {
        /////////////////////
        // Import APPLICATION
        /////////////////////
        InputStream file = this.getClass().getResourceAsStream(filename);
        applicationImportService.importExcel(file, "WE.DONT.CARE.NOT.USED");

        ///////////////////////////
        // Import FLW.01, ... FLW.04
        ///////////////////////////

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.01");
        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.02");
        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.03");
        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.04");
    }

    public void createCapailitiesAndMapping(String sheet) throws EncryptedDocumentException, IOException {
        InputStream file = this.getClass().getResourceAsStream(filename);
        CapabilityImportAnalysisDTO analysisDTO = capabilityImportService.analyzeExcel(file);
        capabilityImportService.confirmImport(analysisDTO);

        file = this.getClass().getResourceAsStream(filename);
        applicationCapabilityImportService.importExcel(file, sheet);
    }

    public void createDataObject() throws IOException {
        InputStream file = this.getClass().getResourceAsStream(filename);
        dataObjectImportService.importExcel(file);
    }

    public void deleteLandscape(Long id) {
        landscapeViewService.delete(id, true, true, true, true, false);
    }

    public void createDataObjects() throws IOException {
        InputStream file = this.getClass().getResourceAsStream(filename);
        dataObjectImportService.importExcel(file);
    }

    protected void checkNBDataObjects(String landscapeName, int nbDataObjectExpected) {
        LandscapeView flw01 = landscapeViewRepository.findByDiagramNameIgnoreCase(landscapeName);
        assertEquals(nbDataObjectExpected, flw01.getDataObjects().size());
    }
}
