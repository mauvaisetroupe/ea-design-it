package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ApplicationComponentImport;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.ApplicationCapabilityImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.ApplicationImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.CapabilityImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.ComponentImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.DataFlowImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.ExcelReader;
import com.mauvaisetroupe.eadesignit.service.importfile.FlowImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.LandscapeExportService;
import com.mauvaisetroupe.eadesignit.service.importfile.PlantumlImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ApplicationCapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.FlowImportDTO;
import com.mauvaisetroupe.eadesignit.web.rest.errors.ApplicationImportException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/import/sheetnames")
    public List<String> getSheetNames(@RequestPart MultipartFile file) throws Exception {
        ExcelReader excelReader = new ExcelReader(file.getInputStream());
        return excelReader.getSheetNames();
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
    public List<ApplicationComponentImport> uploadComponentFile(@RequestPart MultipartFile file) throws Exception {
        return componentImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
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

    @PostMapping("/import/flow/sequence-diagram/pre-import")
    public com.mauvaisetroupe.eadesignit.service.dto.FlowImport uploadPlantuml(@RequestBody String plantumlSource) throws Exception {
        plantumlSource = URLDecoder.decode(plantumlSource, StandardCharsets.UTF_8);
        plantumlSource = plantumlSource.replace("###CR##", "\n");
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
    public List<CapabilityImportDTO> uploadCapabilityFile(@RequestPart MultipartFile file) throws Exception {
        return capabilityImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
    }

    @PostMapping("/import/application/capability/upload-file")
    public List<ApplicationCapabilityDTO> uploadapplicationCapabilityFile(
        @RequestPart MultipartFile file,
        @RequestParam String[] sheetname
    ) throws Exception {
        return applicationCapabilityImportService.importExcel(file.getInputStream(), file.getOriginalFilename(), sheetname);
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
}
