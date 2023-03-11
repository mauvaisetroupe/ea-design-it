package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import java.util.ArrayList;
import java.util.List;

public class ApplicationCapabilityDTO {

    private String sheetname;
    private List<ApplicationCapabilityItemDTO> dtos = new ArrayList<>();

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
