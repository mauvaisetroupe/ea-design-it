package com.mauvaisetroupe.eadesignit.domain;

import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A ApplicationImport.
 */
public class ApplicationImport implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idFromExcel;

    private String name;

    private String nickname;

    private String parentId;

    private String parentName;

    private String description;

    private String type;

    private String softwareType;

    private Set<String> categories = new HashSet<>();

    private Set<String> technologies = new HashSet<>();

    private String documentation;

    private String comment;

    private String owner;

    private String itOwner;

    private String businessOwner;

    private ImportStatus importStatus;

    private String importStatusMessage;

    private String existingApplicationID;

    private Boolean displayInLandscape;

    private Map<String, String> externalIDS = new HashMap<>();

    private String organizationalEntity;

    public Map<String, String> getExternalIDS() {
        return externalIDS;
    }

    public void setExternalIDS(Map<String, String> externalIDS) {
        this.externalIDS = externalIDS;
    }

    public Boolean getDisplayInLandscape() {
        return displayInLandscape;
    }

    public void setDisplayInLandscape(Boolean displayInLandscape) {
        this.displayInLandscape = displayInLandscape;
    }

    public String getIdFromExcel() {
        return idFromExcel;
    }

    public void setIdFromExcel(String idFromExcel) {
        this.idFromExcel = idFromExcel;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getItOwner() {
        return itOwner;
    }

    public void setItOwner(String itOwner) {
        this.itOwner = itOwner;
    }

    public String getBusinessOwner() {
        return businessOwner;
    }

    public void setBusinessOwner(String businessOwner) {
        this.businessOwner = businessOwner;
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

    public String getOrganizationalEntity() {
        return organizationalEntity;
    }

    public void setOrganizationalEntity(String organizationalEntity) {
        this.organizationalEntity = organizationalEntity;
    }
}
