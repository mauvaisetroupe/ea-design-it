package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ProtocolType;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
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
public class LandscapeExportService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    public ByteArrayOutputStream getLandscapeExcel(Long landscapeId) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Application");
        Sheet sheetflow = workbook.createSheet("Message_Flow");

        writeFlows(sheetflow, landscapeId);
        writeApplication(sheet);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();
        return stream;
    }

    private void writeFlows(Sheet sheet, Long landscapeId) {
        LandscapeView landscapeView = landscapeViewRepository.getById(landscapeId);
        int column = 0;
        int rownb = 0;
        Row headerRow = sheet.createRow(rownb++);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_ID_FLOW);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_ALIAS_FLOW);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_SOURCE_ELEMENT);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_TARGET_ELEMENT);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_STEP_DESCRIPTION);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_INTEGRATION_PATTERN);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_FREQUENCY);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_FORMAT);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_SWAGGER);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_BLUEPRINT_SOURCE);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_BLUEPRINT_TARGET);
        headerRow.createCell(column++).setCellValue(FlowImportService.FLOW_COMMENT);

        for (FunctionalFlow flow : landscapeView.getFlows()) {
            for (FunctionalFlowStep step : flow.getSteps()) {
                FlowInterface interface1 = step.getFlowInterface();
                Row row = sheet.createRow(rownb++);
                column = 0;
                row.createCell(column++).setCellValue(interface1.getAlias());
                row.createCell(column++).setCellValue(flow.getAlias());
                row.createCell(column++).setCellValue(interface1.getSource().getName());
                row.createCell(column++).setCellValue(interface1.getTarget().getName());
                row.createCell(column++).setCellValue(flow.getDescription());
                row.createCell(column++).setCellValue(step.getDescription());
                row.createCell(column++).setCellValue(interface1.getProtocol() != null ? interface1.getProtocol().getName() : "");
                if (interface1.getDataFlows() != null && interface1.getDataFlows().size() == 1) {
                    DataFlow dataFlow = interface1.getDataFlows().iterator().next();
                    row.createCell(column++).setCellValue(dataFlow.getFrequency() != null ? dataFlow.getFrequency().toString() : "");
                    row.createCell(column++).setCellValue(dataFlow.getFormat() != null ? dataFlow.getFormat().getName() : "");
                    if (interface1.getProtocol() != null && interface1.getProtocol().getType().equals(ProtocolType.API)) {
                        row.createCell(column++).setCellValue(dataFlow.getContractURL());
                    } else {
                        row.createCell(column++).setCellValue("N/A");
                    }
                } else {
                    row.createCell(column++).setCellValue("multiple");
                    row.createCell(column++).setCellValue("multiple");
                    row.createCell(column++).setCellValue("multiple");
                }
                row.createCell(column++).setCellValue(interface1.getDocumentationURL());
                row.createCell(column++).setCellValue(interface1.getDocumentationURL2());
                row.createCell(column++).setCellValue(flow.getComment());
            }
        }
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
}
