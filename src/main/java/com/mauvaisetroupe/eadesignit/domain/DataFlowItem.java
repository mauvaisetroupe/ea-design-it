package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * DataFlow represents\n- A Event when DataFlow.Protocol=Event
 */
@ApiModel(description = "DataFlow represents\n- A Event when DataFlow.Protocol=Event")
@Entity
@Table(name = "data_flow_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataFlowItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "resource_name")
    private String resourceName;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 500)
    @Column(name = "contract_url", length = 500)
    private String contractURL;

    @Size(max = 500)
    @Column(name = "documentation_url", length = 500)
    private String documentationURL;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "items", "functionalFlows", "flowInterface" }, allowSetters = true)
    private DataFlow dataFlow;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DataFlowItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public DataFlowItem resourceName(String resourceName) {
        this.setResourceName(resourceName);
        return this;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescription() {
        return this.description;
    }

    public DataFlowItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContractURL() {
        return this.contractURL;
    }

    public DataFlowItem contractURL(String contractURL) {
        this.setContractURL(contractURL);
        return this;
    }

    public void setContractURL(String contractURL) {
        this.contractURL = contractURL;
    }

    public String getDocumentationURL() {
        return this.documentationURL;
    }

    public DataFlowItem documentationURL(String documentationURL) {
        this.setDocumentationURL(documentationURL);
        return this;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public DataFlowItem startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public DataFlowItem endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DataFlow getDataFlow() {
        return this.dataFlow;
    }

    public void setDataFlow(DataFlow dataFlow) {
        this.dataFlow = dataFlow;
    }

    public DataFlowItem dataFlow(DataFlow dataFlow) {
        this.setDataFlow(dataFlow);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataFlowItem)) {
            return false;
        }
        return id != null && id.equals(((DataFlowItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataFlowItem{" +
            "id=" + getId() +
            ", resourceName='" + getResourceName() + "'" +
            ", description='" + getDescription() + "'" +
            ", contractURL='" + getContractURL() + "'" +
            ", documentationURL='" + getDocumentationURL() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
