package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.BusinessObject;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import com.mauvaisetroupe.eadesignit.repository.BusinessObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.DataObjectRepository;
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
public class BusinessAndDataObjectExportService {

    @Autowired
    private DataObjectRepository dataObjectRepository;

    @Autowired
    private BusinessObjectRepository businessObjectRepository;

    public ByteArrayOutputStream getBusinessAndDataObjects() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet appliSheet = workbook.createSheet(DataObjectImportService.DATA_OBJECT_SHEET_NAME);

        writeSheets(appliSheet, workbook);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    protected void writeSheets(Sheet dataobjectSheet, Workbook workbook) {
        writeDatObjects(dataobjectSheet);
        ExcelUtils.autoSizeAllColumns(dataobjectSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, dataobjectSheet);
    }

    protected void writeDatObjects(Sheet sheet) {
        int column = 0;
        int rownb = 0;

        Row headerRow = sheet.createRow(rownb++);
        headerRow.createCell(column++).setCellValue(DataObjectImportService.BUSINESS_OBJECT_GENERALIZATION);
        headerRow.createCell(column++).setCellValue(DataObjectImportService.BUSINESS_OBJECT);
        headerRow.createCell(column++).setCellValue(DataObjectImportService.DATA_OBJECT);
        headerRow.createCell(column++).setCellValue(DataObjectImportService.DATA_OBJECT_TYPE);
        headerRow.createCell(column++).setCellValue(DataObjectImportService.DATA_OBJECT_APPLICATION);

        List<DataObject> dataObjects = dataObjectRepository.findAllWithAllChildrens();
        for (DataObject datObject : dataObjects) {
            column = 0;
            Row row = sheet.createRow(rownb++);
            BusinessObject businessObject = datObject.getBusinessObject();
            if (businessObject != null) {
                BusinessObject generalization = businessObject.getGeneralization();
                if (generalization != null) {
                    row.createCell(column).setCellValue(generalization.getName());
                }
                column++;
                row.createCell(column).setCellValue(getFullPath(businessObject));
                column++;
            } else {
                column = column + 2;
            }
            row.createCell(column++).setCellValue(getFullPath(datObject));
            if (datObject.getType() != null) {
                row.createCell(column).setCellValue(datObject.getType().toString());
            }
            column++;
            if (datObject.getApplication() != null) {
                row.createCell(column++).setCellValue(datObject.getApplication().getName());
            }
            column++;
        }

        List<BusinessObject> orphanBusinessObjects = businessObjectRepository
            .findAll()
            .stream()
            .filter(bo -> (bo.getDataObjects() == null || bo.getDataObjects().isEmpty()))
            .toList();

        for (BusinessObject businessObject : orphanBusinessObjects) {
            Row row = sheet.createRow(rownb++);
            column = 0;
            BusinessObject generalization = businessObject.getGeneralization();
            if (generalization != null) {
                row.createCell(column).setCellValue(generalization.getName());
            }
            column++;
            row.createCell(column).setCellValue(getFullPath(businessObject));
            column++;
        }
    }

    private String getFullPath(BusinessObject businessObject) {
        StringBuilder builder = new StringBuilder();
        BusinessObject tmp = businessObject;
        builder.append(businessObject.getName());
        while (tmp.getParent() != null) {
            builder.insert(0, " > ");
            builder.insert(0, tmp.getParent().getName());
            tmp = tmp.getParent();
        }
        return builder.toString();
    }

    private String getFullPath(DataObject businessObject) {
        StringBuilder builder = new StringBuilder();
        DataObject tmp = businessObject;
        builder.append(businessObject.getName());
        while (tmp.getParent() != null) {
            builder.insert(0, " > ");
            builder.insert(0, tmp.getParent().getName());
            tmp = tmp.getParent();
        }
        return builder.toString();
    }
}
