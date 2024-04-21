package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportAnalysisDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ErrorLineException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;

public class ReImportLandscapeWithCapaAndDatObjects extends ImportFlowTest {

    @Test
    @Transactional
    void testReimportLandscapeWithCapabilities() throws EncryptedDocumentException, IOException, ErrorLineException {
        String filename = "/junit/05-import-multi-flows-with-capas-and-dataobjects.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

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
        checkNbLandscapes(4);

        file = this.getClass().getResourceAsStream(filename);
        CapabilityImportAnalysisDTO analysisDTO = capabilityImportService.analyzeExcel(file);
        capabilityImportService.confirmImport(analysisDTO);

        checkNbCapabilities(26);

        file = this.getClass().getResourceAsStream(filename);
        applicationCapabilityImportService.importExcel(file, "CPB.01"); // FLW.01

        checkNbCapabilityMappings(9);

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.01");

        checkNbCapabilityMappings(9);
    }

    @Test
    @Transactional
    void testReimportLandscapeWithDataObjects() throws EncryptedDocumentException, IOException, ErrorLineException {
        String filename = "/junit/05-import-multi-flows-with-capas-and-dataobjects.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

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
        checkNbLandscapes(4);

        file = this.getClass().getResourceAsStream(filename);
        CapabilityImportAnalysisDTO analysisDTO = capabilityImportService.analyzeExcel(file);
        capabilityImportService.confirmImport(analysisDTO);

        checkNbCapabilities(26);

        file = this.getClass().getResourceAsStream(filename);
        applicationCapabilityImportService.importExcel(file, "CPB.01"); // FLW.01

        checkNbCapabilityMappings(9);

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.01");

        checkNbCapabilityMappings(9);
    }
}
