package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import com.mauvaisetroupe.eadesignit.domain.ExternalSystem;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.ApplicationCapabilityImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.ApplicationExportService;
import com.mauvaisetroupe.eadesignit.service.importfile.ApplicationImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.CapabilityImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.ComponentImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.DataFlowImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.DataObjectImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.ExcelReader;
import com.mauvaisetroupe.eadesignit.service.importfile.ExportFullDataService;
import com.mauvaisetroupe.eadesignit.service.importfile.ExternalSystemImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.FlowImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.LandscapeExportService;
import com.mauvaisetroupe.eadesignit.service.importfile.PlantumlImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ApplicationCapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ApplicationCapabilityItemDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportAnalysisDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.DataObjectDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.FlowImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.SummarySheetDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.util.SummaryImporterService;
import com.mauvaisetroupe.eadesignit.web.rest.errors.ApplicationImportException;
import com.mauvaisetroupe.eadesignit.web.rest.errors.BadRequestAlertException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.ApplicationImport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ImportResource {

    @Autowired
    private ApplicationImportService applicationImportService;

    @Autowired
    private ApplicationExportService applicationExportService;

    @Autowired
    private ExportFullDataService exportFullDataService;

    @Autowired
    private ComponentImportService componentImportService;

    @Autowired
    private FlowImportService flowImportService;

    @Autowired
    private DataFlowImportService dataFlowImportService;

    @Autowired
    private CapabilityImportService capabilityImportService;

    @Autowired
    private ApplicationCapabilityImportService applicationCapabilityImportService;

    @Autowired
    private LandscapeExportService landscapeExportService;

    @Autowired
    private LandscapeViewRepository landscapeViewRepository;

    @Autowired
    private PlantumlImportService plantumlImportService;

    @Autowired
    private ExternalSystemImportService externalSystemImportService;

    @Autowired
    private SummaryImporterService summaryImporterService;

    @Autowired
    private DataObjectImportService dataObjectImportService;

    @PostMapping("/import/sheetnames")
    public List<String> getSheetNames(@RequestPart MultipartFile file) throws Exception {
        ExcelReader excelReader = new ExcelReader(file.getInputStream());
        return excelReader.getSheetNames();
    }

    @PostMapping("/import/summary")
    public List<SummarySheetDTO> getSummary(@RequestPart MultipartFile file) throws Exception {
        return summaryImporterService.getSummary(file);
    }

    @PostMapping("/import/application/upload-file")
    public List<ApplicationImport> uploadFile(@RequestPart MultipartFile file) throws Exception {
        try {
            return applicationImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationImportException(e.getMessage());
        }
    }

    @PostMapping("/import/component/upload-file")
    public List<ApplicationImport> uploadComponentFile(@RequestPart MultipartFile file) throws Exception {
        try {
            return componentImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationImportException(e.getMessage());
        }
    }

    @PostMapping("/import/flow/upload-files")
    public List<FlowImportDTO> uploadFlowsFile(@RequestPart MultipartFile[] files) throws Exception {
        List<FlowImportDTO> dtos = new ArrayList<>();
        for (MultipartFile file : files) {
            List<FlowImport> flowImports = flowImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
            dtos.add(new FlowImportDTO(file.getOriginalFilename(), flowImports));
        }
        return dtos;
    }

    @PostMapping("/import/flow/upload-multi-file")
    public List<FlowImportDTO> uploadFlowsMultiFile(@RequestPart MultipartFile file, @RequestParam String[] sheetnames) throws Exception {
        List<FlowImportDTO> dtos = new ArrayList<>();
        for (String sheet : sheetnames) {
            List<FlowImport> flowImports = flowImportService.importExcelWithMultiFLWSheets(file.getInputStream(), sheet);
            dtos.add(new FlowImportDTO(sheet, flowImports));
        }
        return dtos;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @PostMapping("/import/flow/sequence-diagram/pre-import")
    public com.mauvaisetroupe.eadesignit.service.dto.FlowImport uploadPlantuml(@RequestBody String plantumlSource) throws Exception {
        return plantumlImportService.importPlantuml(plantumlSource);
    }

    @PostMapping("/import/flow/sequence-diagram/save")
    public FunctionalFlow saveImport(
        @RequestBody com.mauvaisetroupe.eadesignit.service.dto.FlowImport flowImport,
        @RequestParam(required = false) Long landscapeId
    ) throws Exception {
        return plantumlImportService.saveImport(flowImport, landscapeId);
    }

    @PostMapping("/import/data-flow/upload-file")
    public List<DataFlowImport> uploadDataFlowFile(@RequestPart MultipartFile file) throws Exception {
        return dataFlowImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
    }

    @PostMapping("/import/capability/upload-file")
    public CapabilityImportAnalysisDTO uploadCapabilityFile(@RequestPart MultipartFile file) throws Exception {
        return capabilityImportService.analyzeExcel(file.getInputStream(), file.getOriginalFilename());
    }

    @PostMapping("/import/capability/confirm-uploaded-file")
    public List<CapabilityImportDTO> analyzeCapabilityFile(@RequestBody CapabilityImportAnalysisDTO analysisDTO) throws Exception {
        return capabilityImportService.confirmImport(analysisDTO);
    }

    @PostMapping("/import/application/capability/upload-file")
    public List<ApplicationCapabilityDTO> uploadapplicationCapabilityFile(
        @RequestPart MultipartFile file,
        @RequestParam String[] sheetnames
    ) throws Exception {
        List<ApplicationCapabilityDTO> dtos = new ArrayList<>();
        for (String sheetname : sheetnames) {
            List<ApplicationCapabilityItemDTO> items = applicationCapabilityImportService.importExcel(file.getInputStream(), sheetname);
            dtos.add(new ApplicationCapabilityDTO(sheetname, items));
        }
        return dtos;
    }

    @GetMapping(value = "export/landscape/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        LandscapeView landscapeView = landscapeViewRepository.getById(id);
        ByteArrayOutputStream file = landscapeExportService.getLandscapeExcel(id);
        ByteArrayResource byteArrayResource = new ByteArrayResource(file.toByteArray());

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + landscapeView.getDiagramName() + "xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(byteArrayResource);
    }

    @GetMapping(value = "export/applications")
    public ResponseEntity<Resource> downloadApplications() throws IOException {
        ByteArrayOutputStream file = applicationExportService.getApplications();
        ByteArrayResource byteArrayResource = new ByteArrayResource(file.toByteArray());

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=applications.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(byteArrayResource);
    }

    @GetMapping(value = "export/all")
    public ResponseEntity<Resource> downloadAllData(
        @RequestParam boolean applications,
        @RequestParam boolean applicationComponents,
        @RequestParam boolean owner,
        @RequestParam boolean externalSystem,
        @RequestParam boolean capabilities,
        @RequestParam(value = "landscapes[]", required = false, defaultValue = "") List<Long> landscapes,
        @RequestParam(value = "capabilitiesMapping[]", required = false, defaultValue = "") List<Long> capabilitiesMapping,
        @RequestParam boolean capabilitiesMappingWithNoLandscape,
        @RequestParam boolean functionalFlowsWhithNoLandscape
    ) throws IOException {
        ByteArrayOutputStream file = exportFullDataService.getallData(
            applications,
            applicationComponents,
            owner,
            externalSystem,
            capabilities,
            landscapes,
            capabilitiesMapping,
            capabilitiesMappingWithNoLandscape,
            functionalFlowsWhithNoLandscape
        );
        ByteArrayResource byteArrayResource = new ByteArrayResource(file.toByteArray());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-h-m-s");
        String date = sdf.format(new Date());

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all-data-" + date + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(byteArrayResource);
    }

    @PostMapping("/import/external-system/upload-file")
    public List<ExternalSystem> uploadExternalSystemFile(@RequestPart MultipartFile file) throws Exception {
        return externalSystemImportService.importExcel(file.getInputStream());
    }

    @PostMapping("/import/data-objects/upload-file")
    public DataObjectDTO uploadDataObjectsFile(@RequestPart MultipartFile file) throws Exception {
        try {
            return dataObjectImportService.importExcel(file.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(), "IMPORT DO BO", e.getMessage());
        }
    }
}
