package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;

public class FlowImportNewFormat extends ImportFlowTest {

    @Test
    void testImportSimpleExample() throws EncryptedDocumentException, IOException {
        String filename = "/junit/04-import-multi-flows.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        /////////////////////
        // Import APPLICATION
        /////////////////////
        InputStream file = this.getClass().getResourceAsStream(filename);
        applicationImportService.importExcel(file, "WE.DONT.CARE.NOT.USED");
        List<LandscapeView> landscapes = checkNbLandscapes(0);

        /////////////////////
        // Find Summary
        /////////////////////
        file = this.getClass().getResourceAsStream(filename); // inputstream consumed
        ExcelReader excelReader = new ExcelReader(file);
        List<Map<String, Object>> summaryDF = excelReader.getSheet(ExportFullDataService.SUMMARY_SHEET);
        boolean flowFLW01found = false;
        boolean flowFLW02found = false;
        for (Map<String, Object> map : summaryDF) {
            System.out.println(map);
            if (
                map.containsKey(ExportFullDataService.SHEET_LINK) &&
                map.get(ExportFullDataService.SHEET_LINK).equals("FLW.01") &&
                map.containsKey(ExportFullDataService.ENTITY_TYPE) &&
                map.get(ExportFullDataService.ENTITY_TYPE).equals("Landscape")
            ) flowFLW01found = true;
            if (
                map.containsKey(ExportFullDataService.SHEET_LINK) &&
                map.get(ExportFullDataService.SHEET_LINK).equals("FLW.02") &&
                map.containsKey(ExportFullDataService.ENTITY_TYPE) &&
                map.get(ExportFullDataService.ENTITY_TYPE).equals("Landscape")
            ) flowFLW02found = true;
        }
        assertTrue("flow FLW01 not found", flowFLW01found);
        assertTrue("flow FLW02 nor found", flowFLW02found);

        ///////////////////////////
        // Import FLW.01 creating a landscape called 'Invest Landscape'
        ///////////////////////////

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.01");
        landscapes = checkNbLandscapes(1);

        // 2 flows from 1 landscape
        checkNbFlows(2);

        // 6 interfaces
        List<FlowInterface> interfaces = checkNbInterfaces(6);
        for (int i = 1; i < 6; i++) {
            checkInterfaceExists("TRAD.00" + i, interfaces);
        }

        // 1 lanscape
        assertEquals(landscapes.get(0).getDiagramName(), "Invest Landscape", "Landscape should be named 'Invest Landscape'");
        checkNbFlows(landscapes.get(0), 2);
    }

    @Test
    void testImportWithNoLandscapeAssociated() throws EncryptedDocumentException, IOException {
        String filename = "/junit/04-import-multi-flows.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        /////////////////////
        // Import APPLICATION
        /////////////////////
        InputStream file = this.getClass().getResourceAsStream(filename);
        applicationImportService.importExcel(file, "WE.DONT.CARE.NOT.USED");
        List<LandscapeView> landscapes = checkNbLandscapes(0);

        /////////////////////
        // Find Summary
        /////////////////////
        file = this.getClass().getResourceAsStream(filename); // inputstream consumed
        ExcelReader excelReader = new ExcelReader(file);
        List<Map<String, Object>> summaryDF = excelReader.getSheet(ExportFullDataService.SUMMARY_SHEET);
        boolean flowFLW01found = false;
        boolean flowFLW02found = false;
        for (Map<String, Object> map : summaryDF) {
            System.out.println(map);
            if (
                map.containsKey(ExportFullDataService.SHEET_LINK) &&
                map.get(ExportFullDataService.SHEET_LINK).equals("FLW.01") &&
                map.containsKey(ExportFullDataService.ENTITY_TYPE) &&
                map.get(ExportFullDataService.ENTITY_TYPE).equals("Landscape")
            ) flowFLW01found = true;
            if (
                map.containsKey(ExportFullDataService.SHEET_LINK) &&
                map.get(ExportFullDataService.SHEET_LINK).equals("FLW.02") &&
                map.containsKey(ExportFullDataService.ENTITY_TYPE) &&
                map.get(ExportFullDataService.ENTITY_TYPE).equals("Landscape")
            ) flowFLW02found = true;
        }
        assertTrue("flow FLW01 not found", flowFLW01found);
        assertTrue("flow FLW02 nor found", flowFLW02found);

        ///////////////////////////
        // Import FLW.01 creating a landscape called 'Invest Landscape'
        ///////////////////////////

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.01");
        landscapes = checkNbLandscapes(1);

        // 2 flows from 1 landscape
        checkNbFlows(2);

        // 6 interfaces
        List<FlowInterface> interfaces = checkNbInterfaces(6);
        for (int i = 1; i < 6; i++) {
            checkInterfaceExists("TRAD.00" + i, interfaces);
        }

        // 1 lanscape
        assertEquals(landscapes.get(0).getDiagramName(), "Invest Landscape", "Landscape should be named 'Invest Landscape'");
        checkNbFlows(landscapes.get(0), 2);

        ///////////////////////////
        // Import FLW.02 with no associated landscape
        ///////////////////////////

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.04");

        // 2 + 2 from landscape + orphan
        checkNbFlows(4);
        checkNbsteps("CYP.05", 3);
        checkNbsteps("CYP.06", 2);

        interfaces = checkNbInterfaces(8);
        for (int i = 1; i < 8; i++) {
            checkInterfaceExists("TRAD.00" + i, interfaces);
        }

        // only 1 landscape (anonymouslandscape not imported)
        landscapes = checkNbLandscapes(1);
        assertEquals(landscapes.get(0).getDiagramName(), "Invest Landscape", "Landscape should be named 'Invest Landscape'");
    }

    @Test
    void testUpdateAmExisingFlow() throws EncryptedDocumentException, IOException {
        String filename = "/junit/04-import-multi-flows.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        /////////////////////
        // Import APPLICATION
        /////////////////////
        InputStream file = this.getClass().getResourceAsStream(filename);
        applicationImportService.importExcel(file, "WE.DONT.CARE.NOT.USED");
        List<LandscapeView> landscapes = checkNbLandscapes(0);

        ///////////////////////////
        // Import FLW.01 creating a landscape called 'Invest Landscape'
        ///////////////////////////

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.02");
        landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 2);
        checkNbsteps("CYP.03", 2);

        // import twice, should be idempotent
        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.03");
        checkNbsteps("CYP.03", 3);
    }

    @Test
    void testExistingInterface() throws EncryptedDocumentException, IOException {
        String filename = "/junit/04-import-multi-flows.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        /////////////////////
        // Import APPLICATION
        /////////////////////
        InputStream file = this.getClass().getResourceAsStream(filename);
        applicationImportService.importExcel(file, "WE.DONT.CARE.NOT.USED");
        List<LandscapeView> landscapes = checkNbLandscapes(0);

        ///////////////////////////
        // Import FLW.01 creating a landscape called 'Invest Landscape'
        ///////////////////////////

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.01");
        landscapes = checkNbLandscapes(1);
        checkNbFlows(2);
        List<FlowInterface> interfaces = checkNbInterfaces(6);
        // import twice, should be idempotent
        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.02");
        checkNbFlows(4);
        // 6 interfaces
        interfaces = checkNbInterfaces(10);
        for (int i = 1; i < 10; i++) {
            checkInterfaceExists("TRAD.00" + i, interfaces);
        }
    }

    @Test
    void testIdemPotent() throws EncryptedDocumentException, IOException {
        String filename = "/junit/04-import-multi-flows.xlsx";

        assertEquals(0, applicationRepository.findAll().size());
        assertEquals(0, landscapeViewRepository.findAll().size());
        assertEquals(0, functionalFlowRepository.findAll().size());

        /////////////////////
        // Import APPLICATION
        /////////////////////
        InputStream file = this.getClass().getResourceAsStream(filename);
        applicationImportService.importExcel(file, "WE.DONT.CARE.NOT.USED");
        List<LandscapeView> landscapes = checkNbLandscapes(0);

        ///////////////////////////
        // Import FLW.01 creating a landscape called 'Invest Landscape'
        ///////////////////////////

        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.02");
        landscapes = checkNbLandscapes(1);
        checkNbFlows(landscapes.get(0), 2);
        checkNbsteps("CYP.03", 2);

        // import twice, should be idempotent
        file = this.getClass().getResourceAsStream(filename);
        flowImportService.importExcelWithMultiFLWSheets(file, "FLW.02");
        checkNbsteps("CYP.03", 2);
    }
}
