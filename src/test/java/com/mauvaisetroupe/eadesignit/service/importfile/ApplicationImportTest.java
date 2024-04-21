package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ErrorLineException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class ApplicationImportTest extends ImportFlowTest {

    @Autowired
    ApplicationImportService applicationImportService;

    @Autowired
    ApplicationRepository applicationRepository;

    @Test
    void testNullable() throws EncryptedDocumentException, IOException {
        ExcelReader excelReader = new ExcelReader(null);
        assertTrue(excelReader.isNull("?"), "should be null");
        assertTrue(excelReader.isNull("??"), "should be null");
        assertTrue(excelReader.isNull("???"), "should be null");
    }

    @Test
    void testImport1() throws EncryptedDocumentException, IOException, ErrorLineException {
        String filename = "00-import-applications.xlsx";
        String filepath = "/junit/application_import/" + filename;

        InputStream file1 = this.getClass().getResourceAsStream(filepath);
        List<ApplicationImport> applicationImports = applicationImportService.importExcel(file1, filename);
        assertEquals(4, applicationImports.size());

        List<Application> applications = applicationRepository.findAll();
        assertEquals(4, applications.size());
    }

    @Test
    void testImportWithDuplicateOwner() throws EncryptedDocumentException, IOException, ErrorLineException {
        String filename = "01-import-applications-owner-with-numerical-type.xlsx";
        String filepath = "/junit/application_import/" + filename;
        InputStream file = this.getClass().getResourceAsStream(filepath);
        try {
            applicationImportService.importExcel(file, filename);
            fail("Exception should be thrown");
        } catch (ErrorLineException e) {
            assertEquals(1, e.getErrorLines().size());
            assertTrue(e.getErrorLines().get(0).getErrorMessage().contains("jean.bon@jeanbon.com"));
        } catch (Exception e) {
            fail("ErrorLineException should be thrown");
        }
    }

    @Test
    void testImportWithNamesChanged() throws EncryptedDocumentException, IOException, ErrorLineException {
        String filename1 = "00-import-applications.xlsx";
        String filepath1 = "/junit/application_import/" + filename1;
        InputStream file1 = this.getClass().getResourceAsStream(filepath1);
        applicationImportService.importExcel(file1, filename1);

        String filename2 = "02-import-applications-name-changed.xlsx";
        String filepath2 = "/junit/application_import/" + filename2;

        InputStream file2 = this.getClass().getResourceAsStream(filepath2);
        try {
            applicationImportService.importExcel(file2, filename2);
            fail("Exception should be thrown");
        } catch (ErrorLineException e) {
            assertEquals(2, e.getErrorLines().size());
        } catch (Exception e) {
            fail("ErrorLineException should be thrown");
        }
    }
}
