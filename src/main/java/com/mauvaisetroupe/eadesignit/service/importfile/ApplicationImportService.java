package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationCategory;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.domain.Technology;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ApplicationType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.domain.enumeration.SoftwareType;
import com.mauvaisetroupe.eadesignit.domain.util.EnumUtil;
import com.mauvaisetroupe.eadesignit.repository.ApplicationCategoryRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.repository.TechnologyRepository;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service
public class ApplicationImportService {

    public static final String APPLICATION_ID = "application.id";
    public static final String APPLICATION_NAME = "application.name";
    public static final String APPLICATION_DESCRIPTION = "application.description";
    public static final String APPLICATION_COMMENT = "application.comment";
    public static final String APPLICATION_TYPE = "application.type";
    public static final String SOFTWARE_TYPE = "software.type";
    public static final String APPLICATION_CATEGORY_ = "application.category.";
    public static final String APPLICATION_TECHNOLOGY_ = "application.technology.";
    public static final String APPLICATION_DOCUMENTATION = "application.documentation";
    public static final String APPLICATION_OWNER = "application.owner";

    private static final String APPLICATION_SHEET_NAME = "Application";

    private final Logger log = LoggerFactory.getLogger(ApplicationImportService.class);

    private final ApplicationRepository applicationRepository;
    private final ApplicationCategoryRepository applicationCategoryRepository;
    private final TechnologyRepository technologyRepository;
    private final OwnerRepository ownerRepository;

    public ApplicationImportService(
        ApplicationRepository applicationRepository,
        ApplicationCategoryRepository applicationCategoryRepository,
        TechnologyRepository technologyRepository,
        OwnerRepository ownerRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.applicationCategoryRepository = applicationCategoryRepository;
        this.technologyRepository = technologyRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public List<ApplicationImport> importExcel(InputStream inputStream, String originalFilename)
        throws EncryptedDocumentException, IOException {
        ExcelReader excelReader = new ExcelReader(inputStream);
        List<Map<String, Object>> applicationDF = excelReader.getSheet(APPLICATION_SHEET_NAME);
        log.info("Found Excel sheet " + applicationDF);

        String importID = (new SimpleDateFormat("YYYYMMddhhmmss")).format(new Date());

        List<ApplicationImport> result = new ArrayList<ApplicationImport>();
        Long i = 0L;
        for (Map<String, Object> map : applicationDF) {
            ApplicationImport applicationImport = mapArrayToImportApplication(map);
            applicationImport.setImportId(importID);
            applicationImport.setExcelFileName(originalFilename);
            applicationImport.setId(i++);

            Application application = mapImportToApplication(applicationImport);

            if (application.getId() != null) {
                applicationImport.setImportStatus(ImportStatus.EXISTING);
            } else {
                applicationImport.setImportStatus(ImportStatus.NEW);
                application.setAlias(applicationImport.getIdFromExcel());
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
        applicationImport.setSoftwareType((String) map.get(SOFTWARE_TYPE));
        applicationImport.setOwner((String) map.get(APPLICATION_OWNER));
        applicationImport.setDocumentation((String) map.get(APPLICATION_DOCUMENTATION));
        for (String columnName : map.keySet().stream().filter(Objects::nonNull).collect(Collectors.toList())) {
            if (map.get(columnName) != null) {
                if (columnName.startsWith(APPLICATION_CATEGORY_)) {
                    applicationImport.getCategories().add((String) map.get(columnName));
                } else if (columnName.startsWith(APPLICATION_TECHNOLOGY_)) {
                    applicationImport.getTechnologies().add((String) map.get(columnName));
                }
            }
        }
        return applicationImport;
    }

    public Application mapImportToApplication(ApplicationImport applicationImport) {
        // Check if alias not used for another application
        Optional<Application> optional = applicationRepository.findByAlias(applicationImport.getIdFromExcel());
        final Application application;
        if (optional.isPresent()) {
            application = optional.get();
            Assert.isTrue(
                application.getName().toLowerCase().equals(applicationImport.getName().toLowerCase()),
                "Cannot change name for application '" +
                application.getAlias() +
                "' : '" +
                application.getName() +
                "' in database, and '" +
                applicationImport.getName() +
                "' in your Excel file. Please correct your Excel file or modify database."
            );
        } else {
            application = new Application();
        }

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

        application.setComment(applicationImport.getComment());
        application.setDescription(applicationImport.getDescription());
        application.setName(applicationImport.getName());

        // Application Type
        if (StringUtils.hasText(applicationImport.getType())) {
            application.setApplicationType(ApplicationType.valueOf(EnumUtil.clean(applicationImport.getType())));
        }

        // Software Type
        if (StringUtils.hasText(applicationImport.getSoftwareType())) {
            application.setSoftwareType(SoftwareType.valueOf(EnumUtil.clean(applicationImport.getSoftwareType())));
        }

        // Technology

        for (String _technology : applicationImport.getTechnologies()) {
            if (StringUtils.hasText(_technology)) {
                Technology technology = technologyRepository.findByNameIgnoreCase(_technology);
                if (technology == null) {
                    technology = new Technology();
                    technology.setName(_technology);
                    technologyRepository.save(technology);
                }
                application.addTechnologies(technology);
            }
        }

        // owner
        if (StringUtils.hasText(applicationImport.getOwner())) {
            Owner owner = ownerRepository.findByNameIgnoreCase(applicationImport.getOwner());
            if (owner == null) {
                owner = new Owner();
                owner.setName(applicationImport.getOwner());
                ownerRepository.save(owner);
            }
            application.setOwner(owner);
        }

        // Categories

        for (String _category : applicationImport.getCategories()) {
            ApplicationCategory applicationCategory = applicationCategoryRepository.findByNameIgnoreCase(_category);
            if (applicationCategory == null) {
                applicationCategory = new ApplicationCategory();
                applicationCategory.setName(_category);
                applicationCategoryRepository.save(applicationCategory);
            }
            application.addCategories(applicationCategory);
        }
        return application;
    }
}
