package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.repository.ApplicationComponentRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ExcelUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CapabilityExportService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationComponentRepository applicationComponentRepository;

    @Autowired
    CapabilityRepository capabilityRepository;

    public ByteArrayOutputStream getCapabilities() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet capabilitieSheet = workbook.createSheet("Capabilities");

        writeCapabilities(capabilitieSheet);
        ExcelUtils.autoSizeAllColumns(capabilitieSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, capabilitieSheet);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    protected void writeCapabilities(Sheet sheet) {
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.SUR_DOMAIN);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.L0_NAME);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.L0_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.L1_NAME);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.L1_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.L2_NAME);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.L2_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.L3_NAME);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.L3_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.FULL_PATH);

        if (capabilityRepository.count() > 0) {
            List<Capability> roots = capabilityRepository.findByLevel(-2);
            Assert.isTrue(roots.size() == 1, "Should exist and be unique");

            writeRow(roots.get(0), sheet, 0);
        }
    }

    private int writeRow(Capability capability, Sheet sheet, int rowNb) {
        if (capability.getLevel() >= 0) {
            rowNb = rowNb + 1;
            Row row = sheet.createRow(rowNb);
            Capability tmCapability = capability;
            while (tmCapability != null) {
                if (tmCapability.getLevel() > -1) {
                    int nbColumn = 1 + tmCapability.getLevel() * 2;
                    row.createCell(nbColumn++).setCellValue(tmCapability.getName());
                    row.createCell(nbColumn++).setCellValue(tmCapability.getDescription());
                } else if (tmCapability.getLevel() == -1) {
                    row.createCell(0).setCellValue(tmCapability.getName());
                }
                tmCapability = tmCapability.getParent();
            }
            Cell path = row.createCell(9);

            StringBuffer formula = new StringBuffer();
            int currentRow = rowNb + 1;
            formula.append("CONCAT(A" + currentRow + ",\" / \",B" + currentRow + ",");
            formula.append("IF(NOT(ISBLANK(D" + currentRow + ")),CONCAT(\" > \",D" + currentRow + "),\"\"),");
            formula.append("IF(NOT(ISBLANK(F" + currentRow + ")),CONCAT(\" > \",F" + currentRow + "),\"\"),");
            formula.append("IF(NOT(ISBLANK(H" + currentRow + ")),CONCAT(\" > \",H" + currentRow + "),\"\")");
            formula.append(")");
            path.setCellFormula(formula.toString());
        }
        for (Capability subCapability : capability.getSubCapabilities()) {
            rowNb = writeRow(subCapability, sheet, rowNb++);
        }
        return rowNb;
    }
}
