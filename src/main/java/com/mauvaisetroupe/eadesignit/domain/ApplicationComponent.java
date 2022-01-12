package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ApplicationType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.SoftwareType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationComponent.
 */
@Entity
@Table(name = "component")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "alias", unique = true)
    private String alias;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 500)
    @Column(name = "jhi_comment", length = 500)
    private String comment;

    @Size(max = 500)
    @Column(name = "documentation_url", length = 500)
    private String documentationURL;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_type")
    private ApplicationType applicationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "software_type")
    private SoftwareType softwareType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner", "categories", "technologies", "capabilities", "applicationsLists" }, allowSetters = true)
    private Application application;

    @ManyToMany
    @JoinTable(
        name = "rel_component__categories",
        joinColumns = @JoinColumn(name = "component_id"),
        inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applications", "components" }, allowSetters = true)
    private Set<ApplicationCategory> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_component__technologies",
        joinColumns = @JoinColumn(name = "component_id"),
        inverseJoinColumns = @JoinColumn(name = "technologies_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applications", "components" }, allowSetters = true)
    private Set<Technology> technologies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationComponent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return this.alias;
    }

    public ApplicationComponent alias(String alias) {
        this.setAlias(alias);
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return this.name;
    }

    public ApplicationComponent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ApplicationComponent description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return this.comment;
    }

    public ApplicationComponent comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDocumentationURL() {
        return this.documentationURL;
    }

    public ApplicationComponent documentationURL(String documentationURL) {
        this.setDocumentationURL(documentationURL);
        return this;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public ApplicationComponent startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public ApplicationComponent endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ApplicationType getApplicationType() {
        return this.applicationType;
    }

    public ApplicationComponent applicationType(ApplicationType applicationType) {
        this.setApplicationType(applicationType);
        return this;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public SoftwareType getSoftwareType() {
        return this.softwareType;
    }

    public ApplicationComponent softwareType(SoftwareType softwareType) {
        this.setSoftwareType(softwareType);
        return this;
    }

    public void setSoftwareType(SoftwareType softwareType) {
        this.softwareType = softwareType;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public ApplicationComponent application(Application application) {
        this.setApplication(application);
        return this;
    }

    public Set<ApplicationCategory> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<ApplicationCategory> applicationCategories) {
        this.categories = applicationCategories;
    }

    public ApplicationComponent categories(Set<ApplicationCategory> applicationCategories) {
        this.setCategories(applicationCategories);
        return this;
    }

    public ApplicationComponent addCategories(ApplicationCategory applicationCategory) {
        this.categories.add(applicationCategory);
        applicationCategory.getComponents().add(this);
        return this;
    }

    public ApplicationComponent removeCategories(ApplicationCategory applicationCategory) {
        this.categories.remove(applicationCategory);
        applicationCategory.getComponents().remove(this);
        return this;
    }

    public Set<Technology> getTechnologies() {
        return this.technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }

    public ApplicationComponent technologies(Set<Technology> technologies) {
        this.setTechnologies(technologies);
        return this;
    }

    public ApplicationComponent addTechnologies(Technology technology) {
        this.technologies.add(technology);
        technology.getComponents().add(this);
        return this;
    }

    public ApplicationComponent removeTechnologies(Technology technology) {
        this.technologies.remove(technology);
        technology.getComponents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationComponent)) {
            return false;
        }
        return id != null && id.equals(((ApplicationComponent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationComponent{" +
            "id=" + getId() +
            ", alias='" + getAlias() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", comment='" + getComment() + "'" +
            ", documentationURL='" + getDocumentationURL() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", applicationType='" + getApplicationType() + "'" +
            ", softwareType='" + getSoftwareType() + "'" +
            "}";
    }
}
