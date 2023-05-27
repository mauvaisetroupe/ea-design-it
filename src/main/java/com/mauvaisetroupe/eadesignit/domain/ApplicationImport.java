package com.mauvaisetroupe.eadesignit.domain;

import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ApplicationImport.
 */
public class ApplicationImport implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String importId;

    private String excelFileName;

    private String idFromExcel;

    private String name;

    private String description;

    private String type;

    private String softwareType;

    private Set<String> categories = new HashSet<>();

    private Set<String> technologies = new HashSet<>();

    private String documentation;

    private String comment;

    private String owner;

    private ImportStatus importStatus;

    private String importStatusMessage;

    private String existingApplicationID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImportId() {
        return importId;
    }

    public void setImportId(String importId) {
        this.importId = importId;
    }

    public String getExcelFileName() {
        return excelFileName;
    }

    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    public String getIdFromExcel() {
        return idFromExcel;
    }

    public void setIdFromExcel(String idFromExcel) {
        this.idFromExcel = idFromExcel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSoftwareType() {
        return softwareType;
    }

    public void setSoftwareType(String softwareType) {
        this.softwareType = softwareType;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Set<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<String> technologies) {
        this.technologies = technologies;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public String getImportStatusMessage() {
        return importStatusMessage;
    }

    public void setImportStatusMessage(String importStatusMessage) {
        this.importStatusMessage = importStatusMessage;
    }

    public String getExistingApplicationID() {
        return existingApplicationID;
    }

    public void setExistingApplicationID(String existingApplicationID) {
        this.existingApplicationID = existingApplicationID;
    }
}
