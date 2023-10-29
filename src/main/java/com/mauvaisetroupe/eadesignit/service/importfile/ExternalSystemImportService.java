package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.ExternalSystem;
import com.mauvaisetroupe.eadesignit.repository.ExternalSystemRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalSystemImportService {

    public static final String SHEET_NAME = "ExternalSystem";
    public static final String EXTERNL_ID = "external.id";

    @Autowired
    private ExternalSystemRepository externalSystemRepository;

    public List<ExternalSystem> importExcel(InputStream excel) throws EncryptedDocumentException, IOException {
        List<ExternalSystem> externalSystems = new ArrayList<>();
        ExcelReader excelReader = new ExcelReader(excel);
        List<Map<String, Object>> df = excelReader.getSheet(SHEET_NAME);
        for (Map<String, Object> rowMap : df) {
            String name = (String) rowMap.get(EXTERNL_ID);
            ExternalSystem externalSystem = externalSystemRepository
                .findByExternalSystemID(name)
                .orElseGet(() -> {
                    ExternalSystem newExtSys = new ExternalSystem();
                    newExtSys.setExternalSystemID(name);
                    externalSystemRepository.save(newExtSys);
                    return newExtSys;
                });
            externalSystems.add(externalSystem);
        }
        return externalSystems;
    }
}
