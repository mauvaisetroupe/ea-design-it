package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "alias")
    private String alias;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "comment")
    private String comment;

    @Column(name = "status")
    private String status;

    @Column(name = "documentation_url")
    private String documentationURL;

    @Column(name = "documentation_url_2")
    private String documentationURL2;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_functional_flow__interfaces",
        joinColumns = @JoinColumn(name = "functional_flow_id"),
        inverseJoinColumns = @JoinColumn(name = "interfaces_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dataFlows", "owner", "functionalFlows" }, allowSetters = true)
    private Set<FlowInterface> interfaces = new HashSet<>();

    @ManyToMany(mappedBy = "flows")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owner", "flows" }, allowSetters = true)
    private Set<LandscapeView> landscapes = new HashSet<>();

    @ManyToMany(mappedBy = "functionalFlows")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "events", "functionalFlows", "flowInterface" }, allowSetters = true)
    private Set<DataFlow> dataFlows = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FunctionalFlow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
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

    public String getDocumentationURL() {
        return this.documentationURL;
    }

    public FunctionalFlow documentationURL(String documentationURL) {
        this.setDocumentationURL(documentationURL);
        return this;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public String getDocumentationURL2() {
        return this.documentationURL2;
    }

    public FunctionalFlow documentationURL2(String documentationURL2) {
        this.setDocumentationURL2(documentationURL2);
        return this;
    }

    public void setDocumentationURL2(String documentationURL2) {
        this.documentationURL2 = documentationURL2;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public FunctionalFlow startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public FunctionalFlow endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public Set<LandscapeView> getLandscapes() {
        return this.landscapes;
    }

    public void setLandscapes(Set<LandscapeView> landscapeViews) {
        if (this.landscapes != null) {
            this.landscapes.forEach(i -> i.removeFlows(this));
        }
        if (landscapeViews != null) {
            landscapeViews.forEach(i -> i.addFlows(this));
        }
        this.landscapes = landscapeViews;
    }

    public FunctionalFlow landscapes(Set<LandscapeView> landscapeViews) {
        this.setLandscapes(landscapeViews);
        return this;
    }

    public FunctionalFlow addLandscape(LandscapeView landscapeView) {
        this.landscapes.add(landscapeView);
        landscapeView.getFlows().add(this);
        return this;
    }

    public FunctionalFlow removeLandscape(LandscapeView landscapeView) {
        this.landscapes.remove(landscapeView);
        landscapeView.getFlows().remove(this);
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
            ", documentationURL='" + getDocumentationURL() + "'" +
            ", documentationURL2='" + getDocumentationURL2() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
