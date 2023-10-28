package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ApplicationMapperUtil;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ApplicationImportService {

    public static final String APPLICATION_SHEET_NAME = "Application";
    public static final String OWNER_SHEET_NAME = "Owner";

    private final Logger log = LoggerFactory.getLogger(ApplicationImportService.class);

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapperUtil applicationMapperUtil;

    public ApplicationImportService(ApplicationRepository applicationRepository, ApplicationMapperUtil applicationMapperUtil) {
        this.applicationRepository = applicationRepository;
        this.applicationMapperUtil = applicationMapperUtil;
    }

    @Transactional
    public List<ApplicationImport> importExcel(InputStream inputStream, String originalFilename)
        throws EncryptedDocumentException, IOException {
        ExcelReader excelReader = new ExcelReader(inputStream);

        List<Map<String, Object>> ownerDF = excelReader.getSheet(OWNER_SHEET_NAME);
        for (Map<String, Object> map : ownerDF) {
            applicationMapperUtil.mapArrayToOwner(map);
        }

        List<Map<String, Object>> applicationDF = excelReader.getSheet(APPLICATION_SHEET_NAME);
        log.info("Found Excel sheet " + applicationDF);

        List<ApplicationImport> result = new ArrayList<ApplicationImport>();
        for (Map<String, Object> map : applicationDF) {
            // Map to ApplicationImport DTO
            ApplicationImport applicationImport = applicationMapperUtil.mapArrayToImportApplication(map);

            // Check if Application with same Alias and Name already exist
            Application application = findOrCreateApplication(applicationImport);
            if (application.getId() != null) {
                applicationImport.setImportStatus(ImportStatus.EXISTING);
            } else {
                applicationImport.setImportStatus(ImportStatus.NEW);
            }

            // Map ApplicationImport DTO to Application
            applicationMapperUtil.mapApplicationImportToApplication(applicationImport, application);

            applicationRepository.save(application);
            result.add(applicationImport);
        }

        return result;
    }

    public Application findOrCreateApplication(ApplicationImport applicationImport) {
        // Check if alias not used for another application
        Application application = applicationRepository.findByAlias(applicationImport.getIdFromExcel()).orElseGet(Application::new);

        Assert.isTrue(
            application.getId() == null || application.getName().toLowerCase().equals(applicationImport.getName().toLowerCase()),
            "Cannot change name for application '" +
            application.getAlias() +
            "' : '" +
            application.getName() +
            "' in database, and '" +
            applicationImport.getName() +
            "' in your Excel file. Please correct your Excel file or modify database."
        );

        Application appliWithSameName = applicationRepository.findByNameIgnoreCase(applicationImport.getName());
        if (appliWithSameName != null) {
            Assert.isTrue(
                ("" + appliWithSameName.getAlias()).toLowerCase().equals(("" + applicationImport.getIdFromExcel()).toLowerCase()),
                "Cannot have same application name for two aliases '" +
                appliWithSameName.getName() +
                "' : (" +
                appliWithSameName.getAlias() +
                "/" +
                applicationImport.getIdFromExcel() +
                "), please  correrct your Excel file"
            );
        }
        return application;
    }
}
