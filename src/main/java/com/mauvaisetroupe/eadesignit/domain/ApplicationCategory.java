package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ApplicationCategory.
 */
@Entity
@Table(name = "application_category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_type")
    private String type;

    @Size(max = 250)
    @Column(name = "description", length = 250)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    @JsonIgnoreProperties(
        value = {
            "owner",
            "itOwner",
            "businessOwner",
            "categories",
            "technologies",
            "externalIDS",
            "applicationsLists",
            "capabilityApplicationMappings",
        },
        allowSetters = true
    )
    private Set<Application> applications = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    @JsonIgnoreProperties(value = { "application", "categories", "technologies", "externalIDS" }, allowSetters = true)
    private Set<ApplicationComponent> components = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ApplicationCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public ApplicationCategory type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public ApplicationCategory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeCategories(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addCategories(this));
        }
        this.applications = applications;
    }

    public ApplicationCategory applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public ApplicationCategory addApplications(Application application) {
        this.applications.add(application);
        application.getCategories().add(this);
        return this;
    }

    public ApplicationCategory removeApplications(Application application) {
        this.applications.remove(application);
        application.getCategories().remove(this);
        return this;
    }

    public Set<ApplicationComponent> getComponents() {
        return this.components;
    }

    public void setComponents(Set<ApplicationComponent> applicationComponents) {
        if (this.components != null) {
            this.components.forEach(i -> i.removeCategories(this));
        }
        if (applicationComponents != null) {
            applicationComponents.forEach(i -> i.addCategories(this));
        }
        this.components = applicationComponents;
    }

    public ApplicationCategory components(Set<ApplicationComponent> applicationComponents) {
        this.setComponents(applicationComponents);
        return this;
    }

    public ApplicationCategory addComponents(ApplicationComponent applicationComponent) {
        this.components.add(applicationComponent);
        applicationComponent.getCategories().add(this);
        return this;
    }

    public ApplicationCategory removeComponents(ApplicationComponent applicationComponent) {
        this.components.remove(applicationComponent);
        applicationComponent.getCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
