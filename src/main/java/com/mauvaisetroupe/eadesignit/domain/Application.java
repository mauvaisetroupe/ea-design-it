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
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Application implements Serializable {

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

    @Size(max = 1500)
    @Column(name = "description", length = 1500)
    private String description;

    @Size(max = 1000)
    @Column(name = "jhi_comment", length = 1000)
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

    @ManyToOne
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Owner owner;

    @ManyToMany
    @JoinTable(
        name = "rel_application__categories",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applications", "components" }, allowSetters = true)
    private Set<ApplicationCategory> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_application__technologies",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "technologies_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applications", "components" }, allowSetters = true)
    private Set<Technology> technologies = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_application__capabilities",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "capabilities_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subCapabilities", "parent", "applications", "landscapes" }, allowSetters = true)
    private Set<Capability> capabilities = new HashSet<>();

    @OneToMany(mappedBy = "application")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "application", "categories", "technologies" }, allowSetters = true)
    private Set<ApplicationComponent> applicationsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Application id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return this.alias;
    }

    public Application alias(String alias) {
        this.setAlias(alias);
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return this.name;
    }

    public Application name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Application description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return this.comment;
    }

    public Application comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDocumentationURL() {
        return this.documentationURL;
    }

    public Application documentationURL(String documentationURL) {
        this.setDocumentationURL(documentationURL);
        return this;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Application startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Application endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ApplicationType getApplicationType() {
        return this.applicationType;
    }

    public Application applicationType(ApplicationType applicationType) {
        this.setApplicationType(applicationType);
        return this;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public SoftwareType getSoftwareType() {
        return this.softwareType;
    }

    public Application softwareType(SoftwareType softwareType) {
        this.setSoftwareType(softwareType);
        return this;
    }

    public void setSoftwareType(SoftwareType softwareType) {
        this.softwareType = softwareType;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Application owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    public Set<ApplicationCategory> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<ApplicationCategory> applicationCategories) {
        this.categories = applicationCategories;
    }

    public Application categories(Set<ApplicationCategory> applicationCategories) {
        this.setCategories(applicationCategories);
        return this;
    }

    public Application addCategories(ApplicationCategory applicationCategory) {
        this.categories.add(applicationCategory);
        applicationCategory.getApplications().add(this);
        return this;
    }

    public Application removeCategories(ApplicationCategory applicationCategory) {
        this.categories.remove(applicationCategory);
        applicationCategory.getApplications().remove(this);
        return this;
    }

    public Set<Technology> getTechnologies() {
        return this.technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }

    public Application technologies(Set<Technology> technologies) {
        this.setTechnologies(technologies);
        return this;
    }

    public Application addTechnologies(Technology technology) {
        this.technologies.add(technology);
        technology.getApplications().add(this);
        return this;
    }

    public Application removeTechnologies(Technology technology) {
        this.technologies.remove(technology);
        technology.getApplications().remove(this);
        return this;
    }

    public Set<Capability> getCapabilities() {
        return this.capabilities;
    }

    public void setCapabilities(Set<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public Application capabilities(Set<Capability> capabilities) {
        this.setCapabilities(capabilities);
        return this;
    }

    public Application addCapabilities(Capability capability) {
        this.capabilities.add(capability);
        capability.getApplications().add(this);
        return this;
    }

    public Application removeCapabilities(Capability capability) {
        this.capabilities.remove(capability);
        capability.getApplications().remove(this);
        return this;
    }

    public Set<ApplicationComponent> getApplicationsLists() {
        return this.applicationsLists;
    }

    public void setApplicationsLists(Set<ApplicationComponent> applicationComponents) {
        if (this.applicationsLists != null) {
            this.applicationsLists.forEach(i -> i.setApplication(null));
        }
        if (applicationComponents != null) {
            applicationComponents.forEach(i -> i.setApplication(this));
        }
        this.applicationsLists = applicationComponents;
    }

    public Application applicationsLists(Set<ApplicationComponent> applicationComponents) {
        this.setApplicationsLists(applicationComponents);
        return this;
    }

    public Application addApplicationsList(ApplicationComponent applicationComponent) {
        this.applicationsLists.add(applicationComponent);
        applicationComponent.setApplication(this);
        return this;
    }

    public Application removeApplicationsList(ApplicationComponent applicationComponent) {
        this.applicationsLists.remove(applicationComponent);
        applicationComponent.setApplication(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Application{" +
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
