package com.mauvaisetroupe.eadesignit.service.importfile.util;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationCategory;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.domain.Technology;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ApplicationType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.SoftwareType;
import com.mauvaisetroupe.eadesignit.domain.util.EnumUtil;
import com.mauvaisetroupe.eadesignit.repository.ApplicationCategoryRepository;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.repository.TechnologyRepository;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ApplicationMapperUtil {

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

    public static final String COMPONENT_ID = "component.id";
    public static final String COMPONENT_NAME = "component.name";
    public static final String COMPONENT_DISPLAY_IN_LANDSCAPE = "Display in Landscape";

    private final ApplicationCategoryRepository applicationCategoryRepository;
    private final TechnologyRepository technologyRepository;
    private final OwnerRepository ownerRepository;

    public ApplicationMapperUtil(
        ApplicationCategoryRepository applicationCategoryRepository,
        TechnologyRepository technologyRepository,
        OwnerRepository ownerRepository
    ) {
        this.applicationCategoryRepository = applicationCategoryRepository;
        this.technologyRepository = technologyRepository;
        this.ownerRepository = ownerRepository;
    }

    public ApplicationImport mapArrayToImportApplication(Map<String, Object> map) {
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

        //
        // Component specificiies
        //

        if (map.get(COMPONENT_ID) != null) {
            applicationImport.setIdFromExcel((String) map.get(COMPONENT_ID));
            applicationImport.setName((String) map.get(COMPONENT_NAME));
            applicationImport.setParentName(((String) map.get(APPLICATION_NAME)));
            applicationImport.setParentId(((String) map.get(APPLICATION_ID)));

            String _display = (String) map.get(COMPONENT_DISPLAY_IN_LANDSCAPE);
            if (_display != null && _display.toLowerCase().trim().equals("yes")) {
                applicationImport.setDisplayInLandscape(true);
            } else {
                applicationImport.setDisplayInLandscape(false);
            }
        }

        return applicationImport;
    }

    public void mapApplicationImportToApplication(ApplicationImport applicationImport, final Application application) {
        application.setComment(applicationImport.getComment());
        application.setDescription(applicationImport.getDescription());
        application.setName(applicationImport.getName());
        application.setAlias(applicationImport.getIdFromExcel());
        application.setApplicationType(getApplicationType(applicationImport));
        application.setSoftwareType(getSoftwareType(applicationImport));
        application.setTechnologies(getTechnologies(applicationImport));
        application.setCategories(getCategories(applicationImport));
        application.setOwner(getOwner(applicationImport));
    }

    public void mapApplicationImportToComponent(ApplicationImport applicationImport, final ApplicationComponent application) {
        application.setComment(applicationImport.getComment());
        application.setDescription(applicationImport.getDescription());
        application.setName(applicationImport.getName());
        application.setAlias(applicationImport.getIdFromExcel());
        application.setApplicationType(getApplicationType(applicationImport));
        application.setSoftwareType(getSoftwareType(applicationImport));
        application.setTechnologies(getTechnologies(applicationImport));
        application.setCategories(getCategories(applicationImport));
    }

    private SoftwareType getSoftwareType(ApplicationImport applicationImport) {
        SoftwareType softwareType = null;
        if (StringUtils.hasText(applicationImport.getSoftwareType())) {
            softwareType = SoftwareType.valueOf(EnumUtil.clean(applicationImport.getSoftwareType()));
        }
        return softwareType;
    }

    private ApplicationType getApplicationType(ApplicationImport applicationImport) {
        ApplicationType applicationType = null;
        if (StringUtils.hasText(applicationImport.getType())) {
            applicationType = ApplicationType.valueOf(EnumUtil.clean(applicationImport.getType()));
        }
        return applicationType;
    }

    private Set<Technology> getTechnologies(ApplicationImport applicationImport) {
        Set<Technology> technologies = new HashSet<>();
        for (String _technology : applicationImport.getTechnologies()) {
            if (StringUtils.hasText(_technology)) {
                Technology technology = technologyRepository.findByNameIgnoreCase(_technology);
                if (technology == null) {
                    technology = new Technology();
                    technology.setName(_technology);
                    technologyRepository.save(technology);
                }
                technologies.add(technology);
            }
        }
        return technologies;
    }

    private Set<ApplicationCategory> getCategories(ApplicationImport applicationImport) {
        Set<ApplicationCategory> categories = new HashSet<>();
        for (String _category : applicationImport.getCategories()) {
            ApplicationCategory applicationCategory = applicationCategoryRepository.findByNameIgnoreCase(_category);
            if (applicationCategory == null) {
                applicationCategory = new ApplicationCategory();
                applicationCategory.setName(_category);
                applicationCategoryRepository.save(applicationCategory);
            }
            categories.add(applicationCategory);
        }
        return categories;
    }

    private Owner getOwner(ApplicationImport applicationImport) {
        Owner owner = null;
        if (StringUtils.hasText(applicationImport.getOwner())) {
            owner = ownerRepository.findByNameIgnoreCase(applicationImport.getOwner());
            if (owner == null) {
                owner = new Owner();
                owner.setName(applicationImport.getOwner());
                ownerRepository.save(owner);
            }
        }
        return owner;
    }
}