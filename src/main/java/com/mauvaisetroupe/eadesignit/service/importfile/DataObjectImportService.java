package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.BusinessObject;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.Technology;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.BusinessObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.DataObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.TechnologyRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.DataObjectDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.DataObjectImport;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DataObjectImportService {

    private final Logger log = LoggerFactory.getLogger(DataObjectImportService.class);

    @Autowired
    private LandscapeViewRepository landscapeViewRepository;

    @Autowired
    private DataObjectRepository dataObjectRepository;

    @Autowired
    private BusinessObjectRepository businessObjectRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    public static final String BUSINESS_OBJECT = "businessobject.fullpath";
    public static final String BUSINESS_OBJECT_GENERALIZATION = "generalization.fullpath";
    public static final String BUSINESS_OBJECT_ABSTRACT = "abstract";
    public static final String DATA_OBJECT = "dataobject.fullpath";
    public static final String DATA_OBJECT_APPLICATION = "application";
    public static final String LANDSCAPE_NAME_PREFIX = "landscape.";
    public static final String DATA_OBJECT_TYPE = "dataobject.type";
    public static final String DATA_OBJECT_SHEET_NAME = "BO";

    public DataObjectDTO importExcel(InputStream excel) throws IOException {
        ExcelReader dataObjectExcelReader = new ExcelReader(excel);
        List<DataObjectImport> result = new ArrayList<DataObjectImport>();

        Set<String> businessObjectFullPath = new HashSet<>();

        // Delete all BusinessObject and all DataObjects
        deleteDataObjects();
        deleteBusinessObjects();

        List<Map<String, Object>> df = dataObjectExcelReader.getSheet(DATA_OBJECT_SHEET_NAME);
        for (Map<String, Object> map : df) {
            // Populate DTO from map
            DataObjectImport dto = new DataObjectImport();
            dto.setBusinessobject((String) map.get(BUSINESS_OBJECT));
            dto.setDataobject((String) map.get(DATA_OBJECT));
            dto.setGeneralization((String) map.get(BUSINESS_OBJECT_GENERALIZATION));
            dto.setType((String) map.get(DATA_OBJECT_TYPE));
            String _abstract = (String) map.get(BUSINESS_OBJECT_ABSTRACT);
            if (_abstract != null && _abstract.toLowerCase().trim().equals("yes")) {
                dto.setAbstractValue(true);
            } else {
                dto.setAbstractValue(false);
            }
            dto.setApplication((String) map.get(DATA_OBJECT_APPLICATION));
            for (String columnName : map.keySet().stream().filter(Objects::nonNull).collect(Collectors.toList())) {
                if (map.get(columnName) != null) {
                    if (columnName.startsWith(LANDSCAPE_NAME_PREFIX)) {
                        dto.getLandscapes().add((String) map.get(columnName));
                    }
                }
            }

            // Create Business Objects from full path
            BusinessObject bo = null;
            if (dto.getBusinessobject() != null) {
                String[] bos = dto.getBusinessobject().split("\\w*>\\w*");
                BusinessObject parent = null;
                for (int i = 0; i < bos.length; i++) {
                    String boName = bos[i].trim();
                    bo = findOrCreateBO(parent, boName);
                    parent = bo;
                }
                bo.setAbstractBusinessObject(dto.isAbstractValue());
                businessObjectRepository.save(bo);
                // Create Business Object from Generalization
                if (StringUtils.hasText(dto.getGeneralization())) {
                    BusinessObject generalization = findOrCreateBO(null, dto.getGeneralization());
                    bo.setGeneralization(generalization);
                    businessObjectRepository.save(bo);
                }
            }

            // Find Application when exists
            Application application = null;
            if (StringUtils.hasText(dto.getApplication())) {
                application = applicationRepository.findByNameIgnoreCase(dto.getApplication());
                if (application == null) {
                    throw new IllegalStateException("Cannot find application [" + dto.getApplication() + "]");
                }
            }

            // Create Data Object from fullpath
            if (StringUtils.hasText(dto.getDataobject())) {
                try {
                    if (application == null) {
                        dto.setErrorMessage("Application should not be null");
                        dto.setImportStatus(ImportStatus.ERROR);
                        throw new IllegalStateException();
                    }

                    String[] dos = dto.getDataobject().split("\\w*>\\w*");
                    DataObject dataObjectParent = null;
                    if (dos.length > 1) {
                        dataObjectParent = checkParentExist(dos, application);
                        if (dataObjectParent == null) {
                            dto.setErrorMessage("All DataObject parents should be exist and be declared in a previous line");
                            dto.setImportStatus(ImportStatus.ERROR);
                            throw new IllegalStateException();
                        }
                    }
                    String dataObjectName = dos[dos.length - 1].trim();
                    DataObject dataObject = findOrCreateDO(dataObjectName, dataObjectParent, application);
                    // overwrite a value
                    dataObject.setBusinessObject(bo);
                    dataObject.setType(dto.getType());

                    // create link to landascape
                    if (dto.getLandscapes() != null && dto.getLandscapes().size() > 0) {
                        for (String landscapename : dto.getLandscapes()) {
                            LandscapeView landscape = landscapeViewRepository.findByDiagramNameIgnoreCase(landscapename);
                            if (landscape == null) {
                                throw new IllegalStateException("Cannot find landscape " + landscapename);
                            }
                            dataObject.addLandscapes(landscape);
                            landscapeViewRepository.save(landscape);
                        }
                        dataObjectRepository.save(dataObject);
                    }
                } catch (IllegalStateException e) {
                    log.error("Error with line " + map);
                }
            }
            result.add(dto);
        }

        DataObjectDTO dataObjectDTO = new DataObjectDTO();
        dataObjectDTO.setSheetname(DATA_OBJECT_SHEET_NAME);
        dataObjectDTO.setDtos(result);
        return dataObjectDTO;
    }

    private DataObject checkParentExist(String[] dos, Application application) {
        DataObject dataObjectParent = null;
        DataObject dataObject = null;
        for (int i = 0; i < dos.length - 1; i++) {
            String doName = dos[i].trim();
            dataObject =
                dataObjectRepository.findByNameIgnoreCaseAndParentAndApplication(doName, dataObjectParent, application).orElse(null);

            if (dataObject == null) return null;
            dataObjectParent = dataObject;
        }
        return dataObject;
    }

    private BusinessObject findOrCreateBO(BusinessObject parent, String boName) {
        BusinessObject bo;
        bo =
            ((parent == null)
                    ? businessObjectRepository.findByNameIgnoreCase(boName)
                    : businessObjectRepository.findByNameIgnoreCaseAndParentNameIgnoreCase(boName, parent.getName())).orElseGet(
                    BusinessObject::new
                );
        if (bo.getId() == null) {
            bo.setName(boName);
            bo.setParent(parent);
            businessObjectRepository.save(bo);
        }
        return bo;
    }

    private DataObject findOrCreateDO(String dataObjectName, DataObject parent, Application application) {
        DataObject dataObj;
        dataObj =
            dataObjectRepository
                .findByNameIgnoreCaseAndParentAndApplication(dataObjectName, parent, application)
                .orElseGet(DataObject::new);
        if (dataObj.getId() == null) {
            dataObj.setName(dataObjectName);
            dataObj.setParent(parent);
            dataObj.setApplication(application);
            dataObjectRepository.save(dataObj);
        }
        return dataObj;
    }

    private void deleteDataObjects() {
        List<DataObject> dataObjects = new ArrayList<>(dataObjectRepository.findAll());
        for (DataObject dataObject : dataObjects) {
            dataObject.setParent(null);
            dataObject.setBusinessObject(null);
            for (LandscapeView landscapeView : new ArrayList<>(dataObject.getLandscapes())) {
                landscapeView.removeDataObjects(dataObject);
                landscapeViewRepository.save(landscapeView);
            }
            for (Technology technology : new ArrayList<>(dataObject.getTechnologies())) {
                technology.removeDataObjects(dataObject);
                technologyRepository.save(technology);
            }
        }
        for (DataObject dataObject : dataObjects) {
            dataObjectRepository.delete(dataObject);
        }
    }

    private void deleteBusinessObjects() {
        List<BusinessObject> businessObjects = new ArrayList<>(businessObjectRepository.findAll());
        for (BusinessObject businessObject : businessObjects) {
            businessObject.setParent(null);
            businessObject.setGeneralization(null);
        }
        for (BusinessObject businessObject : businessObjects) {
            businessObjectRepository.delete(businessObject);
        }
    }

    private void processError(DataObjectImport applicationCapability, String error) {
        log.error(error);
        applicationCapability.setImportStatus(ImportStatus.ERROR);
        applicationCapability.setErrorMessage(error);
    }
}
