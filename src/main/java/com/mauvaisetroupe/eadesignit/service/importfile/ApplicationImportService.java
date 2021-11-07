package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ApplicationType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ApplicationImportService {

    private static final String APPLICATION_TYPE = "application.type";
    private static final String APPLICATION_COMMENT = "application.comment";
    private static final String APPLICATION_DESCRIPTION = "application.description";
    private static final String APPLICATION_NAME = "application.name";
    private static final String APPLICATION_ID = "application.id";

    private final Logger log = LoggerFactory.getLogger(ApplicationImportService.class);

    private final ApplicationRepository applicationRepository;

    public ApplicationImportService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<ApplicationImport> importExcel(MultipartFile file) throws Exception {
        ExcelReader excelReader = new ExcelReader(file);
        List<Map<String, Object>> applicationDF = excelReader.getSheetAt(0);

        String importID = (new SimpleDateFormat("YYYYMMddhhmmss")).format(new Date());
        String lowerCaseFileName = file.getOriginalFilename().toLowerCase();

        List<ApplicationImport> result = new ArrayList<ApplicationImport>();
        Long i = 0L;
        for (Map<String, Object> map : applicationDF) {
            ApplicationImport applicationImport = mapArrayToImportApplication(map);
            applicationImport.setImportId(importID);
            applicationImport.setExcelFileName(lowerCaseFileName);
            applicationImport.setId(i++);

            Application application = mapImportToApplication(applicationImport);

            if (application.getId() != null) {
                applicationImport.setImportStatus(ImportStatus.EXISTING);
            } else {
                applicationImport.setImportStatus(ImportStatus.NEW);
                application.setId(applicationImport.getIdFromExcel());
            }
            applicationRepository.save(application);
            result.add(applicationImport);
        }

        return result;
    }

    private ApplicationImport mapArrayToImportApplication(Map<String, Object> map) {
        ApplicationImport applicationImport = new ApplicationImport();
        applicationImport.setIdFromExcel((String) map.get(APPLICATION_ID));
        applicationImport.setName((String) map.get(APPLICATION_NAME));
        applicationImport.setDescription((String) map.get(APPLICATION_DESCRIPTION));
        applicationImport.setComment((String) map.get(APPLICATION_COMMENT));
        applicationImport.setType((String) map.get(APPLICATION_TYPE));
        return applicationImport;
    }

    public Application mapImportToApplication(ApplicationImport applicationImport) {
        Optional<Application> optional = applicationRepository.findById(applicationImport.getIdFromExcel());
        final Application application;
        if (optional.isPresent()) {
            application = optional.get();
            Assert.isTrue(
                application.getName().equals(applicationImport.getName()),
                "Cannot change application name (" +
                application.getName() +
                "/" +
                applicationImport.getName() +
                "), please  correrct your Excel file"
            );
        } else {
            application = new Application();
        }
        application.setComment(applicationImport.getComment());
        application.setDescription(applicationImport.getDescription());
        application.setName(applicationImport.getName());
        application.setTechnology(applicationImport.getTechnology());
        if (StringUtils.hasText(applicationImport.getType())) {
            application.setType(ObjectUtils.caseInsensitiveValueOf(ApplicationType.values(), applicationImport.getType()));
        }
        return application;
    }
}
