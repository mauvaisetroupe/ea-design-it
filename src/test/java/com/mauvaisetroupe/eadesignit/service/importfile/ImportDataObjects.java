package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;

public class ImportDataObjects extends ImportFlowTest {

    @Test
    @Transactional
    void testImportDataObjects() throws EncryptedDocumentException, IOException {
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

        List<DataObject> allCustomerDO = dataObjectRepository.findByNameIgnoreCase("Customer");
        assertEquals(4, allCustomerDO.size());

        for (String appName : new String[] { "APPLICATION-0002", "APPLICATION-0003", "APPLICATION-0004", "APPLICATION-0005" }) {
            assertThat(
                dataObjectRepository
                    .findByNameIgnoreCaseAndParentAndApplication("Customer", null, applicationRepository.findByNameIgnoreCase(appName))
                    .isPresent()
            );
        }

        // Risk Profile	                    GOLDEN_SOURCE	        APPLICATION-0005
        // Customer > Risk Profile          READ_ONLY_REPLICA       APPLICATION-0002
        // Customer	                        READ_ONLY_REPLICA	    APPLICATION-0002

        assertEquals(2, dataObjectRepository.findByNameIgnoreCase("Risk Profile").size());

        Application application_0005 = applicationRepository.findByNameIgnoreCase("APPLICATION-0002");
        assertEquals("CYP.CMP.00000002", application_0005.getAlias());

        Optional<DataObject> customerDOOptional = dataObjectRepository.findByNameIgnoreCaseAndParentAndApplication(
            "Customer",
            null,
            application_0005
        );
        assertTrue(customerDOOptional.isPresent());

        Optional<DataObject> RiskDOOptional = dataObjectRepository.findByNameIgnoreCaseAndParentAndApplication(
            "Risk Profile",
            customerDOOptional.orElseThrow(),
            application_0005
        );
        assertTrue(RiskDOOptional.isPresent());
    }
}
