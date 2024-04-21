package com.mauvaisetroupe.eadesignit.service.dto;

import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.ErrorLine;
import java.util.List;

public class ApplicationsImportDTO {

    private ImportStatus status;
    private List<ErrorLine> errorLines;
    private String errorMessage;
    private List<ApplicationImport> applicationImports;

    public ApplicationsImportDTO(
        List<ApplicationImport> applicationImports,
        ImportStatus status,
        List<ErrorLine> errorLines,
        String errorMessage
    ) {
        this.status = status;
        this.errorLines = errorLines;
        this.errorMessage = errorMessage;
        this.applicationImports = applicationImports;
    }

    public ApplicationsImportDTO() {}

    public ImportStatus getStatus() {
        return status;
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public List<ErrorLine> getErrorLines() {
        return errorLines;
    }

    public void setErrorLines(List<ErrorLine> errorLines) {
        this.errorLines = errorLines;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<ApplicationImport> getApplicationImports() {
        return applicationImports;
    }

    public void setApplicationImports(List<ApplicationImport> applicationImports) {
        this.applicationImports = applicationImports;
    }
}
