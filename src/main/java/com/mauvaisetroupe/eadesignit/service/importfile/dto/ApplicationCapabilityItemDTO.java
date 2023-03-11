package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import java.util.List;

public class ApplicationCapabilityItemDTO {

    private List<String> applicationNames;
    private CapabilityImportDTO capabilityImportDTO;
    private ImportStatus importStatus;
    private String errorMessage;

    public List<String> getApplicationNames() {
        return applicationNames;
    }

    public void setApplicationNames(List<String> applicationNames) {
        this.applicationNames = applicationNames;
    }

    public CapabilityImportDTO getCapabilityImportDTO() {
        return capabilityImportDTO;
    }

    public void setCapabilityImportDTO(CapabilityImportDTO capabilityImportDTO) {
        this.capabilityImportDTO = capabilityImportDTO;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
