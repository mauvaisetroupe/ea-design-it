package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.enumeration.FlowType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Format;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Frequency;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DataFlow.
 */
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

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private Frequency frequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private Format format;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FlowType type;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * TOPIC name for event, FileName for Files
     */
    @ApiModelProperty(value = "TOPIC name for event, FileName for Files")
    @Column(name = "resource_name")
    private String resourceName;

    /**
     * Swagger or XSD URL
     */
    @ApiModelProperty(value = "Swagger or XSD URL")
    @Column(name = "contract_url")
    private String contractURL;

    @Column(name = "documentation_url")
    private String documentationURL;

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

    public FlowType getType() {
        return this.type;
    }

    public DataFlow type(FlowType type) {
        this.setType(type);
        return this;
    }

    public void setType(FlowType type) {
        this.type = type;
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
            ", frequency='" + getFrequency() + "'" +
            ", format='" + getFormat() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", resourceName='" + getResourceName() + "'" +
            ", contractURL='" + getContractURL() + "'" +
            ", documentationURL='" + getDocumentationURL() + "'" +
            "}";
    }
}
