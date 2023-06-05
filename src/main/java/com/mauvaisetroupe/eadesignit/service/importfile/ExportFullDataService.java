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

    @Autowired
    private ApplicationExportService applicationExportService;

    @Autowired
    private LandscapeExportService landscapeExportService;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    public ByteArrayOutputStream getallData() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet summarySheet = workbook.createSheet("Summary");
        Sheet appliSheet = workbook.createSheet("Application");
        Sheet componentSheet = workbook.createSheet("Component");
        Sheet ownerSheet = workbook.createSheet("Owner");

        int lineNb = 0;
        applicationExportService.writeSheets(appliSheet, componentSheet, ownerSheet, workbook);
        addApplicationSummary(
            workbook,
            summarySheet,
            appliSheet.getSheetName(),
            componentSheet.getSheetName(),
            ownerSheet.getSheetName(),
            lineNb
        );
        lineNb += 3;

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
        ExcelUtils.autoSizeAllColumns(summarySheet);

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
        int lineNb
    ) {
        Row row = summarySheet.createRow(lineNb++);
        int columnNb = 0;
        row.createCell(columnNb++).setCellValue("Application");
        row.createCell(columnNb++).setCellValue("Application List");
        Cell cell = row.createCell(columnNb++);
        createHyperlink(workbook, appSheet, cell);

        row = summarySheet.createRow(lineNb++);
        columnNb = 0;
        row.createCell(columnNb++).setCellValue("Application Component");
        row.createCell(columnNb++).setCellValue("Application component List");
        cell = row.createCell(columnNb++);
        createHyperlink(workbook, componentSheet, cell);

        row = summarySheet.createRow(lineNb++);
        columnNb = 0;
        row.createCell(columnNb++).setCellValue("Owner");
        row.createCell(columnNb++).setCellValue("Owner List");
        cell = row.createCell(columnNb++);
        createHyperlink(workbook, ownerSheet, cell);
    }

    private void addSummryFlow(Workbook workbook, Sheet summarySheet, LandscapeView landscape, String flowSheetName, int lineNb) {
        Row row = summarySheet.createRow(lineNb);
        int columnNb = 0;
        // Atrifact category
        row.createCell(columnNb++).setCellValue("Landscape");

        // Landscape Name
        row.createCell(columnNb++).setCellValue(landscape.getDiagramName());

        // Link to shet
        Cell cell = row.createCell(columnNb++);
        createHyperlink(workbook, flowSheetName, cell);
    }

    private void createHyperlink(Workbook workbook, String flowSheetName, Cell cell) {
        cell.setCellValue(flowSheetName);
        CreationHelper helper = workbook.getCreationHelper();
        Hyperlink hyperlink = helper.createHyperlink(HyperlinkType.DOCUMENT);
        hyperlink.setAddress("'" + flowSheetName + "'!A1");
        cell.setHyperlink(hyperlink);
        CellStyle hlinkstyle = workbook.createCellStyle();
        Font hlinkfont = workbook.createFont();
        hlinkfont.setUnderline(Font.U_SINGLE);
        hlinkfont.setColor(IndexedColors.BLUE.index);
        hlinkstyle.setFont(hlinkfont);
        cell.setCellStyle(hlinkstyle);
    }
}
