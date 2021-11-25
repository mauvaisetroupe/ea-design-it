package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import java.util.List;

public class FlowImportDTO {

    private String excelFileName;
    private List<FlowImport> flowImports;

    public FlowImportDTO(String excelFileName, List<FlowImport> flowImports) {
        this.excelFileName = excelFileName;
        this.flowImports = flowImports;
    }

    public String getExcelFileName() {
        return excelFileName;
    }

    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    public List<FlowImport> getFlowImports() {
        return flowImports;
    }

    public void setFlowImports(List<FlowImport> flowImports) {
        this.flowImports = flowImports;
    }
}
