package com.mauvaisetroupe.eadesignit.domain;

import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DataFlowImport.
 */
@Entity
@Table(name = "tmp_import_dataflows")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataFlowImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "data_resource_name")
    private String dataResourceName;

    @Column(name = "data_resource_type")
    private String dataResourceType;

    @Column(name = "data_description")
    private String dataDescription;

    @Column(name = "data_documentation_url")
    private String dataDocumentationURL;

    @Column(name = "data_item_resource_name")
    private String dataItemResourceName;

    @Column(name = "data_item_resource_type")
    private String dataItemResourceType;

    @Column(name = "data_item_description")
    private String dataItemDescription;

    @Column(name = "data_item_documentation_url")
    private String dataItemDocumentationURL;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "format")
    private String format;

    @Column(name = "contract_url")
    private String contractURL;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_data_flow_status")
    private ImportStatus importDataFlowStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_data_flow_item_status")
    private ImportStatus importDataFlowItemStatus;

    @Column(name = "import_status_message")
    private String importStatusMessage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DataFlowImport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataResourceName() {
        return this.dataResourceName;
    }

    public DataFlowImport dataResourceName(String dataResourceName) {
        this.setDataResourceName(dataResourceName);
        return this;
    }

    public void setDataResourceName(String dataResourceName) {
        this.dataResourceName = dataResourceName;
    }

    public String getDataResourceType() {
        return this.dataResourceType;
    }

    public DataFlowImport dataResourceType(String dataResourceType) {
        this.setDataResourceType(dataResourceType);
        return this;
    }

    public void setDataResourceType(String dataResourceType) {
        this.dataResourceType = dataResourceType;
    }

    public String getDataDescription() {
        return this.dataDescription;
    }

    public DataFlowImport dataDescription(String dataDescription) {
        this.setDataDescription(dataDescription);
        return this;
    }

    public void setDataDescription(String dataDescription) {
        this.dataDescription = dataDescription;
    }

    public String getDataDocumentationURL() {
        return this.dataDocumentationURL;
    }

    public DataFlowImport dataDocumentationURL(String dataDocumentationURL) {
        this.setDataDocumentationURL(dataDocumentationURL);
        return this;
    }

    public void setDataDocumentationURL(String dataDocumentationURL) {
        this.dataDocumentationURL = dataDocumentationURL;
    }

    public String getDataItemResourceName() {
        return this.dataItemResourceName;
    }

    public DataFlowImport dataItemResourceName(String dataItemResourceName) {
        this.setDataItemResourceName(dataItemResourceName);
        return this;
    }

    public void setDataItemResourceName(String dataItemResourceName) {
        this.dataItemResourceName = dataItemResourceName;
    }

    public String getDataItemResourceType() {
        return this.dataItemResourceType;
    }

    public DataFlowImport dataItemResourceType(String dataItemResourceType) {
        this.setDataItemResourceType(dataItemResourceType);
        return this;
    }

    public void setDataItemResourceType(String dataItemResourceType) {
        this.dataItemResourceType = dataItemResourceType;
    }

    public String getDataItemDescription() {
        return this.dataItemDescription;
    }

    public DataFlowImport dataItemDescription(String dataItemDescription) {
        this.setDataItemDescription(dataItemDescription);
        return this;
    }

    public void setDataItemDescription(String dataItemDescription) {
        this.dataItemDescription = dataItemDescription;
    }

    public String getDataItemDocumentationURL() {
        return this.dataItemDocumentationURL;
    }

    public DataFlowImport dataItemDocumentationURL(String dataItemDocumentationURL) {
        this.setDataItemDocumentationURL(dataItemDocumentationURL);
        return this;
    }

    public void setDataItemDocumentationURL(String dataItemDocumentationURL) {
        this.dataItemDocumentationURL = dataItemDocumentationURL;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public DataFlowImport frequency(String frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFormat() {
        return this.format;
    }

    public DataFlowImport format(String format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getContractURL() {
        return this.contractURL;
    }

    public DataFlowImport contractURL(String contractURL) {
        this.setContractURL(contractURL);
        return this;
    }

    public void setContractURL(String contractURL) {
        this.contractURL = contractURL;
    }

    public ImportStatus getImportDataFlowStatus() {
        return this.importDataFlowStatus;
    }

    public DataFlowImport importDataFlowStatus(ImportStatus importDataFlowStatus) {
        this.setImportDataFlowStatus(importDataFlowStatus);
        return this;
    }

    public void setImportDataFlowStatus(ImportStatus importDataFlowStatus) {
        this.importDataFlowStatus = importDataFlowStatus;
    }

    public ImportStatus getImportDataFlowItemStatus() {
        return this.importDataFlowItemStatus;
    }

    public DataFlowImport importDataFlowItemStatus(ImportStatus importDataFlowItemStatus) {
        this.setImportDataFlowItemStatus(importDataFlowItemStatus);
        return this;
    }

    public void setImportDataFlowItemStatus(ImportStatus importDataFlowItemStatus) {
        this.importDataFlowItemStatus = importDataFlowItemStatus;
    }

    public String getImportStatusMessage() {
        return this.importStatusMessage;
    }

    public DataFlowImport importStatusMessage(String importStatusMessage) {
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
        if (!(o instanceof DataFlowImport)) {
            return false;
        }
        return id != null && id.equals(((DataFlowImport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataFlowImport{" +
            "id=" + getId() +
            ", dataResourceName='" + getDataResourceName() + "'" +
            ", dataResourceType='" + getDataResourceType() + "'" +
            ", dataDescription='" + getDataDescription() + "'" +
            ", dataDocumentationURL='" + getDataDocumentationURL() + "'" +
            ", dataItemResourceName='" + getDataItemResourceName() + "'" +
            ", dataItemResourceType='" + getDataItemResourceType() + "'" +
            ", dataItemDescription='" + getDataItemDescription() + "'" +
            ", dataItemDocumentationURL='" + getDataItemDocumentationURL() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", format='" + getFormat() + "'" +
            ", contractURL='" + getContractURL() + "'" +
            ", importDataFlowStatus='" + getImportDataFlowStatus() + "'" +
            ", importDataFlowItemStatus='" + getImportDataFlowItemStatus() + "'" +
            ", importStatusMessage='" + getImportStatusMessage() + "'" +
            "}";
    }
}
