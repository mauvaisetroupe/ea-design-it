package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import java.util.ArrayList;
import java.util.List;

public class ApplicationCapabilityDTO {

    private String sheetname;
    private List<ApplicationCapabilityItemDTO> dtos = new ArrayList<>();

    public ApplicationCapabilityDTO(String sheetname, List<ApplicationCapabilityItemDTO> dtos) {
        this.sheetname = sheetname;
        this.dtos = dtos;
    }

    public List<ApplicationCapabilityItemDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ApplicationCapabilityItemDTO> dtos) {
        this.dtos = dtos;
    }

    public String getSheetname() {
        return sheetname;
    }

    public void setSheetname(String sheetname) {
        this.sheetname = sheetname;
    }
}
