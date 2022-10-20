package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationCategory;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponentImport;
import com.mauvaisetroupe.eadesignit.domain.Technology;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ApplicationType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.domain.enumeration.SoftwareType;
import com.mauvaisetroupe.eadesignit.domain.util.EnumUtil;
import com.mauvaisetroupe.eadesignit.repository.ApplicationCategoryRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationComponentRepository;
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
import java.util.Optional;
import javax.transaction.Transactional;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service
public class ComponentImportService {

    private static final String COMPONENT_ID = "component.id";
    private static final String COMPONENT_NAME = "component.name";
    private static final String APPLICATION_ID = "application.id";
    private static final String APPLICATION_NAME = "application.name";
    private static final String COMPONENT_DISPLAY_IN_LANDSCAPE = "Display in Landscape";
    private static final String COMPONENT_DESCRIPTION = "application.description";
    private static final String COMPONENT_COMMENT = "application.comment";
    private static final String COMPONENT_TYPE = "application.type";
    private static final String SOFTWARE_TYPE = "software.type";
    private static final String COMPONENT_CATEGORY_1 = "application.category.1";
    private static final String COMPONENT_CATEGORY_2 = "application.category.2";
    private static final String COMPONENT_CATEGORY_3 = "application.category.3";
    private static final String COMPONENT_TECHNOLOGY = "application.technology";
    private static final String COMPONENT_DOCUMENTATION = "application.documentation";

    private final List<String> columnsArray = new ArrayList<String>();

    private static final String COMPONENT_SHEET_NAME = "Component";

    private final Logger log = LoggerFactory.getLogger(ApplicationImportService.class);

    private final ApplicationComponentRepository applicationComponentRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationCategoryRepository applicationCategoryRepository;
    private final TechnologyRepository technologyRepository;

    public ComponentImportService(
        ApplicationComponentRepository applicationComponentRepository,
        ApplicationRepository applicationRepository,
        ApplicationCategoryRepository applicationCategoryRepository,
        TechnologyRepository technologyRepository,
        OwnerRepository ownerRepository
    ) {
        this.applicationComponentRepository = applicationComponentRepository;
        this.applicationRepository = applicationRepository;
        this.applicationCategoryRepository = applicationCategoryRepository;
        this.technologyRepository = technologyRepository;

        this.columnsArray.add(COMPONENT_ID);
        this.columnsArray.add(COMPONENT_NAME);
        this.columnsArray.add(APPLICATION_ID);
        this.columnsArray.add(APPLICATION_NAME);
        this.columnsArray.add(COMPONENT_DISPLAY_IN_LANDSCAPE);
        this.columnsArray.add(COMPONENT_DESCRIPTION);
        this.columnsArray.add(COMPONENT_COMMENT);
        this.columnsArray.add(COMPONENT_TYPE);
        this.columnsArray.add(SOFTWARE_TYPE);
        this.columnsArray.add(COMPONENT_CATEGORY_1);
        this.columnsArray.add(COMPONENT_CATEGORY_2);
        this.columnsArray.add(COMPONENT_CATEGORY_3);
        this.columnsArray.add(COMPONENT_TECHNOLOGY);
        this.columnsArray.add(COMPONENT_DOCUMENTATION);
    }

    @Transactional
    public List<ApplicationComponentImport> importExcel(InputStream inputStream, String originalFilename)
        throws EncryptedDocumentException, IOException {
        ExcelReader excelReader = new ExcelReader(inputStream);
        List<Map<String, Object>> applicationDF = excelReader.getSheet(COMPONENT_SHEET_NAME);
        log.info("Found Excel sheet " + applicationDF);

        String importID = (new SimpleDateFormat("YYYYMMddhhmmss")).format(new Date());

        List<ApplicationComponentImport> result = new ArrayList<ApplicationComponentImport>();
        Long i = 0L;
        for (Map<String, Object> map : applicationDF) {
            ApplicationComponentImport applicationImport = mapArrayToImportApplication(map);
            applicationImport.setImportId(importID);
            applicationImport.setExcelFileName(originalFilename);
            applicationImport.setId(i++);

            try {
                if (!StringUtils.hasText(applicationImport.getApplicationName())) {
                    throw new RuntimeException("Component name cannot be empty");
                }
                ApplicationComponent component = mapImportToApplication(applicationImport);
                if (component.getApplication() == null) {
                    throw new RuntimeException("Component find application with name " + applicationImport.getApplicationName());
                }
                if (component.getId() != null) {
                    applicationImport.setImportStatus(ImportStatus.EXISTING);
                } else {
                    applicationImport.setImportStatus(ImportStatus.NEW);
                    component.setAlias(applicationImport.getIdFromExcel());
                }
                applicationComponentRepository.save(component);
            } catch (Exception e) {
                applicationImport.setImportStatus(ImportStatus.ERROR);
                applicationImport.setImportStatusMessage(e.getMessage());
            }
            result.add(applicationImport);
        }

        return result;
    }

    private ApplicationComponentImport mapArrayToImportApplication(Map<String, Object> map) {
        ApplicationComponentImport applicationImport = new ApplicationComponentImport();
        applicationImport.setIdFromExcel((String) map.get(COMPONENT_ID));
        applicationImport.setName((String) map.get(COMPONENT_NAME));
        applicationImport.setApplicationName((String) map.get(APPLICATION_NAME));

        String _external = (String) map.get(COMPONENT_DISPLAY_IN_LANDSCAPE);
        if (_external != null && _external.toLowerCase().trim().equals("yes")) {
            applicationImport.setDisplayInLandscape(true);
        } else {
            applicationImport.setDisplayInLandscape(false);
        }

        applicationImport.setName((String) map.get(COMPONENT_NAME));
        applicationImport.setDescription((String) map.get(COMPONENT_DESCRIPTION));
        applicationImport.setComment((String) map.get(COMPONENT_COMMENT));
        applicationImport.setType((String) map.get(COMPONENT_TYPE));
        applicationImport.setSoftwareType((String) map.get(SOFTWARE_TYPE));
        applicationImport.setTechnology((String) map.get(COMPONENT_TECHNOLOGY));
        applicationImport.setCategory1((String) map.get(COMPONENT_CATEGORY_1));
        applicationImport.setCategory2((String) map.get(COMPONENT_CATEGORY_2));
        applicationImport.setCategory3((String) map.get(COMPONENT_CATEGORY_3));
        applicationImport.setDocumentation((String) map.get(COMPONENT_DOCUMENTATION));
        return applicationImport;
    }

    public ApplicationComponent mapImportToApplication(ApplicationComponentImport applicationImport) {
        // Check if alias not used for another application
        if (!StringUtils.hasText(applicationImport.getIdFromExcel())) {
            throw new RuntimeException("ID fro component canot be empty");
        }
        Optional<ApplicationComponent> optional = applicationComponentRepository.findByAlias(applicationImport.getIdFromExcel());
        final ApplicationComponent component;
        if (optional.isPresent()) {
            component = optional.get();
            Assert.isTrue(
                component.getName().toLowerCase().equals(applicationImport.getName().toLowerCase()),
                "Cannot change application name (" +
                component.getName() +
                "/" +
                applicationImport.getName() +
                "), please  correrct your Excel file"
            );
        } else {
            component = new ApplicationComponent();
        }

        ApplicationComponent appliWithSameName = applicationComponentRepository.findByNameIgnoreCase(applicationImport.getName());
        if (appliWithSameName != null) {
            Assert.isTrue(
                appliWithSameName.getAlias().toLowerCase().equals(applicationImport.getIdFromExcel().toLowerCase()),
                "Cannot have same application name for two aliases '" +
                appliWithSameName.getName() +
                "' : (" +
                appliWithSameName.getAlias() +
                "/" +
                applicationImport.getIdFromExcel() +
                "), please  correrct your Excel file"
            );
        }

        component.setComment(applicationImport.getComment());
        component.setDescription(applicationImport.getDescription());
        component.setName(applicationImport.getName());

        // Application Parent
        Application application = this.applicationRepository.findByNameIgnoreCase(applicationImport.getApplicationName());
        component.setApplication(application);

        // Application Type
        if (StringUtils.hasText(applicationImport.getType())) {
            component.setApplicationType(ApplicationType.valueOf(EnumUtil.clean(applicationImport.getType())));
        }

        // Software Type
        if (StringUtils.hasText(applicationImport.getSoftwareType())) {
            component.setSoftwareType(SoftwareType.valueOf(EnumUtil.clean(applicationImport.getSoftwareType())));
        }

        // Technology

        if (StringUtils.hasText(applicationImport.getTechnology())) {
            Technology technology = technologyRepository.findByNameIgnoreCase(applicationImport.getTechnology());
            if (technology == null) {
                technology = new Technology();
                technology.setName(applicationImport.getTechnology());
                technologyRepository.save(technology);
            }
            component.addTechnologies(technology);
        }

        // Categories 1, 2 et 3

        if (StringUtils.hasText(applicationImport.getCategory1())) {
            ApplicationCategory applicationCategory1 = applicationCategoryRepository.findByNameIgnoreCase(applicationImport.getCategory1());
            if (applicationCategory1 == null) {
                applicationCategory1 = new ApplicationCategory();
                applicationCategory1.setName(applicationImport.getCategory1());
                applicationCategoryRepository.save(applicationCategory1);
            }
            component.addCategories(applicationCategory1);
        }

        if (StringUtils.hasText(applicationImport.getCategory2())) {
            ApplicationCategory applicationCategory2 = applicationCategoryRepository.findByNameIgnoreCase(applicationImport.getCategory2());
            if (applicationCategory2 == null) {
                applicationCategory2 = new ApplicationCategory();
                applicationCategory2.setName(applicationImport.getCategory2());
                applicationCategoryRepository.save(applicationCategory2);
            }
            component.addCategories(applicationCategory2);
        }

        if (StringUtils.hasText(applicationImport.getCategory3())) {
            ApplicationCategory applicationCategory3 = applicationCategoryRepository.findByNameIgnoreCase(applicationImport.getCategory3());
            if (applicationCategory3 == null) {
                applicationCategory3 = new ApplicationCategory();
                applicationCategory3.setName(applicationImport.getCategory3());
                applicationCategoryRepository.save(applicationCategory3);
            }
            component.addCategories(applicationCategory3);
        }
        return component;
    }
}
