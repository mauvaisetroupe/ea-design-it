package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;

public class FlowImportServiceTest {

    @Test
    void testNullable() throws EncryptedDocumentException, IOException {
        ExcelReader flowImportService = new ExcelReader(null);
        assertTrue(flowImportService.isNull("?"), "should be null");
        assertTrue(flowImportService.isNull("??"), "should be null");
        assertTrue(flowImportService.isNull("???"), "should be null");
    }
}
