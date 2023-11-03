package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import java.util.ArrayList;
import java.util.List;

public class DataObjectDTO {

    private String sheetname;
    private List<DataObjectImport> dtos = new ArrayList<>();

    public String getSheetname() {
        return sheetname;
    }

    public void setSheetname(String sheetname) {
        this.sheetname = sheetname;
    }

    public List<DataObjectImport> getDtos() {
        return dtos;
    }

    @Override
    public String toString() {
        return "DataObjectDTO [sheetname=" + sheetname + ", dtos=" + dtos + "]";
    }

    public void setDtos(List<DataObjectImport> dtos) {
        this.dtos = dtos;
    }
}
