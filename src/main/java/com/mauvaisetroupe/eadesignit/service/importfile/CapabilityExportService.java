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
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CapabilityExportService {

    private static final int LAST_CELL = 9;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationComponentRepository applicationComponentRepository;

    @Autowired
    CapabilityRepository capabilityRepository;

    public ByteArrayOutputStream getCapabilities() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet capabilitieSheet = workbook.createSheet(CapabilityImportService.CAPABILITY_SHEET_NAME);

        writeCapabilities(capabilitieSheet, workbook);
        ExcelUtils.autoSizeAllColumns(capabilitieSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, capabilitieSheet);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    protected void writeCapabilities(Sheet sheet, Workbook workbook) {
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

            writeRow(roots.get(0), sheet, 0, workbook);
        }
    }

    private int writeRow(Capability capability, Sheet sheet, int rowNb, Workbook workbook) {
        if (capability.getLevel() >= 0) {
            rowNb = rowNb + 1;
            Row row = sheet.createRow(rowNb);
            Cell formulaCell = row.createCell(LAST_CELL);
            formulaCell.setCellFormula(createRule(rowNb + 1));
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
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
            formulaEvaluator.evaluateFormulaCell(formulaCell);
        }
        for (Capability subCapability : capability.getSubCapabilities()) {
            rowNb = writeRow(subCapability, sheet, rowNb++, workbook);
        }
        return rowNb;
    }

    private String createRule(int rowwNumber) {
        StringBuffer buffer = new StringBuffer();
        String CHEVRON = "\" > \"";
        String SEP = ",";
        buffer
            .append("CONCATENATE(")
            .append("A")
            .append(rowwNumber)
            .append(SEP)
            .append(CHEVRON)
            .append(SEP)
            .append("B" + rowwNumber)
            .append(SEP)
            .append(ifNotBlank("D" + rowwNumber))
            .append(SEP)
            .append(ifNotBlank("F" + rowwNumber))
            .append(SEP)
            .append(ifNotBlank("H" + rowwNumber))
            .append(")");

        return buffer.toString();
    }

    private String ifNotBlank(String cell) {
        String CHEVRON = "\" > \"";
        String SEP = ",";
        String DOUBLEQUOTE = "\"\"";
        return "IF(NOT(ISBLANK(" + cell + "))" + SEP + "CONCATENATE(" + CHEVRON + SEP + cell + ")" + SEP + DOUBLEQUOTE + ")";
    }
}
