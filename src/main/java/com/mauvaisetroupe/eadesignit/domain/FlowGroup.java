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
 * A FlowGroup.
 */
@Entity
@Table(name = "flow_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlowGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "title", length = 100)
    private String title;

    @Size(max = 500)
    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "steps", "owner", "landscapes", "dataFlows" }, allowSetters = true)
    private FunctionalFlow flow;

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "flowInterface", "group" }, allowSetters = true)
    private Set<FunctionalFlowStep> steps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FlowGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public FlowGroup title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public FlowGroup url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public FlowGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FunctionalFlow getFlow() {
        return this.flow;
    }

    public void setFlow(FunctionalFlow functionalFlow) {
        this.flow = functionalFlow;
    }

    public FlowGroup flow(FunctionalFlow functionalFlow) {
        this.setFlow(functionalFlow);
        return this;
    }

    public Set<FunctionalFlowStep> getSteps() {
        return this.steps;
    }

    public void setSteps(Set<FunctionalFlowStep> functionalFlowSteps) {
        if (this.steps != null) {
            this.steps.forEach(i -> i.setGroup(null));
        }
        if (functionalFlowSteps != null) {
            functionalFlowSteps.forEach(i -> i.setGroup(this));
        }
        this.steps = functionalFlowSteps;
    }

    public FlowGroup steps(Set<FunctionalFlowStep> functionalFlowSteps) {
        this.setSteps(functionalFlowSteps);
        return this;
    }

    public FlowGroup addSteps(FunctionalFlowStep functionalFlowStep) {
        this.steps.add(functionalFlowStep);
        functionalFlowStep.setGroup(this);
        return this;
    }

    public FlowGroup removeSteps(FunctionalFlowStep functionalFlowStep) {
        this.steps.remove(functionalFlowStep);
        functionalFlowStep.setGroup(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlowGroup)) {
            return false;
        }
        return id != null && id.equals(((FlowGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowGroup{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
