package com.mauvaisetroupe.eadesignit.domain;

import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FlowImport.
 */
@Entity
@Table(name = "tmp_import_flows")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlowImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_flow_from_excel")
    private String idFlowFromExcel;

    @Column(name = "flow_alias")
    private String flowAlias;

    @Column(name = "source_element")
    private String sourceElement;

    @Column(name = "target_element")
    private String targetElement;

    @Column(name = "description")
    private String description;

    @Column(name = "step_description")
    private String stepDescription;

    @Column(name = "integration_pattern")
    private String integrationPattern;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "format")
    private String format;

    @Column(name = "swagger")
    private String swagger;

    @Column(name = "source_url_documentation")
    private String sourceURLDocumentation;

    @Column(name = "target_url_documentation")
    private String targetURLDocumentation;

    @Column(name = "source_documentation_status")
    private String sourceDocumentationStatus;

    @Column(name = "target_documentation_status")
    private String targetDocumentationStatus;

    @Column(name = "flow_status")
    private String flowStatus;

    @Column(name = "jhi_comment")
    private String comment;

    @Column(name = "document_name")
    private String documentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_interface_status")
    private ImportStatus importInterfaceStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_functional_flow_status")
    private ImportStatus importFunctionalFlowStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_data_flow_status")
    private ImportStatus importDataFlowStatus;

    @Column(name = "import_status_message")
    private String importStatusMessage;

    private boolean external;

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

    public String getFrequency() {
        return this.frequency;
    }

    public FlowImport frequency(String frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFormat() {
        return this.format;
    }

    public FlowImport format(String format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSwagger() {
        return this.swagger;
    }

    public FlowImport swagger(String swagger) {
        this.setSwagger(swagger);
        return this;
    }

    public void setSwagger(String swagger) {
        this.swagger = swagger;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlowImport)) {
            return false;
        }
        return id != null && id.equals(((FlowImport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowImport{" +
            "id=" + getId() +
            ", idFlowFromExcel='" + getIdFlowFromExcel() + "'" +
            ", flowAlias='" + getFlowAlias() + "'" +
            ", sourceElement='" + getSourceElement() + "'" +
            ", targetElement='" + getTargetElement() + "'" +
            ", description='" + getDescription() + "'" +
            ", stepDescription='" + getStepDescription() + "'" +
            ", integrationPattern='" + getIntegrationPattern() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", format='" + getFormat() + "'" +
            ", swagger='" + getSwagger() + "'" +
            ", sourceURLDocumentation='" + getSourceURLDocumentation() + "'" +
            ", targetURLDocumentation='" + getTargetURLDocumentation() + "'" +
            ", sourceDocumentationStatus='" + getSourceDocumentationStatus() + "'" +
            ", targetDocumentationStatus='" + getTargetDocumentationStatus() + "'" +
            ", flowStatus='" + getFlowStatus() + "'" +
            ", comment='" + getComment() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", importInterfaceStatus='" + getImportInterfaceStatus() + "'" +
            ", importFunctionalFlowStatus='" + getImportFunctionalFlowStatus() + "'" +
            ", importDataFlowStatus='" + getImportDataFlowStatus() + "'" +
            ", importStatusMessage='" + getImportStatusMessage() + "'" +
            "}";
    }
}
