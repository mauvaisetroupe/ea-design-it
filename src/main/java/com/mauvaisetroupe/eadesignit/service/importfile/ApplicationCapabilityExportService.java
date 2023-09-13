package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.CapabilityApplicationMappingRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.CapabilityMappingDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ExcelUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationCapabilityExportService {

    private static final String NO_LANDSCAPE = "NO.LANDSCAPE";

    @Autowired
    private CapabilityApplicationMappingRepository capabilityApplicationMappingRepository;

    public ByteArrayOutputStream getApplicationCapabilitiesMapping() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        writeApplicationCapabilitiesMapping(workbook);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    protected List<CapabilityMappingDTO> writeApplicationCapabilitiesMapping(Workbook workbook) {
        List<CapabilityMappingDTO> capabilityMappingDTOs = new ArrayList<>();

        Map<String, Set<CapabilityApplicationMapping>> mappingByLandscape = new HashMap<>();
        List<CapabilityApplicationMapping> mappings = capabilityApplicationMappingRepository.findAllWithEagerRelationships();
        for (CapabilityApplicationMapping mapping : mappings) {
            Set<LandscapeView> landscapes = mapping.getLandscapes();
            if (landscapes == null || landscapes.isEmpty()) {
                addCapabilityMappingToMap(mappingByLandscape, mapping, NO_LANDSCAPE);
            } else {
                for (LandscapeView landscape : landscapes) {
                    addCapabilityMappingToMap(mappingByLandscape, mapping, landscape.getDiagramName());
                }
            }
        }

        int nblandscape = 1;
        for (String landscapeName : mappingByLandscape.keySet()) {
            String sheetname = "CPB." + nblandscape++;
            Sheet capabilitieSheet = workbook.createSheet(sheetname);
            capabilityMappingDTOs.add(new CapabilityMappingDTO(landscapeName, sheetname));

            writeApplicationCapabilitiesMapping(workbook, capabilitieSheet, mappingByLandscape.get(landscapeName));

            writeApplicationConditionalFormatting(capabilitieSheet);

            ExcelUtils.autoSizeAllColumns(capabilitieSheet);
            ExcelUtils.addHeaderColorAndFilte(workbook, capabilitieSheet);
        }

        return capabilityMappingDTOs;
    }

    private void writeApplicationConditionalFormatting(Sheet sheet) {
        // Add conditional formatting if Application doesn't exist
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule ruleForapplication = sheetCF.createConditionalFormattingRule(
            "COUNTIF(Application!$B$2:$B$1000,A2) + COUNTIF(Component!$B$2:$B$1000,A2)<=0"
        );
        ConditionalFormattingRule ruleForCapabilities = sheetCF.createConditionalFormattingRule("COUNTIF(Capabilities!$J$2:$J$2000,B2)<=0");
        FontFormatting fontFmt = ruleForapplication.createFontFormatting();
        fontFmt.setFontStyle(false, true);
        fontFmt.setFontColorIndex(IndexedColors.RED.index);

        ConditionalFormattingRule[] cfRulesForapplications = new ConditionalFormattingRule[] { ruleForapplication };
        CellRangeAddress[] regionsForApplications = new CellRangeAddress[] { CellRangeAddress.valueOf("A2:A200") };

        FontFormatting fontFmt2 = ruleForCapabilities.createFontFormatting();
        fontFmt2.setFontStyle(false, true);
        fontFmt2.setFontColorIndex(IndexedColors.RED.index);

        ConditionalFormattingRule[] cfRulesForCapabilities = new ConditionalFormattingRule[] { ruleForCapabilities };
        CellRangeAddress[] regionsForCapabilities = new CellRangeAddress[] { CellRangeAddress.valueOf("B2:B200") };

        sheetCF.addConditionalFormatting(regionsForApplications, cfRulesForapplications);
        sheetCF.addConditionalFormatting(regionsForCapabilities, cfRulesForCapabilities);
    }

    private void writeApplicationCapabilitiesMapping(Workbook workbook, Sheet sheet, Set<CapabilityApplicationMapping> set) {
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);
        headerRow.createCell(column++).setCellValue(ApplicationCapabilityImportService.APP_NAME_1);
        headerRow.createCell(column++).setCellValue(CapabilityImportService.FULL_PATH);

        for (CapabilityApplicationMapping applicationMapping : set) {
            Row row = sheet.createRow(rownb++);
            row.createCell(0).setCellValue(applicationMapping.getApplication().getName());
            row.createCell(1).setCellValue(getCapabilityFullPath(applicationMapping.getCapability()));
        }
    }

    private void addCapabilityMappingToMap(
        Map<String, Set<CapabilityApplicationMapping>> mappingByLandscape,
        CapabilityApplicationMapping mapping,
        String key
    ) {
        Set<CapabilityApplicationMapping> tmpSet = mappingByLandscape.get(key);
        tmpSet = (tmpSet == null) ? new HashSet<>() : tmpSet;
        tmpSet.add(mapping);
        mappingByLandscape.put(key, tmpSet);
    }

    private String getCapabilityFullPath(Capability capability) {
        StringBuffer buffer = new StringBuffer();
        String sep = "";
        while (capability.getParent() != null) {
            buffer.insert(0, sep).insert(0, capability.getName());
            capability = capability.getParent();
            sep = " > ";
        }
        return buffer.toString();
    }
}
