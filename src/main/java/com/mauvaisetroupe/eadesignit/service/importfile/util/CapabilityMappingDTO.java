package com.mauvaisetroupe.eadesignit.service.importfile.util;

public class CapabilityMappingDTO {

    private String landscape;
    private String sheetName;

    public CapabilityMappingDTO(String landscape, String sheetName) {
        this.landscape = landscape;
        this.sheetName = sheetName;
    }

    public String getLandscape() {
        return landscape;
    }

    public void setLandscape(String landscape) {
        this.landscape = landscape;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
