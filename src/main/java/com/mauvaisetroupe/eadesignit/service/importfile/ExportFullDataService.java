package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ExcelUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportFullDataService {

    public static final String SUMMARY_SHEET = "Summary";

    @Autowired
    private ApplicationExportService applicationExportService;

    @Autowired
    private LandscapeExportService landscapeExportService;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    @Autowired
    CapabilityExportService capabilityExportService;

    private static String ENTITY_TYPE = "entity.type";
    private static String SHEET_LINK = "sheet hyperlink";
    private static String LANDSCAPE_NAME = "landscape.name";

    public ByteArrayOutputStream getallData() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet summarySheet = workbook.createSheet(SUMMARY_SHEET);
        Sheet appliSheet = workbook.createSheet("Application");
        Sheet componentSheet = workbook.createSheet("Component");
        Sheet ownerSheet = workbook.createSheet("Owner");
        Sheet externalSystemSheet = workbook.createSheet("ExternalSystem");
        Sheet capabilitiesSheet = workbook.createSheet("Capabilities");

        int lineNb = 0;
        int nbcolumn = 0;
        Row headerRow = summarySheet.createRow(lineNb++);
        headerRow.createCell(nbcolumn++).setCellValue(ENTITY_TYPE);
        headerRow.createCell(nbcolumn++).setCellValue(SHEET_LINK);
        headerRow.createCell(nbcolumn++).setCellValue(LANDSCAPE_NAME);

        // Application, ApplicationComponent, Owner & ExternalSystem

        applicationExportService.writeSheets(appliSheet, componentSheet, ownerSheet, externalSystemSheet, workbook);
        addApplicationSummary(
            workbook,
            summarySheet,
            appliSheet.getSheetName(),
            componentSheet.getSheetName(),
            ownerSheet.getSheetName(),
            externalSystemSheet.getSheetName(),
            lineNb
        );
        lineNb += 4;

        // Landcsacpes

        List<LandscapeView> landscapes = landscapeViewRepository.findAll();
        int landscapeNb = 1;
        for (LandscapeView landscape : landscapes) {
            String flowSheetName = "FLW." + landscapeNb++;
            flowSheetName = flowSheetName.substring(0, Math.min(10, flowSheetName.length()));
            addSummryFlow(workbook, summarySheet, landscape, flowSheetName, lineNb++);
            Sheet flowSheet = workbook.createSheet(flowSheetName);
            landscapeExportService.writeFlows(flowSheet, landscape.getId());
            ExcelUtils.autoSizeAllColumns(flowSheet);
            ExcelUtils.addHeaderColorAndFilte(workbook, flowSheet);
            ExcelUtils.alternateColors(workbook, flowSheet, 1);
        }

        // Capabilities
        capabilityExportService.writeCapabilities(capabilitiesSheet);
        ExcelUtils.autoSizeAllColumns(capabilitiesSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, capabilitiesSheet);
        addCapabilitiesSummary(workbook, summarySheet, capabilitiesSheet.getSheetName(), lineNb);

        ExcelUtils.autoSizeAllColumns(summarySheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, summarySheet);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    private void addApplicationSummary(
        Workbook workbook,
        Sheet summarySheet,
        String appSheet,
        String componentSheet,
        String ownerSheet,
        String externalSystemSheet,
        int lineNb
    ) {
        Row row = summarySheet.createRow(lineNb++);
        int columnNb = 0;
        row.createCell(columnNb++).setCellValue("Application");
        Cell cell = row.createCell(columnNb++);
        createHyperlink(workbook, appSheet, cell);

        row = summarySheet.createRow(lineNb++);
        columnNb = 0;
        row.createCell(columnNb++).setCellValue("Application Component");
        cell = row.createCell(columnNb++);
        createHyperlink(workbook, componentSheet, cell);

        row = summarySheet.createRow(lineNb++);
        columnNb = 0;
        row.createCell(columnNb++).setCellValue("Owner");
        cell = row.createCell(columnNb++);
        createHyperlink(workbook, ownerSheet, cell);

        row = summarySheet.createRow(lineNb++);
        columnNb = 0;
        row.createCell(columnNb++).setCellValue("ExternalSystem");
        cell = row.createCell(columnNb++);
        createHyperlink(workbook, externalSystemSheet, cell);
    }

    private void addSummryFlow(Workbook workbook, Sheet summarySheet, LandscapeView landscape, String flowSheetName, int lineNb) {
        Row row = summarySheet.createRow(lineNb);
        int columnNb = 0;
        // Atrifact category
        row.createCell(columnNb++).setCellValue("Landscape");

        // Link to shet
        Cell cell = row.createCell(columnNb++);
        createHyperlink(workbook, flowSheetName, cell);

        // Landscape Name
        row.createCell(columnNb++).setCellValue(landscape.getDiagramName());
    }

    private void addCapabilitiesSummary(Workbook workbook, Sheet summarySheet, String capabilitySheetName, int lineNb) {
        Row row = summarySheet.createRow(lineNb);
        int columnNb = 0;
        row.createCell(columnNb++).setCellValue("Capabilities");
        // Link to shet
        Cell cell = row.createCell(columnNb++);
        createHyperlink(workbook, capabilitySheetName, cell);
    }

    private void createHyperlink(Workbook workbook, String sheetName, Cell cell) {
        cell.setCellValue(sheetName);
        CreationHelper helper = workbook.getCreationHelper();
        Hyperlink hyperlink = helper.createHyperlink(HyperlinkType.DOCUMENT);
        hyperlink.setAddress("'" + sheetName + "'!A1");
        cell.setHyperlink(hyperlink);
        CellStyle hlinkstyle = workbook.createCellStyle();
        Font hlinkfont = workbook.createFont();
        hlinkfont.setUnderline(Font.U_SINGLE);
        hlinkfont.setColor(IndexedColors.BLUE.index);
        hlinkstyle.setFont(hlinkfont);
        cell.setCellStyle(hlinkstyle);
    }
}
