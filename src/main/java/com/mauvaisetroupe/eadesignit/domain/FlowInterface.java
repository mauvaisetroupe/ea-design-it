package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Protocol;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FlowInterface.
 */
@Entity
@Table(name = "flow_interface")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlowInterface implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "protocol")
    private Protocol protocol;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "flowInterface")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "functionalFlows", "flowInterface" }, allowSetters = true)
    private Set<DataFlow> dataFlows = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner", "applicationsLists" }, allowSetters = true)
    private Application source;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner", "applicationsLists" }, allowSetters = true)
    private Application target;

    @ManyToOne
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private ApplicationComponent sourceComponent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private ApplicationComponent targetComponent;

    @ManyToOne(optional = false)
    @NotNull
    private Owner owner;

    @ManyToMany(mappedBy = "interfaces")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "interfaces", "landscape", "dataFlows" }, allowSetters = true)
    private Set<FunctionalFlow> functionalFlows = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FlowInterface id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public FlowInterface protocol(Protocol protocol) {
        this.setProtocol(protocol);
        return this;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getStatus() {
        return this.status;
    }

    public FlowInterface status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<DataFlow> getDataFlows() {
        return this.dataFlows;
    }

    public void setDataFlows(Set<DataFlow> dataFlows) {
        if (this.dataFlows != null) {
            this.dataFlows.forEach(i -> i.setFlowInterface(null));
        }
        if (dataFlows != null) {
            dataFlows.forEach(i -> i.setFlowInterface(this));
        }
        this.dataFlows = dataFlows;
    }

    public FlowInterface dataFlows(Set<DataFlow> dataFlows) {
        this.setDataFlows(dataFlows);
        return this;
    }

    public FlowInterface addDataFlows(DataFlow dataFlow) {
        this.dataFlows.add(dataFlow);
        dataFlow.setFlowInterface(this);
        return this;
    }

    public FlowInterface removeDataFlows(DataFlow dataFlow) {
        this.dataFlows.remove(dataFlow);
        dataFlow.setFlowInterface(null);
        return this;
    }

    public Application getSource() {
        return this.source;
    }

    public void setSource(Application application) {
        this.source = application;
    }

    public FlowInterface source(Application application) {
        this.setSource(application);
        return this;
    }

    public Application getTarget() {
        return this.target;
    }

    public void setTarget(Application application) {
        this.target = application;
    }

    public FlowInterface target(Application application) {
        this.setTarget(application);
        return this;
    }

    public ApplicationComponent getSourceComponent() {
        return this.sourceComponent;
    }

    public void setSourceComponent(ApplicationComponent applicationComponent) {
        this.sourceComponent = applicationComponent;
    }

    public FlowInterface sourceComponent(ApplicationComponent applicationComponent) {
        this.setSourceComponent(applicationComponent);
        return this;
    }

    public ApplicationComponent getTargetComponent() {
        return this.targetComponent;
    }

    public void setTargetComponent(ApplicationComponent applicationComponent) {
        this.targetComponent = applicationComponent;
    }

    public FlowInterface targetComponent(ApplicationComponent applicationComponent) {
        this.setTargetComponent(applicationComponent);
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public FlowInterface owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    public Set<FunctionalFlow> getFunctionalFlows() {
        return this.functionalFlows;
    }

    public void setFunctionalFlows(Set<FunctionalFlow> functionalFlows) {
        if (this.functionalFlows != null) {
            this.functionalFlows.forEach(i -> i.removeInterfaces(this));
        }
        if (functionalFlows != null) {
            functionalFlows.forEach(i -> i.addInterfaces(this));
        }
        this.functionalFlows = functionalFlows;
    }

    public FlowInterface functionalFlows(Set<FunctionalFlow> functionalFlows) {
        this.setFunctionalFlows(functionalFlows);
        return this;
    }

    public FlowInterface addFunctionalFlows(FunctionalFlow functionalFlow) {
        this.functionalFlows.add(functionalFlow);
        functionalFlow.getInterfaces().add(this);
        return this;
    }

    public FlowInterface removeFunctionalFlows(FunctionalFlow functionalFlow) {
        this.functionalFlows.remove(functionalFlow);
        functionalFlow.getInterfaces().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlowInterface)) {
            return false;
        }
        return id != null && id.equals(((FlowInterface) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowInterface{" +
            "id=" + getId() +
            ", protocol='" + getProtocol() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
