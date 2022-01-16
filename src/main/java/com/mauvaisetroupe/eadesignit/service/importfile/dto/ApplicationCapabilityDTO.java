package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import com.mauvaisetroupe.eadesignit.domain.Application;
import java.util.List;

public class ApplicationCapabilityDTO {

    List<Application> application;
    CapabilityImportDTO capabilityImportDTO;

    public List<Application> getApplication() {
        return application;
    }

    public void setApplication(List<Application> application) {
        this.application = application;
    }

    public CapabilityImportDTO getCapabilityImportDTO() {
        return capabilityImportDTO;
    }

    public void setCapabilityImportDTO(CapabilityImportDTO capabilityImportDTO) {
        this.capabilityImportDTO = capabilityImportDTO;
    }
}
