package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.util.CapabilityMappingDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.util.ExcelUtils;
import com.mauvaisetroupe.eadesignit.service.importfile.util.SummaryImporterService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
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

    @Autowired
    CapabilityExportService capabilityExportService;

    @Autowired
    ExternalSystemExportService externalSystemExportService;

    @Autowired
    ApplicationCapabilityExportService capabilityMappingExportService;

    @Autowired
    FunctionalFlowRepository flowRepository;

    @Autowired
    BusinessAndDataObjectExportService businessAndDataObjectExportService;

    public ByteArrayOutputStream getallData(
        boolean exportApplications,
        boolean exportComponents,
        boolean exportOwner,
        boolean exportExternalSystem,
        boolean exportCapabilities,
        List<Long> landscapesToExport,
        List<Long> capabilitiesMappingToExport,
        boolean exportCapabilitiesWithNoLandscape,
        boolean functionalFlowsWhithNoLandscape,
        boolean businessAndDataObjects
    ) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet summarySheet = workbook.createSheet(SummaryImporterService.SUMMARY_SHEET);

        Sheet appliSheet = null;
        Sheet componentSheet = null;
        Sheet ownerSheet = null;
        Sheet externalSystemSheet = null;

        int lineNb = 0;
        int nbcolumn = 0;
        Row headerRow = summarySheet.createRow(lineNb++);
        headerRow.createCell(nbcolumn++).setCellValue(SummaryImporterService.ENTITY_TYPE);
        headerRow.createCell(nbcolumn++).setCellValue(SummaryImporterService.SHEET_LINK);
        headerRow.createCell(nbcolumn++).setCellValue(SummaryImporterService.LANDSCAPE_NAME);
        headerRow.createCell(nbcolumn++).setCellValue(SummaryImporterService.OWNER);

        // External Systems
        if (exportExternalSystem) {
            externalSystemSheet = workbook.createSheet(ExternalSystemImportService.SHEET_NAME);
            externalSystemExportService.writeExternalSytemSheet(externalSystemSheet, workbook);
            ExcelUtils.autoSizeAllColumns(externalSystemSheet);
            ExcelUtils.addHeaderColorAndFilte(workbook, externalSystemSheet);
        }

        // Application, ApplicationComponent, Owner & ExternalSystem
        if (exportApplications) {
            appliSheet = workbook.createSheet(ApplicationImportService.APPLICATION_SHEET_NAME);
            applicationExportService.writeApplication(appliSheet);
            ExcelUtils.autoSizeAllColumns(appliSheet);
            ExcelUtils.addHeaderColorAndFilte(workbook, appliSheet);
        }
        if (exportComponents) {
            componentSheet = workbook.createSheet(ComponentImportService.COMPONENT_SHEET_NAME);
            applicationExportService.writeComponent(componentSheet);
            ExcelUtils.autoSizeAllColumns(componentSheet);
            ExcelUtils.addHeaderColorAndFilte(workbook, componentSheet);
        }
        if (exportOwner) {
            ownerSheet = workbook.createSheet(ApplicationImportService.OWNER_SHEET_NAME);
            applicationExportService.writeOwner(ownerSheet);
            ExcelUtils.autoSizeAllColumns(ownerSheet);
            ExcelUtils.addHeaderColorAndFilte(workbook, ownerSheet);
        }
        addApplicationSummary(workbook, summarySheet, appliSheet, componentSheet, ownerSheet, externalSystemSheet, lineNb);
        lineNb += 4;

        // Landcsacpes

        List<LandscapeView> landscapes = landscapeViewRepository.findAll();
        int landscapeNb = 1;
        for (LandscapeView landscape : landscapes) {
            if (landscapesToExport.contains(landscape.getId())) {
                String flowSheetName = "FLW." + landscapeNb++;
                flowSheetName = flowSheetName.substring(0, Math.min(10, flowSheetName.length()));
                addSummryFlow(workbook, summarySheet, landscape, flowSheetName, lineNb++);
                Sheet flowSheet = workbook.createSheet(flowSheetName);
                landscapeExportService.writeFlows(flowSheet, landscape.getId());
                ExcelUtils.autoSizeAllColumns(flowSheet);
                ExcelUtils.addHeaderColorAndFilte(workbook, flowSheet);
                ExcelUtils.alternateColors(workbook, flowSheet, 1);
            }
        }

        // Orphan flows
        if (functionalFlowsWhithNoLandscape) {
            Set<FunctionalFlow> flows = flowRepository.findByLandscapesIsEmpty();
            if (flows != null && !flows.isEmpty()) {
                Sheet flowSheet = workbook.createSheet("FLW.ORPH");
                landscapeExportService.writeOrphanFlows(flowSheet, flows);
                addSummryFlow(workbook, summarySheet, null, "FLW.ORPH", lineNb++);
                ExcelUtils.autoSizeAllColumns(flowSheet);
                ExcelUtils.addHeaderColorAndFilte(workbook, flowSheet);
                ExcelUtils.alternateColors(workbook, flowSheet, 1);
            }
        }

        // Capabilities
        if (exportCapabilities) {
            Sheet capabilitiesSheet = workbook.createSheet(CapabilityImportService.CAPABILITY_SHEET_NAME);
            capabilityExportService.writeCapabilities(capabilitiesSheet, workbook);
            ExcelUtils.autoSizeAllColumns(capabilitiesSheet);
            ExcelUtils.addHeaderColorAndFilte(workbook, capabilitiesSheet);
            addCapabilitiesSummary(workbook, summarySheet, capabilitiesSheet.getSheetName(), lineNb++);
        }

        // CapabilityMapping
        List<CapabilityMappingDTO> capabilityMappingDTOs = capabilityMappingExportService.writeApplicationCapabilitiesMapping(
            workbook,
            capabilitiesMappingToExport,
            exportCapabilitiesWithNoLandscape
        );
        int nbmappings = addCapabilitieMappingsSummary(workbook, summarySheet, capabilityMappingDTOs, lineNb);
        lineNb = lineNb + nbmappings;

        // Business and Data Objects
        if (businessAndDataObjects) {
            Sheet businessObjectsSheet = workbook.createSheet(DataObjectImportService.DATA_OBJECT_SHEET_NAME);
            addBusinessAndDataObjectsSummary(workbook, summarySheet, businessObjectsSheet.getSheetName(), lineNb);
            businessAndDataObjectExportService.writeDatObjects(businessObjectsSheet);
            ExcelUtils.autoSizeAllColumns(businessObjectsSheet);
            ExcelUtils.addHeaderColorAndFilte(workbook, businessObjectsSheet);
            // find business bobjects with no data object
            //businessObjectRepository.findAllWithAllChildrens();

        }

        // Close stream
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
        Sheet appSheet,
        Sheet componentSheet,
        Sheet ownerSheet,
        Sheet externalSystemSheet,
        int lineNb
    ) {
        int columnNb = 0;
        if (appSheet != null) {
            Row row = summarySheet.createRow(lineNb++);
            row.createCell(columnNb++).setCellValue("Application");
            Cell cell = row.createCell(columnNb++);
            createHyperlink(workbook, appSheet.getSheetName(), cell);
        }

        if (componentSheet != null) {
            Row row = summarySheet.createRow(lineNb++);
            columnNb = 0;
            row.createCell(columnNb++).setCellValue("Application Component");
            Cell cell = row.createCell(columnNb++);
            createHyperlink(workbook, componentSheet.getSheetName(), cell);
        }

        if (ownerSheet != null) {
            Row row = summarySheet.createRow(lineNb++);
            columnNb = 0;
            row.createCell(columnNb++).setCellValue("Owner");
            Cell cell = row.createCell(columnNb++);
            createHyperlink(workbook, ownerSheet.getSheetName(), cell);
        }

        if (externalSystemSheet != null) {
            Row row = summarySheet.createRow(lineNb++);
            columnNb = 0;
            row.createCell(columnNb++).setCellValue("ExternalSystem");
            Cell cell = row.createCell(columnNb++);
            createHyperlink(workbook, externalSystemSheet.getSheetName(), cell);
        }
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
        if (landscape != null) { // for orphan flows
            row.createCell(columnNb++).setCellValue(landscape.getDiagramName());
            // Landscape Owner
            if (landscape.getOwner() != null) {
                row.createCell(columnNb++).setCellValue(landscape.getOwner().getName());
            }
        } else {
            columnNb++;
            columnNb++;
        }
    }

    private void addCapabilitiesSummary(Workbook workbook, Sheet summarySheet, String capabilitySheetName, int lineNb) {
        Row row = summarySheet.createRow(lineNb);
        int columnNb = 0;
        row.createCell(columnNb++).setCellValue("Capabilities");
        // Link to shet
        Cell cell = row.createCell(columnNb++);
        createHyperlink(workbook, capabilitySheetName, cell);
    }

    private int addCapabilitieMappingsSummary(
        Workbook workbook,
        Sheet summarySheet,
        List<CapabilityMappingDTO> capabilityMappingDTOs,
        int lineNb
    ) {
        for (CapabilityMappingDTO capabilityMappingDTO : capabilityMappingDTOs) {
            Row row = summarySheet.createRow(lineNb++);
            int columnNb = 0;
            row.createCell(columnNb++).setCellValue("Capability Mapping");
            Cell cell = row.createCell(columnNb++);
            createHyperlink(workbook, capabilityMappingDTO.getSheetName(), cell);
            row.createCell(columnNb++).setCellValue(capabilityMappingDTO.getLandscape());
        }
        return capabilityMappingDTOs == null ? 0 : capabilityMappingDTOs.size();
    }

    private void addBusinessAndDataObjectsSummary(Workbook workbook, Sheet summarySheet, String businessSheetName, int lineNb) {
        Row row = summarySheet.createRow(lineNb);
        int columnNb = 0;
        row.createCell(columnNb++).setCellValue("Business and Data Objects");
        // Link to shet
        Cell cell = row.createCell(columnNb++);
        createHyperlink(workbook, businessSheetName, cell);
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
