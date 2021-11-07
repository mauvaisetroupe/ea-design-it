package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.service.importfile.ApplicationImportService;
import java.io.IOException;
import java.net.URISyntaxException;
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

    public ImportResource(ApplicationImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/import/application/upload-file")
    public List<ApplicationImport> uploadFile(@RequestPart MultipartFile file) throws URISyntaxException, IOException, java.io.IOException {
        return importService.importExcel(file);
    }
}
