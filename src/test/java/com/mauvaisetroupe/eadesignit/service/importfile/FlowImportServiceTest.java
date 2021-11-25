package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FlowImportServiceTest {

    @Test
    void testNullable() {
        FlowImportService flowImportService = new FlowImportService(null, null, null, null, null, null, null, null);
        assertTrue(flowImportService.nullable("?"), "should be null");
        assertTrue(flowImportService.nullable("??"), "should be null");
        assertTrue(flowImportService.nullable("???"), "should be null");
    }
}
