package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.service.importfile.ApplicationImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.CapabilityImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.DataFlowImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.FlowImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityImportDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.FlowImportDTO;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private final ApplicationImportService applicationImportService;
    private final FlowImportService flowImportService;
    private final DataFlowImportService dataFlowImportService;
    private final CapabilityImportService capabilityImportService;

    public ImportResource(
        ApplicationImportService importService,
        FlowImportService flowImportService,
        DataFlowImportService dataFlowImportService,
        CapabilityImportService capabilityImportService
    ) {
        this.applicationImportService = importService;
        this.flowImportService = flowImportService;
        this.dataFlowImportService = dataFlowImportService;
        this.capabilityImportService = capabilityImportService;
    }

    @PostMapping("/import/application/upload-file")
    public List<ApplicationImport> uploadFile(@RequestPart MultipartFile file) throws Exception {
        return applicationImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
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

    @PostMapping("/import/data-flow/upload-file")
    public List<DataFlowImport> uploadDataFlowFile(@RequestPart MultipartFile file) throws Exception {
        return dataFlowImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
    }

    @PostMapping("/import/capability/upload-file")
    public List<CapabilityImportDTO> uploadCapabilityFile(@RequestPart MultipartFile file) throws Exception {
        return capabilityImportService.importExcel(file.getInputStream(), file.getOriginalFilename());
    }
}
