package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Format;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Frequency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * DataFlow represents\n- A file when Protocol=FILE\n- A topic when Protocol=Event\n- A Swagger when Protocol=API\n- A WSDL when Protocol=SOAP\n- An XSD when Protocol=ESB, MESSAGING
 */
@ApiModel(
    description = "DataFlow represents\n- A file when Protocol=FILE\n- A topic when Protocol=Event\n- A Swagger when Protocol=API\n- A WSDL when Protocol=SOAP\n- An XSD when Protocol=ESB, MESSAGING"
)
@Entity
@Table(name = "data_flow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * TOPIC name for event, FileName for Files
     */
    @ApiModelProperty(value = "TOPIC name for event, FileName for Files")
    @Column(name = "resource_name")
    private String resourceName;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private Frequency frequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private Format format;

    /**
     * Swagger or XSD URL
     */
    @ApiModelProperty(value = "Swagger or XSD URL")
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

    @OneToMany(mappedBy = "dataFlow")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dataFlow" }, allowSetters = true)
    private Set<DataFlowItem> items = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_data_flow__functional_flows",
        joinColumns = @JoinColumn(name = "data_flow_id"),
        inverseJoinColumns = @JoinColumn(name = "functional_flows_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "interfaces", "landscapes", "dataFlows" }, allowSetters = true)
    private Set<FunctionalFlow> functionalFlows = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "dataFlows", "source", "target", "sourceComponent", "targetComponent", "owner", "functionalFlows" },
        allowSetters = true
    )
    private FlowInterface flowInterface;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DataFlow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public DataFlow resourceName(String resourceName) {
        this.setResourceName(resourceName);
        return this;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescription() {
        return this.description;
    }

    public DataFlow description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Frequency getFrequency() {
        return this.frequency;
    }

    public DataFlow frequency(Frequency frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Format getFormat() {
        return this.format;
    }

    public DataFlow format(Format format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public String getContractURL() {
        return this.contractURL;
    }

    public DataFlow contractURL(String contractURL) {
        this.setContractURL(contractURL);
        return this;
    }

    public void setContractURL(String contractURL) {
        this.contractURL = contractURL;
    }

    public String getDocumentationURL() {
        return this.documentationURL;
    }

    public DataFlow documentationURL(String documentationURL) {
        this.setDocumentationURL(documentationURL);
        return this;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public DataFlow startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public DataFlow endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<DataFlowItem> getItems() {
        return this.items;
    }

    public void setItems(Set<DataFlowItem> dataFlowItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setDataFlow(null));
        }
        if (dataFlowItems != null) {
            dataFlowItems.forEach(i -> i.setDataFlow(this));
        }
        this.items = dataFlowItems;
    }

    public DataFlow items(Set<DataFlowItem> dataFlowItems) {
        this.setItems(dataFlowItems);
        return this;
    }

    public DataFlow addItems(DataFlowItem dataFlowItem) {
        this.items.add(dataFlowItem);
        dataFlowItem.setDataFlow(this);
        return this;
    }

    public DataFlow removeItems(DataFlowItem dataFlowItem) {
        this.items.remove(dataFlowItem);
        dataFlowItem.setDataFlow(null);
        return this;
    }

    public Set<FunctionalFlow> getFunctionalFlows() {
        return this.functionalFlows;
    }

    public void setFunctionalFlows(Set<FunctionalFlow> functionalFlows) {
        this.functionalFlows = functionalFlows;
    }

    public DataFlow functionalFlows(Set<FunctionalFlow> functionalFlows) {
        this.setFunctionalFlows(functionalFlows);
        return this;
    }

    public DataFlow addFunctionalFlows(FunctionalFlow functionalFlow) {
        this.functionalFlows.add(functionalFlow);
        functionalFlow.getDataFlows().add(this);
        return this;
    }

    public DataFlow removeFunctionalFlows(FunctionalFlow functionalFlow) {
        this.functionalFlows.remove(functionalFlow);
        functionalFlow.getDataFlows().remove(this);
        return this;
    }

    public FlowInterface getFlowInterface() {
        return this.flowInterface;
    }

    public void setFlowInterface(FlowInterface flowInterface) {
        this.flowInterface = flowInterface;
    }

    public DataFlow flowInterface(FlowInterface flowInterface) {
        this.setFlowInterface(flowInterface);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataFlow)) {
            return false;
        }
        return id != null && id.equals(((DataFlow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataFlow{" +
            "id=" + getId() +
            ", resourceName='" + getResourceName() + "'" +
            ", description='" + getDescription() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", format='" + getFormat() + "'" +
            ", contractURL='" + getContractURL() + "'" +
            ", documentationURL='" + getDocumentationURL() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
