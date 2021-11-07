package com.mauvaisetroupe.eadesignit.service;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ApplicationType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.ApplicationImportRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportService {

    private static final String APPLICATION_TYPE = "application.type";
    private static final String APPLICATION_COMMENT = "application.comment";
    private static final String APPLICATION_DESCRIPTION = "application.description";
    private static final String APPLICATION_NAME = "application.name";
    private static final String APPLICATION_ID = "application.id";

    private final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;
    private final ApplicationImportRepository applicationImportRepository;

    public ImportService(ApplicationRepository applicationRepository, ApplicationImportRepository applicationImportRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationImportRepository = applicationImportRepository;
    }

    public List<ApplicationImport> importExcel(MultipartFile file) throws IOException {
        Workbook offices;
        String lowerCaseFileName = file.getOriginalFilename().toLowerCase();
        if (lowerCaseFileName.endsWith(".xlsx")) {
            offices = new XSSFWorkbook(file.getInputStream());
        } else {
            offices = new HSSFWorkbook(file.getInputStream());
        }
        Sheet sheet = offices.getSheetAt(0);

        String importID = (new SimpleDateFormat("YYYYMMddhhmmss")).format(new Date());

        int index = 0;
        Map<String, Integer> map = new HashMap<String, Integer>(); //Create map

        List<ApplicationImport> result = new ArrayList<ApplicationImport>();
        Long i = 0L;
        for (Row row : sheet) {
            if (index == 0) {
                for (Cell cell : row) {
                    // map column name and column index
                    map.put(cell.getStringCellValue(), cell.getColumnIndex());
                }
            } else {
                //application.name application.description application.comment application.type
                final ApplicationImport applicationImport = new ApplicationImport();
                applicationImport.setIdFromExcel(CellUtil.getCell(row, map.get(APPLICATION_ID)).getStringCellValue());
                applicationImport.setName(CellUtil.getCell(row, map.get(APPLICATION_NAME)).getStringCellValue());
                applicationImport.setDescription(CellUtil.getCell(row, map.get(APPLICATION_DESCRIPTION)).getStringCellValue());
                applicationImport.setComment(CellUtil.getCell(row, map.get(APPLICATION_COMMENT)).getStringCellValue());
                applicationImport.setType(CellUtil.getCell(row, map.get(APPLICATION_TYPE)).getStringCellValue());
                applicationImport.setImportId(importID);
                applicationImport.setExcelFileName(lowerCaseFileName);
                applicationImport.setId(i++);

                Application application = mapImportToApplicayion(applicationImport);
                if (application.getId() != null) {
                    applicationImport.setImportStatus(ImportStatus.EXISTING);
                } else {
                    applicationImport.setImportStatus(ImportStatus.UPDATED);
                    application.setId(applicationImport.getIdFromExcel());
                }
                applicationRepository.save(application);
                result.add(applicationImport);
            }
            index++;
        }
        offices.close();
        return result;
    }

    public Application mapImportToApplicayion(ApplicationImport applicationImport) {
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
            application.setType(ApplicationType.valueOf(applicationImport.getType().toUpperCase()));
        }
        return application;
    }
}
