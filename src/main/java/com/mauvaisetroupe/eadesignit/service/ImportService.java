package com.mauvaisetroupe.eadesignit.service;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportService {

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
                applicationImport.setIdFromExcel(CellUtil.getCell(row, map.get("application.id")).getStringCellValue());
                applicationImport.setName(CellUtil.getCell(row, map.get("application.name")).getStringCellValue());
                applicationImport.setDescription(CellUtil.getCell(row, map.get("application.description")).getStringCellValue());
                applicationImport.setComment(CellUtil.getCell(row, map.get("application.comment")).getStringCellValue());
                applicationImport.setType(CellUtil.getCell(row, map.get("application.type")).getStringCellValue());
                applicationImport.setImportId(importID);
                applicationImport.setExcelFileName(lowerCaseFileName);
                applicationImport.setId(i++);

                Optional<Application> optional = applicationRepository.findById(applicationImport.getIdFromExcel());
                optional.ifPresentOrElse(
                    app -> {
                        applicationImport.setIdFromExcel(app.getId());
                        applicationImport.setImportStatus(ImportStatus.EXISTING);
                    },
                    () -> {
                        applicationImport.setImportStatus(ImportStatus.NEW);
                    }
                );

                result.add(applicationImport);
            }
            index++;
        }
        offices.close();
        return result;
    }

    public List<ApplicationImport> importApplication() {
        // List<ApplicationImport> applicationImports = applicationImportRepository.findByImportId(importID);
        // for (ApplicationImport applicationImport: applicationImports) {
        //     if (applicationImport.getExistingApplicationID()!=null) {
        //         Application application = applicationRepository.findById(applicationImport.getExistingApplicationID()).get();
        //         application.setComment(applicationImport.getComment());
        //         application.setDescription(applicationImport.getDescription());
        //         application.setName(applicationImport.getName());
        //     }
        //     else {

        //     }
        // }
        return null;
    }
}
