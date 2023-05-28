package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.ApplicationComponentRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ApplicationMapperUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    private static final String COMPONENT_SHEET_NAME = "Component";

    private final Logger log = LoggerFactory.getLogger(ApplicationImportService.class);

    private final ApplicationComponentRepository applicationComponentRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapperUtil applicationMapperUtil;

    public ComponentImportService(
        ApplicationComponentRepository applicationComponentRepository,
        ApplicationRepository applicationRepository,
        ApplicationMapperUtil applicationMapperUtil
    ) {
        this.applicationComponentRepository = applicationComponentRepository;
        this.applicationRepository = applicationRepository;
        this.applicationMapperUtil = applicationMapperUtil;
    }

    @Transactional
    public List<ApplicationImport> importExcel(InputStream inputStream, String originalFilename)
        throws EncryptedDocumentException, IOException {
        ExcelReader excelReader = new ExcelReader(inputStream);
        List<Map<String, Object>> applicationDF = excelReader.getSheet(COMPONENT_SHEET_NAME);
        log.info("Found Excel sheet " + applicationDF);

        List<ApplicationImport> result = new ArrayList<ApplicationImport>();
        Long i = 0L;
        for (Map<String, Object> map : applicationDF) {
            // Map to ApplicationImport DTO
            ApplicationImport applicationImport = applicationMapperUtil.mapArrayToImportApplication(map);

            // Find parent Application
            Application application = this.applicationRepository.findByNameIgnoreCase(applicationImport.getParentName());
            if (application == null) {
                throw new RuntimeException("Component find application with name " + applicationImport.getParentName());
            } else if (applicationImport.getParentId() != null && !applicationImport.getParentId().equals(application.getAlias())) {
                throw new RuntimeException(
                    "Pb with application id/name : " + applicationImport.getParentId() + " / " + applicationImport.getParentName()
                );
            }

            // Check if Component with same Alias and Name already exist
            ApplicationComponent component = findOrCreateComponent(applicationImport);
            if (component.getId() != null) {
                applicationImport.setImportStatus(ImportStatus.EXISTING);
            } else {
                applicationImport.setImportStatus(ImportStatus.NEW);
                component.setAlias(applicationImport.getIdFromExcel());
            }

            // Map ApplicationImport DTO to Application
            applicationMapperUtil.mapApplicationImportToComponent(applicationImport, component);

            // Set parent Application
            component.setApplication(application);

            applicationComponentRepository.save(component);
            result.add(applicationImport);
        }

        return result;
    }

    public ApplicationComponent findOrCreateComponent(ApplicationImport mapApplicationImportToComponent) {
        // Check if alias not used for another application
        if (!StringUtils.hasText(mapApplicationImportToComponent.getIdFromExcel())) {
            throw new RuntimeException("ID fro component canot be empty");
        }
        Optional<ApplicationComponent> optional = applicationComponentRepository.findByAlias(
            mapApplicationImportToComponent.getIdFromExcel()
        );
        final ApplicationComponent component;
        if (optional.isPresent()) {
            component = optional.get();
            Assert.isTrue(
                component.getName().toLowerCase().equals(mapApplicationImportToComponent.getName().toLowerCase()),
                "Cannot change application name (" +
                component.getName() +
                "/" +
                mapApplicationImportToComponent.getName() +
                "), please  correrct your Excel file"
            );
        } else {
            component = new ApplicationComponent();
        }

        ApplicationComponent appliWithSameName = applicationComponentRepository.findByNameIgnoreCase(
            mapApplicationImportToComponent.getName()
        );
        if (appliWithSameName != null) {
            Assert.isTrue(
                appliWithSameName.getAlias().toLowerCase().equals(mapApplicationImportToComponent.getIdFromExcel().toLowerCase()),
                "Cannot have same application name for two aliases '" +
                appliWithSameName.getName() +
                "' : (" +
                appliWithSameName.getAlias() +
                "/" +
                mapApplicationImportToComponent.getIdFromExcel() +
                "), please  correrct your Excel file"
            );
        }

        return component;
    }
}
