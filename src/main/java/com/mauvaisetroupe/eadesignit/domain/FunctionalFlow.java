package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FunctionalFlow.
 */
@Entity
@Table(name = "functional_flow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FunctionalFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Pattern(regexp = "^[A-Z].[99]{4}$")
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "alias")
    private String alias;

    @Column(name = "description")
    private String description;

    @Column(name = "comment")
    private String comment;

    @Column(name = "status")
    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_functional_flow__interfaces",
        joinColumns = @JoinColumn(name = "functional_flow_id"),
        inverseJoinColumns = @JoinColumn(name = "interfaces_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dataFlows", "sourceComponent", "targetComponent", "owner", "functionalFlows" }, allowSetters = true)
    private Set<FlowInterface> interfaces = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "flows", "owner" }, allowSetters = true)
    private LandscapeView landscape;

    @ManyToMany(mappedBy = "functionalFlows")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "functionalFlows", "flowInterface" }, allowSetters = true)
    private Set<DataFlow> dataFlows = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public FunctionalFlow id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return this.alias;
    }

    public FunctionalFlow alias(String alias) {
        this.setAlias(alias);
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return this.description;
    }

    public FunctionalFlow description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return this.comment;
    }

    public FunctionalFlow comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return this.status;
    }

    public FunctionalFlow status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<FlowInterface> getInterfaces() {
        return this.interfaces;
    }

    public void setInterfaces(Set<FlowInterface> flowInterfaces) {
        this.interfaces = flowInterfaces;
    }

    public FunctionalFlow interfaces(Set<FlowInterface> flowInterfaces) {
        this.setInterfaces(flowInterfaces);
        return this;
    }

    public FunctionalFlow addInterfaces(FlowInterface flowInterface) {
        this.interfaces.add(flowInterface);
        flowInterface.getFunctionalFlows().add(this);
        return this;
    }

    public FunctionalFlow removeInterfaces(FlowInterface flowInterface) {
        this.interfaces.remove(flowInterface);
        flowInterface.getFunctionalFlows().remove(this);
        return this;
    }

    public LandscapeView getLandscape() {
        return this.landscape;
    }

    public void setLandscape(LandscapeView landscapeView) {
        this.landscape = landscapeView;
    }

    public FunctionalFlow landscape(LandscapeView landscapeView) {
        this.setLandscape(landscapeView);
        return this;
    }

    public Set<DataFlow> getDataFlows() {
        return this.dataFlows;
    }

    public void setDataFlows(Set<DataFlow> dataFlows) {
        if (this.dataFlows != null) {
            this.dataFlows.forEach(i -> i.removeFunctionalFlows(this));
        }
        if (dataFlows != null) {
            dataFlows.forEach(i -> i.addFunctionalFlows(this));
        }
        this.dataFlows = dataFlows;
    }

    public FunctionalFlow dataFlows(Set<DataFlow> dataFlows) {
        this.setDataFlows(dataFlows);
        return this;
    }

    public FunctionalFlow addDataFlows(DataFlow dataFlow) {
        this.dataFlows.add(dataFlow);
        dataFlow.getFunctionalFlows().add(this);
        return this;
    }

    public FunctionalFlow removeDataFlows(DataFlow dataFlow) {
        this.dataFlows.remove(dataFlow);
        dataFlow.getFunctionalFlows().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionalFlow)) {
            return false;
        }
        return id != null && id.equals(((FunctionalFlow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FunctionalFlow{" +
            "id=" + getId() +
            ", alias='" + getAlias() + "'" +
            ", description='" + getDescription() + "'" +
            ", comment='" + getComment() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
