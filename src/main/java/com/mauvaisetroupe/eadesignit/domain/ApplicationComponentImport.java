package com.mauvaisetroupe.eadesignit.domain;

import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import java.io.Serializable;

public class ApplicationComponentImport implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String importId;

    private String excelFileName;

    private String idFromExcel;

    private String name;

    private String applicationName;

    private String applicationId;

    private String description;

    private String type;

    private String softwareType;

    private String category1;

    private String category2;

    private String category3;

    private String technology;

    private String documentation;

    private String comment;

    private String owner;

    private ImportStatus importStatus;

    private String importStatusMessage;

    private String existingApplicationID;

    private boolean displayInLandscape;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationComponentImport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImportId() {
        return this.importId;
    }

    public ApplicationComponentImport importId(String importId) {
        this.setImportId(importId);
        return this;
    }

    public void setImportId(String importId) {
        this.importId = importId;
    }

    public String getExcelFileName() {
        return this.excelFileName;
    }

    public ApplicationComponentImport excelFileName(String excelFileName) {
        this.setExcelFileName(excelFileName);
        return this;
    }

    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    public String getIdFromExcel() {
        return this.idFromExcel;
    }

    public ApplicationComponentImport idFromExcel(String idFromExcel) {
        this.setIdFromExcel(idFromExcel);
        return this;
    }

    public void setIdFromExcel(String idFromExcel) {
        this.idFromExcel = idFromExcel;
    }

    public String getName() {
        return this.name;
    }

    public ApplicationComponentImport name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getDescription() {
        return this.description;
    }

    public ApplicationComponentImport description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public ApplicationComponentImport type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSoftwareType() {
        return this.softwareType;
    }

    public ApplicationComponentImport softwareType(String softwareType) {
        this.setSoftwareType(softwareType);
        return this;
    }

    public void setSoftwareType(String softwareType) {
        this.softwareType = softwareType;
    }

    public String getCategory1() {
        return this.category1;
    }

    public ApplicationComponentImport category1(String category1) {
        this.setCategory1(category1);
        return this;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return this.category2;
    }

    public ApplicationComponentImport category2(String category2) {
        this.setCategory2(category2);
        return this;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return this.category3;
    }

    public ApplicationComponentImport category3(String category3) {
        this.setCategory3(category3);
        return this;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getTechnology() {
        return this.technology;
    }

    public ApplicationComponentImport technology(String technology) {
        this.setTechnology(technology);
        return this;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getDocumentation() {
        return this.documentation;
    }

    public ApplicationComponentImport documentation(String documentation) {
        this.setDocumentation(documentation);
        return this;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getComment() {
        return this.comment;
    }

    public ApplicationComponentImport comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOwner() {
        return this.owner;
    }

    public ApplicationComponentImport owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ImportStatus getImportStatus() {
        return this.importStatus;
    }

    public ApplicationComponentImport importStatus(ImportStatus importStatus) {
        this.setImportStatus(importStatus);
        return this;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public String getImportStatusMessage() {
        return this.importStatusMessage;
    }

    public ApplicationComponentImport importStatusMessage(String importStatusMessage) {
        this.setImportStatusMessage(importStatusMessage);
        return this;
    }

    public void setImportStatusMessage(String importStatusMessage) {
        this.importStatusMessage = importStatusMessage;
    }

    public String getExistingApplicationID() {
        return this.existingApplicationID;
    }

    public ApplicationComponentImport existingApplicationID(String existingApplicationID) {
        this.setExistingApplicationID(existingApplicationID);
        return this;
    }

    public void setExistingApplicationID(String existingApplicationID) {
        this.existingApplicationID = existingApplicationID;
    }

    public boolean isDisplayInLandscape() {
        return displayInLandscape;
    }

    public void setDisplayInLandscape(boolean displayInLandscape) {
        this.displayInLandscape = displayInLandscape;
    }
}
