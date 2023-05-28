package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationCategory;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.Technology;
import com.mauvaisetroupe.eadesignit.repository.ApplicationComponentRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ApplicationMapperUtil;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ExcelUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationExportService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationComponentRepository applicationComponentRepository;

    public ByteArrayOutputStream getApplications() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet appliSheet = workbook.createSheet("Application");
        Sheet componentSheet = workbook.createSheet("Component");

        writeApplication(appliSheet);
        ExcelUtils.autoSizeAllColumns(appliSheet);
        writeComponent(componentSheet);
        ExcelUtils.autoSizeAllColumns(componentSheet);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    private void writeApplication(Sheet sheet) {
        List<Application> applications = applicationRepository.findAll();
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);

        int maxCategories = applications.stream().map(a -> a.getCategories().size()).mapToInt(v -> v).max().orElse(0);
        int maxTechnologies = applications.stream().map(a -> a.getTechnologies().size()).mapToInt(v -> v).max().orElse(0);

        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_ID);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_NAME);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_COMMENT);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_TYPE);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.SOFTWARE_TYPE);
        for (int i = 1; i <= maxCategories; i++) {
            headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_CATEGORY_ + i);
        }
        for (int i = 1; i <= maxTechnologies; i++) {
            headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_TECHNOLOGY_ + i);
        }
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_DOCUMENTATION);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_OWNER);

        for (Application application : applications) {
            column = 0;
            Row row = sheet.createRow(rownb++);
            row.createCell(column++).setCellValue(application.getAlias());
            row.createCell(column++).setCellValue(application.getName());

            row.createCell(column++).setCellValue(application.getDescription());
            row.createCell(column++).setCellValue(application.getComment());
            if (application.getApplicationType() != null) row.createCell(column).setCellValue(application.getApplicationType().toString());
            column++;
            if (application.getSoftwareType() != null) row.createCell(column).setCellValue(application.getSoftwareType().toString());
            column++;
            int nbCategories = 0;
            for (ApplicationCategory category : application.getCategories()) {
                row.createCell(column + nbCategories).setCellValue(category.getName());
                nbCategories++;
            }
            column = column + maxCategories;
            int nbTechno = 0;
            for (Technology technology : application.getTechnologies()) {
                row.createCell(column + nbTechno).setCellValue(technology.getName());
                nbTechno++;
            }
            column = column + maxTechnologies;

            row.createCell(column++).setCellValue(application.getDocumentationURL());
            if (application.getOwner() != null) {
                row.createCell(column++).setCellValue(application.getOwner().getName());
            } else {
                column++;
            }
        }
    }

    private void writeComponent(Sheet sheet) {
        List<ApplicationComponent> applications = applicationComponentRepository.findAll();
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);

        int maxCategories = applications.stream().map(a -> a.getCategories().size()).mapToInt(v -> v).max().orElse(0);
        int maxTechnologies = applications.stream().map(a -> a.getTechnologies().size()).mapToInt(v -> v).max().orElse(0);

        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.COMPONENT_ID);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.COMPONENT_NAME);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_NAME);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_ID);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_COMMENT);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_TYPE);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.SOFTWARE_TYPE);
        for (int i = 1; i <= maxCategories; i++) {
            headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_CATEGORY_ + i);
        }
        for (int i = 1; i <= maxTechnologies; i++) {
            headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_TECHNOLOGY_ + i);
        }
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_DOCUMENTATION);

        for (ApplicationComponent application : applications) {
            column = 0;
            Row row = sheet.createRow(rownb++);
            row.createCell(column++).setCellValue(application.getAlias());
            row.createCell(column++).setCellValue(application.getName());
            row.createCell(column++).setCellValue(application.getApplication().getAlias());
            row.createCell(column++).setCellValue(application.getApplication().getName());

            row.createCell(column++).setCellValue(application.getDescription());
            row.createCell(column++).setCellValue(application.getComment());
            if (application.getApplicationType() != null) row.createCell(column).setCellValue(application.getApplicationType().toString());
            column++;
            if (application.getSoftwareType() != null) row.createCell(column).setCellValue(application.getSoftwareType().toString());
            column++;
            int nbCategories = 0;
            for (ApplicationCategory category : application.getCategories()) {
                row.createCell(column + nbCategories).setCellValue(category.getName());
                nbCategories++;
            }
            column = column + maxCategories;
            int nbTechno = 0;
            for (Technology technology : application.getTechnologies()) {
                row.createCell(column + nbTechno).setCellValue(technology.getName());
                nbTechno++;
            }
            column = column + maxTechnologies;

            row.createCell(column++).setCellValue(application.getDocumentationURL());
        }
    }
}
