package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationCategory;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.ExternalReference;
import com.mauvaisetroupe.eadesignit.domain.ExternalSystem;
import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.domain.Technology;
import com.mauvaisetroupe.eadesignit.repository.ApplicationComponentRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.ExternalSystemRepository;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ApplicationMapperUtil;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ExcelUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    ExternalSystemRepository externalSystemRepository;

    public ByteArrayOutputStream getApplications() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet appliSheet = workbook.createSheet(ApplicationImportService.APPLICATION_SHEET_NAME);
        Sheet componentSheet = workbook.createSheet(ComponentImportService.COMPONENT_SHEET_NAME);
        Sheet ownerSheet = workbook.createSheet(ApplicationImportService.OWNER_SHEET_NAME);

        writeSheets(appliSheet, componentSheet, ownerSheet, workbook);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    protected void writeSheets(Sheet appliSheet, Sheet componentSheet, Sheet ownerSheet, Workbook workbook) {
        writeApplication(appliSheet);
        ExcelUtils.autoSizeAllColumns(appliSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, appliSheet);
        writeComponent(componentSheet);
        ExcelUtils.autoSizeAllColumns(componentSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, componentSheet);
        writeOwner(ownerSheet);
        ExcelUtils.autoSizeAllColumns(ownerSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, ownerSheet);
    }

    protected void writeApplication(Sheet sheet) {
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
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_NICKNAME);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_DOCUMENTATION);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_OWNER);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.IT_OWNER);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.BUSINESS_OWNER);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.APPLICATION_ORGANIZATIONAL_ENTITY);

        Map<String, Integer> externalOrder = writeExternalSystemHeader(column, headerRow);

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

            row.createCell(column++).setCellValue(application.getNickname());
            row.createCell(column++).setCellValue(application.getDocumentationURL());

            if (application.getOwner() != null) {
                row.createCell(column).setCellValue(application.getOwner().getName());
            }
            column++;
            if (application.getItOwner() != null) {
                row.createCell(column).setCellValue(application.getItOwner().getName());
            }
            column++;
            if (application.getBusinessOwner() != null) {
                row.createCell(column).setCellValue(application.getBusinessOwner().getName());
            }
            column++;
            if (application.getOrganizationalEntity() != null) {
                row.createCell(column).setCellValue(application.getOrganizationalEntity().getName());
            }
            column++;

            writeExternalSytemRow(column, externalOrder, application.getExternalIDS(), row);
            column = column + externalOrder.entrySet().size();
        }
    }

    protected void writeComponent(Sheet sheet) {
        List<ApplicationComponent> applications = applicationComponentRepository.findAll();
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);

        int maxCategories = applications.stream().map(a -> a.getCategories().size()).mapToInt(v -> v).max().orElse(0);
        int maxTechnologies = applications.stream().map(a -> a.getTechnologies().size()).mapToInt(v -> v).max().orElse(0);

        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.COMPONENT_ID);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.COMPONENT_NAME);
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

        Map<String, Integer> externalOrder = writeExternalSystemHeader(column, headerRow);
        column = column + externalOrder.entrySet().size();

        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.COMPONENT_DISPLAY_IN_LANDSCAPE);

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

            writeExternalSytemRow(column, externalOrder, application.getExternalIDS(), row);
            column = column + externalOrder.entrySet().size();

            String displayComponentInLandscape = "";
            if (application.getDisplayInLandscape() != null && application.getDisplayInLandscape()) {
                displayComponentInLandscape = "yes";
            }
            row.createCell(column++).setCellValue(displayComponentInLandscape);
        }
    }

    protected void writeOwner(Sheet sheet) {
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);

        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.OWNER_NAME);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.OWNER_FIRSTNAME);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.OWNER_LASTNAME);
        headerRow.createCell(column++).setCellValue(ApplicationMapperUtil.OWNER_EMAIL);

        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            column = 0;
            Row row = sheet.createRow(rownb++);
            row.createCell(column++).setCellValue(owner.getName());
            row.createCell(column++).setCellValue(owner.getFirstname());
            row.createCell(column++).setCellValue(owner.getLastname());
            row.createCell(column++).setCellValue(owner.getEmail());
        }
    }

    private void writeExternalSytemRow(int column, Map<String, Integer> externalOrder, Set<ExternalReference> set, Row row) {
        for (ExternalReference externalReference : set) {
            row
                .createCell(column + externalOrder.get(externalReference.getExternalSystem().getExternalSystemID()))
                .setCellValue(externalReference.getExternalID());
        }
    }

    protected Map<String, Integer> writeExternalSystemHeader(int column, Row headerRow) {
        int i = 0;
        Map<String, Integer> externalOrder = new HashMap<>();
        for (ExternalSystem externalSystem : externalSystemRepository.findAll()) {
            headerRow
                .createCell(column++)
                .setCellValue(ApplicationMapperUtil.APPLICATION_EXTERNALID_ + externalSystem.getExternalSystemID());
            externalOrder.put(externalSystem.getExternalSystemID(), i++);
        }
        return externalOrder;
    }
}
