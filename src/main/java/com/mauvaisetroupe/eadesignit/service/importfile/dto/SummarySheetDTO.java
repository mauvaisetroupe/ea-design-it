package com.mauvaisetroupe.eadesignit.service.importfile.dto;

public class SummarySheetDTO {

    private String entityType;
    private String sheetName;
    private String landscapeName;
    private String ownerName;
    private boolean landscapeExists;
    private boolean ownerExists;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getLandscapeName() {
        return landscapeName;
    }

    public void setLandscapeName(String landscapeName) {
        this.landscapeName = landscapeName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isLandscapeExists() {
        return landscapeExists;
    }

    public void setLandscapeExists(boolean landscapeExists) {
        this.landscapeExists = landscapeExists;
    }

    public boolean isOwnerExists() {
        return ownerExists;
    }

    public void setOwnerExists(boolean ownerExists) {
        this.ownerExists = ownerExists;
    }

    public SummarySheetDTO(
        String entityType,
        String sheetName,
        String landscapeName,
        String ownerName,
        boolean landscapeExists,
        boolean ownerExists
    ) {
        this.entityType = entityType;
        this.sheetName = sheetName;
        this.landscapeName = landscapeName;
        this.ownerName = ownerName;
        this.landscapeExists = landscapeExists;
        this.ownerExists = ownerExists;
    }

    public SummarySheetDTO() {}
}
