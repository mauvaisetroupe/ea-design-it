package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FunctionalFlowStep.
 */
@Entity
@Table(name = "REL_FLOW__INTERFACES", uniqueConstraints = { @UniqueConstraint(columnNames = { "interfaces_id", "flow_id" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FunctionalFlowStep implements Serializable, Comparable<FunctionalFlowStep> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "step_order")
    private Integer stepOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "interfaces_id")
    @NotNull
    @JsonIgnoreProperties(value = { "sourceComponent", "targetComponent", "owner", "steps" }, allowSetters = true)
    private FlowInterface flowInterface;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "steps", "owner", "dataFlows" }, allowSetters = true)
    private FunctionalFlow flow;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FunctionalFlowStep id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public FunctionalFlowStep description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStepOrder() {
        return this.stepOrder;
    }

    public FunctionalFlowStep stepOrder(Integer stepOrder) {
        this.setStepOrder(stepOrder);
        return this;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }

    public FlowInterface getFlowInterface() {
        return this.flowInterface;
    }

    public void setFlowInterface(FlowInterface flowInterface) {
        this.flowInterface = flowInterface;
    }

    public FunctionalFlowStep flowInterface(FlowInterface flowInterface) {
        this.setFlowInterface(flowInterface);
        return this;
    }

    public FunctionalFlow getFlow() {
        return this.flow;
    }

    public void setFlow(FunctionalFlow functionalFlow) {
        this.flow = functionalFlow;
    }

    public FunctionalFlowStep flow(FunctionalFlow functionalFlow) {
        this.setFlow(functionalFlow);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionalFlowStep)) {
            return false;
        }
        return id != null && id.equals(((FunctionalFlowStep) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FunctionalFlowStep{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", stepOrder=" + getStepOrder() +
            ", flow=" + ((getFlow()==null)?null:getFlow().getAlias()) +
            ", interface=" + ((getFlowInterface()==null)?null:getFlowInterface().getAlias()) +            
            "}";
    }

    @Override
    public int compareTo(FunctionalFlowStep arg0) {
        int result = -1;
        if (arg0 == null) {
            result = -1;
        } else if (arg0 == this) {
            result = 0;
        } else if (arg0.getId() != null && arg0.getId() == this.getId()) {
            result = 0;
        } else if (arg0.getStepOrder() == null) {
            result = -1;
        } else if (this.getStepOrder() == null) {
            result = 1;
        } else if (this.getStepOrder().equals(arg0.getStepOrder())) {
            // no ex aequo
            result = this.getId().compareTo(arg0.getId());
        } else {
            result = this.getStepOrder().compareTo(arg0.getStepOrder());
        }
        return result;
    }
}
