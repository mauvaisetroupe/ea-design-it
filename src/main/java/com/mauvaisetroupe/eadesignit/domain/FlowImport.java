package com.mauvaisetroupe.eadesignit.domain;

import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import java.io.Serializable;

/**
 * A FlowImport.
 */
public class FlowImport implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String idFlowFromExcel;

    private String flowAlias;

    private String sourceElement;

    private String targetElement;

    private String description;

    private String stepDescription;

    private String integrationPattern;

    private String sourceURLDocumentation;

    private String targetURLDocumentation;

    private String sourceDocumentationStatus;

    private String targetDocumentationStatus;

    private String flowStatus;

    private String comment;

    private String documentName;

    private ImportStatus importInterfaceStatus;

    private ImportStatus importFunctionalFlowStatus;

    private ImportStatus importDataFlowStatus;

    private String importStatusMessage;

    private boolean external;

    private Integer groupOrder;

    private String groupFlowAlias;

    private String groupTitle;

    private String groupURL;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public Long getId() {
        return this.id;
    }

    public FlowImport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdFlowFromExcel() {
        return this.idFlowFromExcel;
    }

    public FlowImport idFlowFromExcel(String idFlowFromExcel) {
        this.setIdFlowFromExcel(idFlowFromExcel);
        return this;
    }

    public void setIdFlowFromExcel(String idFlowFromExcel) {
        this.idFlowFromExcel = idFlowFromExcel;
    }

    public String getFlowAlias() {
        return this.flowAlias;
    }

    public FlowImport flowAlias(String flowAlias) {
        this.setFlowAlias(flowAlias);
        return this;
    }

    public void setFlowAlias(String flowAlias) {
        this.flowAlias = flowAlias;
    }

    public String getSourceElement() {
        return this.sourceElement;
    }

    public FlowImport sourceElement(String sourceElement) {
        this.setSourceElement(sourceElement);
        return this;
    }

    public void setSourceElement(String sourceElement) {
        this.sourceElement = sourceElement;
    }

    public String getTargetElement() {
        return this.targetElement;
    }

    public FlowImport targetElement(String targetElement) {
        this.setTargetElement(targetElement);
        return this;
    }

    public void setTargetElement(String targetElement) {
        this.targetElement = targetElement;
    }

    public String getDescription() {
        return this.description;
    }

    public FlowImport description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStepDescription() {
        return this.stepDescription;
    }

    public FlowImport stepDescription(String stepDescription) {
        this.setStepDescription(stepDescription);
        return this;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getIntegrationPattern() {
        return this.integrationPattern;
    }

    public FlowImport integrationPattern(String integrationPattern) {
        this.setIntegrationPattern(integrationPattern);
        return this;
    }

    public void setIntegrationPattern(String integrationPattern) {
        this.integrationPattern = integrationPattern;
    }

    public String getSourceURLDocumentation() {
        return this.sourceURLDocumentation;
    }

    public FlowImport sourceURLDocumentation(String sourceURLDocumentation) {
        this.setSourceURLDocumentation(sourceURLDocumentation);
        return this;
    }

    public void setSourceURLDocumentation(String sourceURLDocumentation) {
        this.sourceURLDocumentation = sourceURLDocumentation;
    }

    public String getTargetURLDocumentation() {
        return this.targetURLDocumentation;
    }

    public FlowImport targetURLDocumentation(String targetURLDocumentation) {
        this.setTargetURLDocumentation(targetURLDocumentation);
        return this;
    }

    public void setTargetURLDocumentation(String targetURLDocumentation) {
        this.targetURLDocumentation = targetURLDocumentation;
    }

    public String getSourceDocumentationStatus() {
        return this.sourceDocumentationStatus;
    }

    public FlowImport sourceDocumentationStatus(String sourceDocumentationStatus) {
        this.setSourceDocumentationStatus(sourceDocumentationStatus);
        return this;
    }

    public void setSourceDocumentationStatus(String sourceDocumentationStatus) {
        this.sourceDocumentationStatus = sourceDocumentationStatus;
    }

    public String getTargetDocumentationStatus() {
        return this.targetDocumentationStatus;
    }

    public FlowImport targetDocumentationStatus(String targetDocumentationStatus) {
        this.setTargetDocumentationStatus(targetDocumentationStatus);
        return this;
    }

    public void setTargetDocumentationStatus(String targetDocumentationStatus) {
        this.targetDocumentationStatus = targetDocumentationStatus;
    }

    public String getFlowStatus() {
        return this.flowStatus;
    }

    public FlowImport flowStatus(String flowStatus) {
        this.setFlowStatus(flowStatus);
        return this;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getComment() {
        return this.comment;
    }

    public FlowImport comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public FlowImport documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public ImportStatus getImportInterfaceStatus() {
        return this.importInterfaceStatus;
    }

    public FlowImport importInterfaceStatus(ImportStatus importInterfaceStatus) {
        this.setImportInterfaceStatus(importInterfaceStatus);
        return this;
    }

    public void setImportInterfaceStatus(ImportStatus importInterfaceStatus) {
        this.importInterfaceStatus = importInterfaceStatus;
    }

    public ImportStatus getImportFunctionalFlowStatus() {
        return this.importFunctionalFlowStatus;
    }

    public FlowImport importFunctionalFlowStatus(ImportStatus importFunctionalFlowStatus) {
        this.setImportFunctionalFlowStatus(importFunctionalFlowStatus);
        return this;
    }

    public void setImportFunctionalFlowStatus(ImportStatus importFunctionalFlowStatus) {
        this.importFunctionalFlowStatus = importFunctionalFlowStatus;
    }

    public ImportStatus getImportDataFlowStatus() {
        return this.importDataFlowStatus;
    }

    public FlowImport importDataFlowStatus(ImportStatus importDataFlowStatus) {
        this.setImportDataFlowStatus(importDataFlowStatus);
        return this;
    }

    public void setImportDataFlowStatus(ImportStatus importDataFlowStatus) {
        this.importDataFlowStatus = importDataFlowStatus;
    }

    public String getImportStatusMessage() {
        return this.importStatusMessage;
    }

    public FlowImport importStatusMessage(String importStatusMessage) {
        this.setImportStatusMessage(importStatusMessage);
        return this;
    }

    public void setImportStatusMessage(String importStatusMessage) {
        this.importStatusMessage = importStatusMessage;
    }

    public Integer getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(Integer groupOrder) {
        this.groupOrder = groupOrder;
    }

    public String getGroupFlowAlias() {
        return groupFlowAlias;
    }

    public void setGroupFlowAlias(String groupFlowAlias) {
        this.groupFlowAlias = groupFlowAlias;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupURL() {
        return groupURL;
    }

    public void setGroupURL(String groupURL) {
        this.groupURL = groupURL;
    }
}
