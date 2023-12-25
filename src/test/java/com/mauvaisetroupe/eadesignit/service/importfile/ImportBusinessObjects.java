package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;

public class ImportBusinessObjects extends ImportFlowTest {

    @Test
    @Transactional
    void testImportBusinessObjects() throws EncryptedDocumentException, IOException {
        String filename = "/junit/05-import-multi-flows-with-capas-and-dataobjects.xlsx";

        // Party		yes
        // Customer	Party		Customer	READ_ONLY_REPLICA	APPLICATION-0005	Invest Landscape
        // Customer	Party		Customer	READ_ONLY_REPLICA	APPLICATION-0002	Invest Landscape
        // Customer	Party		Customer	GOLDEN_SOURCE	APPLICATION-0003	Invest Landscape
        // Customer	Party		Customer	READ_ONLY_REPLICA	APPLICATION-0004	Invest Landscape
        // Customer > Address			Address	GOLDEN_SOURCE	APPLICATION-0005	Invest Landscape
        // Customer > Risk Profile			Risk Profile	GOLDEN_SOURCE	APPLICATION-0005	Invest Landscape
        // Customer > Risk Profile			Customer > Risk Profile	READ_ONLY_REPLICA	APPLICATION-0002	Payment Landscape
        // Prospect	Party		Prospect	GOLDEN_SOURCE	APPLICATION-0001	Payment Landscape
        // Prospect	Party		Prospect	READ_ONLY_REPLICA	APPLICATION-0002	Payment Landscape
        // Prospect > External Assets			Prospect > External Assets	READ_ONLY_REPLICA	APPLICATION-0001	Payment Landscape
        // Prospect > External Assets			Prospect > External Assets	GOLDEN_SOURCE	APPLICATION-0002	Payment Landscape
        // Prospect > External Assets > Cash			Prospect > External Assets > Cash	GOLDEN_SOURCE	APPLICATION-0003	Payment Landscape

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
        dataObjectImportService.importExcel(file);

        assertEquals(7, businessObjectRepository.findAll().size());
        assertThat(businessObjectRepository.findByNameIgnoreCase("Customer").isPresent());
        assertThat(businessObjectRepository.findByNameIgnoreCaseAndParentNameIgnoreCase("Risk Profile", "Customer").isPresent());
        assertThat(businessObjectRepository.findByNameIgnoreCaseAndParentNameIgnoreCase("Address", "Customer").isPresent());

        assertThat(businessObjectRepository.findByNameIgnoreCase("Prospect").isPresent());
        assertThat(businessObjectRepository.findByNameIgnoreCaseAndParentNameIgnoreCase("External Assets", "Prospect").isPresent());
        assertThat(businessObjectRepository.findByNameIgnoreCaseAndParentNameIgnoreCase("Cash", "External Assets").isPresent());
        assertEquals(
            "Prospect",
            businessObjectRepository
                .findByNameIgnoreCaseAndParentNameIgnoreCase("Cash", "External Assets")
                .orElseThrow()
                .getParent()
                .getParent()
                .getName()
        );
    }
}
