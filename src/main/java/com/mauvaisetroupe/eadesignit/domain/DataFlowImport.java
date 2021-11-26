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

    @Column(name = "data_id")
    private String dataId;

    @Column(name = "data_parent_id")
    private String dataParentId;

    @Column(name = "data_parent_name")
    private String dataParentName;

    @Column(name = "functional_flow_id")
    private String functionalFlowId;

    @Column(name = "flow_interface_id")
    private String flowInterfaceId;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "data_resource_name")
    private String dataResourceName;

    @Column(name = "data_resource_type")
    private String dataResourceType;

    @Column(name = "data_description")
    private String dataDescription;

    @Column(name = "data_frequency")
    private String dataFrequency;

    @Column(name = "data_format")
    private String dataFormat;

    @Column(name = "data_contract_url")
    private String dataContractURL;

    @Column(name = "data_documentation_url")
    private String dataDocumentationURL;

    @Column(name = "source")
    private String source;

    @Column(name = "target")
    private String target;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_data_status")
    private ImportStatus importDataStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_data_item_status")
    private ImportStatus importDataItemStatus;

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

    public String getDataId() {
        return this.dataId;
    }

    public DataFlowImport dataId(String dataId) {
        this.setDataId(dataId);
        return this;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataParentId() {
        return this.dataParentId;
    }

    public DataFlowImport dataParentId(String dataParentId) {
        this.setDataParentId(dataParentId);
        return this;
    }

    public void setDataParentId(String dataParentId) {
        this.dataParentId = dataParentId;
    }

    public String getDataParentName() {
        return this.dataParentName;
    }

    public DataFlowImport dataParentName(String dataParentName) {
        this.setDataParentName(dataParentName);
        return this;
    }

    public void setDataParentName(String dataParentName) {
        this.dataParentName = dataParentName;
    }

    public String getFunctionalFlowId() {
        return this.functionalFlowId;
    }

    public DataFlowImport functionalFlowId(String functionalFlowId) {
        this.setFunctionalFlowId(functionalFlowId);
        return this;
    }

    public void setFunctionalFlowId(String functionalFlowId) {
        this.functionalFlowId = functionalFlowId;
    }

    public String getFlowInterfaceId() {
        return this.flowInterfaceId;
    }

    public DataFlowImport flowInterfaceId(String flowInterfaceId) {
        this.setFlowInterfaceId(flowInterfaceId);
        return this;
    }

    public void setFlowInterfaceId(String flowInterfaceId) {
        this.flowInterfaceId = flowInterfaceId;
    }

    public String getDataType() {
        return this.dataType;
    }

    public DataFlowImport dataType(String dataType) {
        this.setDataType(dataType);
        return this;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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

    public String getDataFrequency() {
        return this.dataFrequency;
    }

    public DataFlowImport dataFrequency(String dataFrequency) {
        this.setDataFrequency(dataFrequency);
        return this;
    }

    public void setDataFrequency(String dataFrequency) {
        this.dataFrequency = dataFrequency;
    }

    public String getDataFormat() {
        return this.dataFormat;
    }

    public DataFlowImport dataFormat(String dataFormat) {
        this.setDataFormat(dataFormat);
        return this;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getDataContractURL() {
        return this.dataContractURL;
    }

    public DataFlowImport dataContractURL(String dataContractURL) {
        this.setDataContractURL(dataContractURL);
        return this;
    }

    public void setDataContractURL(String dataContractURL) {
        this.dataContractURL = dataContractURL;
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

    public String getSource() {
        return this.source;
    }

    public DataFlowImport source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return this.target;
    }

    public DataFlowImport target(String target) {
        this.setTarget(target);
        return this;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ImportStatus getImportDataStatus() {
        return this.importDataStatus;
    }

    public DataFlowImport importDataStatus(ImportStatus importDataStatus) {
        this.setImportDataStatus(importDataStatus);
        return this;
    }

    public void setImportDataStatus(ImportStatus importDataStatus) {
        this.importDataStatus = importDataStatus;
    }

    public ImportStatus getImportDataItemStatus() {
        return this.importDataItemStatus;
    }

    public DataFlowImport importDataItemStatus(ImportStatus importDataItemStatus) {
        this.setImportDataItemStatus(importDataItemStatus);
        return this;
    }

    public void setImportDataItemStatus(ImportStatus importDataItemStatus) {
        this.importDataItemStatus = importDataItemStatus;
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
            ", dataId='" + getDataId() + "'" +
            ", dataParentId='" + getDataParentId() + "'" +
            ", dataParentName='" + getDataParentName() + "'" +
            ", functionalFlowId='" + getFunctionalFlowId() + "'" +
            ", flowInterfaceId='" + getFlowInterfaceId() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", dataResourceName='" + getDataResourceName() + "'" +
            ", dataResourceType='" + getDataResourceType() + "'" +
            ", dataDescription='" + getDataDescription() + "'" +
            ", dataFrequency='" + getDataFrequency() + "'" +
            ", dataFormat='" + getDataFormat() + "'" +
            ", dataContractURL='" + getDataContractURL() + "'" +
            ", dataDocumentationURL='" + getDataDocumentationURL() + "'" +
            ", source='" + getSource() + "'" +
            ", target='" + getTarget() + "'" +
            ", importDataStatus='" + getImportDataStatus() + "'" +
            ", importDataItemStatus='" + getImportDataItemStatus() + "'" +
            ", importStatusMessage='" + getImportStatusMessage() + "'" +
            "}";
    }
}
