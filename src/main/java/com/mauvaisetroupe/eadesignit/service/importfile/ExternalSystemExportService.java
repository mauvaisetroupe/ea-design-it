package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.ExternalSystem;
import com.mauvaisetroupe.eadesignit.repository.ExternalSystemRepository;
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
public class ExternalSystemExportService {

    @Autowired
    private ExternalSystemRepository externalSystemRepository;

    public ByteArrayOutputStream getExternalSystems() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet externalSystemSheet = workbook.createSheet(ExternalSystemImportService.SHEET_NAME);

        writeExternalSytemSheet(externalSystemSheet, workbook);

        ExcelUtils.autoSizeAllColumns(externalSystemSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, externalSystemSheet);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    protected void writeExternalSytemSheet(Sheet sheet, Workbook workbook) {
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);
        headerRow.createCell(column++).setCellValue(ExternalSystemImportService.EXTERNL_ID);

        List<ExternalSystem> externalSystems = externalSystemRepository.findAll();
        for (ExternalSystem externalSystem : externalSystems) {
            column = 0;
            Row row = sheet.createRow(rownb++);
            row.createCell(column++).setCellValue(externalSystem.getExternalSystemID());
        }
        ExcelUtils.autoSizeAllColumns(sheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, sheet);
    }
}
