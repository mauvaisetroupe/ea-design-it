package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ProtocolType;
import com.mauvaisetroupe.eadesignit.repository.ApplicationComponentRepository;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ExcelUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
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
public class LandscapeExportService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationComponentRepository applicationComponentRepository;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    public ByteArrayOutputStream getLandscapeExcel(Long landscapeId) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet appliSheet = workbook.createSheet("Application");
        Sheet componentSheet = workbook.createSheet("Component");
        Sheet flowSheet = workbook.createSheet("Message_Flow");

        writeFlows(flowSheet, landscapeId);
        ExcelUtils.autoSizeAllColumns(flowSheet);
        ExcelUtils.addHeaderColorAndFilte(workbook, flowSheet);
        writeApplication(appliSheet);
        ExcelUtils.autoSizeAllColumns(appliSheet);
        writeComponent(componentSheet);
        ExcelUtils.autoSizeAllColumns(componentSheet);

        ExcelUtils.alternateColors(workbook, flowSheet, 1);
        workbook.setActiveSheet(2);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    protected void writeFlows(Sheet sheet, Long landscapeId) {
        LandscapeView landscapeView = landscapeViewRepository.getById(landscapeId);
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_ID_FLOW);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_ALIAS_FLOW);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_SOURCE_ELEMENT);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_TARGET_ELEMENT);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_DESCRIPTION);
        Cell stepNumberHeader = headerRow.createCell(column++);
        stepNumberHeader.setCellValue(FlowImportService.FLOW_STEP_NUMBER);
        addComment(sheet, stepNumberHeader, "Not used during import process, only display helper");
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_STEP_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_GROUP_FLOW_ALIAS);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_GROUP_ORDER);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_GROUP_TITLE);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_GROUP_URL);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_INTEGRATION_PATTERN);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_BLUEPRINT_SOURCE);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_BLUEPRINT_TARGET);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_COMMENT);

        for (FunctionalFlow flow : landscapeView.getFlows()) {
            int currentGroupOrder = 1;
            for (FunctionalFlowStep step : flow.getSteps()) {
                FlowInterface interface1 = step.getFlowInterface();
                Row row = sheet.createRow(rownb++);
                column = 0;
                row.createCell(column++).setCellValue(interface1.getAlias());
                row.createCell(column++).setCellValue(flow.getAlias());
                if (interface1.getSourceComponent() != null) {
                    row.createCell(column++).setCellValue(interface1.getSourceComponent().getName());
                } else {
                    row.createCell(column++).setCellValue(interface1.getSource().getName());
                }
                if (interface1.getTargetComponent() != null) {
                    row.createCell(column++).setCellValue(interface1.getTargetComponent().getName());
                } else {
                    row.createCell(column++).setCellValue(interface1.getTarget().getName());
                }
                row.createCell(column++).setCellValue(flow.getDescription());
                if (step.getStepOrder() != null) {
                    row.createCell(column).setCellValue(step.getStepOrder());
                }
                column++;
                row.createCell(column++).setCellValue(step.getDescription());

                if (step.getGroup() != null) {
                    if (step.getGroup().getFlow() != null) {
                        row.createCell(column).setCellValue(step.getGroup().getFlow().getAlias());
                    }
                    column++;
                    row.createCell(column++).setCellValue(currentGroupOrder++);
                    row.createCell(column++).setCellValue(step.getGroup().getTitle());
                    row.createCell(column++).setCellValue(step.getGroup().getUrl());
                } else {
                    currentGroupOrder = 1;
                    column = column + 4;
                }

                row.createCell(column++).setCellValue(interface1.getProtocol() != null ? interface1.getProtocol().getName() : "");
                int nbDataFlows = (interface1.getDataFlows() == null) ? 0 : interface1.getDataFlows().size();
                switch (nbDataFlows) {
                    case 0:
                        column = column + 3;
                        break;
                    case 1:
                        DataFlow dataFlow = interface1.getDataFlows().iterator().next();
                        row.createCell(column++).setCellValue(dataFlow.getFrequency() != null ? dataFlow.getFrequency().toString() : "");
                        row.createCell(column++).setCellValue(dataFlow.getFormat() != null ? dataFlow.getFormat().getName() : "");
                        if (interface1.getProtocol() != null && interface1.getProtocol().getType().equals(ProtocolType.API)) {
                            row.createCell(column++).setCellValue(dataFlow.getContractURL());
                        } else {
                            row.createCell(column++).setCellValue("N/A");
                        }
                        break;
                    default:
                        row.createCell(column++).setCellValue("multiple");
                        row.createCell(column++).setCellValue("multiple");
                        row.createCell(column++).setCellValue("multiple");
                        break;
                }
                row.createCell(column++).setCellValue(flow.getDocumentationURL());
                row.createCell(column++).setCellValue(flow.getDocumentationURL2());
                row.createCell(column++).setCellValue(flow.getComment());
            }
        }
        // Add conditional formatting if Application doesn't exist
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule(
            "COUNTIF(Application!$B$2:$B$1000,c2) + COUNTIF(Component!$B$2:$B$1000,c2)<=0"
        );
        FontFormatting fontFmt = rule.createFontFormatting();
        fontFmt.setFontStyle(false, true);
        fontFmt.setFontColorIndex(IndexedColors.RED.index);
        ConditionalFormattingRule[] cfRules = new ConditionalFormattingRule[] { rule };
        CellRangeAddress[] regions = new CellRangeAddress[] { CellRangeAddress.valueOf("C2:D200") };
        sheetCF.addConditionalFormatting(regions, cfRules);
    }

    public void addComment(Sheet sheet, Cell cell, String commentText) {
        CreationHelper factory = sheet.getWorkbook().getCreationHelper();
        //get an existing cell or create it otherwise:

        ClientAnchor anchor = factory.createClientAnchor();
        //i found it useful to show the comment box at the bottom right corner
        // anchor.setCol1(cell.getColumnIndex() + 1); //the box of the comment starts at this given column...
        // anchor.setCol2(cell.getColumnIndex() + 3); //...and ends at that given column
        // anchor.setRow1(cell.getRowIndex() + 1); //one row below the cell...
        // anchor.setRow2(cell.getRowIndex() + 5); //...and 4 rows high

        Drawing drawing = sheet.createDrawingPatriarch();
        Comment comment = drawing.createCellComment(anchor);
        //set the comment text and author
        comment.setString(factory.createRichTextString(commentText));
        cell.setCellComment(comment);
    }

    private void writeApplication(Sheet sheet) {
        List<Application> applications = applicationRepository.findAll();
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);
        headerRow.createCell(column++).setCellValue("application.id");
        headerRow.createCell(column++).setCellValue("application.name");

        for (Application application : applications) {
            column = 0;
            Row row = sheet.createRow(rownb++);
            row.createCell(column++).setCellValue(application.getAlias());
            row.createCell(column++).setCellValue(application.getName());
        }
    }

    private void writeComponent(Sheet sheet) {
        List<ApplicationComponent> components = applicationComponentRepository.findAll();
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);
        headerRow.createCell(column++).setCellValue("component.id");
        headerRow.createCell(column++).setCellValue("component.name");
        headerRow.createCell(column++).setCellValue("application.name");

        for (ApplicationComponent component : components) {
            column = 0;
            Row row = sheet.createRow(rownb++);
            row.createCell(column++).setCellValue(component.getAlias());
            row.createCell(column++).setCellValue(component.getName());
            row.createCell(column++).setCellValue(component.getApplication().getName());
        }
    }
}
