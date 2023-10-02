package com.mauvaisetroupe.eadesignit.service.importfile.util;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.service.importfile.ExcelReader;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.SummarySheetDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SummaryImporterService {

    public static String SUMMARY_SHEET = "Summary";
    public static String ENTITY_TYPE = "entity.type";
    public static String SHEET_LINK = "sheet hyperlink";
    public static String LANDSCAPE_NAME = "landscape.name";
    public static String OWNER = "owner";
    public static final String ENTITY_TYPE_LANDSCAPE = "Landscape";
    public static final String ENTITY_TYPE_CAPABILITY_MAPPING = "Capability Mapping";

    @Autowired
    private LandscapeViewRepository landscapeViewRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    public String findLandscape(ExcelReader excelReader, String sheetname) {
        // Find diagramname in Summary sheet
        List<Map<String, Object>> summaryDF = excelReader.getSheet(SUMMARY_SHEET);
        return findLandscape(summaryDF, sheetname);
    }

    public String findOwner(ExcelReader excelReader, String sheetname) {
        List<Map<String, Object>> summaryDF = excelReader.getSheet(SUMMARY_SHEET);
        return findOwner(summaryDF, sheetname);
    }

    public List<SummarySheetDTO> getSummary(MultipartFile file) throws EncryptedDocumentException, IOException {
        ExcelReader excelReader = new ExcelReader(file.getInputStream());
        List<Map<String, Object>> rows = excelReader.getSheet(SUMMARY_SHEET);
        List<SummarySheetDTO> result = new ArrayList<>();
        for (Map<String, Object> map : rows) {
            SummarySheetDTO summarySheetDTO = new SummarySheetDTO();
            summarySheetDTO.setEntityType((String) map.get(ENTITY_TYPE));
            summarySheetDTO.setSheetName((String) map.get(SHEET_LINK));
            if ((String) map.get(LANDSCAPE_NAME) != null) {
                summarySheetDTO.setLandscapeName((String) map.get(LANDSCAPE_NAME));
                LandscapeView landscapeView = landscapeViewRepository.findByDiagramNameIgnoreCase(summarySheetDTO.getLandscapeName());
                if (landscapeView != null) {
                    summarySheetDTO.setLandscapeExists(true);
                }
            }
            if ((String) map.get(OWNER) != null) {
                summarySheetDTO.setOwnerName((String) map.get(OWNER));
                Owner owner = ownerRepository.findByNameIgnoreCase(summarySheetDTO.getOwnerName());
                if (owner != null) {
                    summarySheetDTO.setOwnerExists(true);
                }
            }
            result.add(summarySheetDTO);
        }
        return result;
    }

    //////////////////// private

    private String findLandscape(List<Map<String, Object>> summaryDF, String sheetname) {
        for (Map<String, Object> row : summaryDF) {
            if (sheetname.equals(row.get(SHEET_LINK))) {
                return (String) row.get(LANDSCAPE_NAME);
            }
        }
        throw new IllegalStateException("Error with sheet name " + sheetname);
    }

    private String findOwner(List<Map<String, Object>> summaryDF, String sheetname) {
        for (Map<String, Object> row : summaryDF) {
            if (sheetname.equals(row.get(SHEET_LINK))) {
                return (String) row.get(OWNER);
            }
        }
        throw new IllegalStateException("Error with sheet name " + sheetname);
    }
}
