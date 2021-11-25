package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.service.importfile.ApplicationImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.FlowImportService;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.FlowImportDTO;
import java.io.IOException;
import java.net.URISyntaxException;
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

    private final ApplicationImportService importService;
    private final FlowImportService flowImportService;

    public ImportResource(ApplicationImportService importService, FlowImportService flowImportService) {
        this.importService = importService;
        this.flowImportService = flowImportService;
    }

    @PostMapping("/import/application/upload-file")
    public List<ApplicationImport> uploadFile(@RequestPart MultipartFile file) throws Exception {
        return importService.importExcel(file);
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
}
