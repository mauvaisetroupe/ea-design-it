package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Technology.
 */
@Entity
@Table(name = "technology")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Technology implements Serializable {

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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "technologies")
    @JsonIgnoreProperties(
        value = {
            "owner",
            "itOwner",
            "businessOwner",
            "organizationalEntity",
            "categories",
            "technologies",
            "externalIDS",
            "applicationsLists",
            "capabilityApplicationMappings",
            "dataObjects",
        },
        allowSetters = true
    )
    private Set<Application> applications = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "technologies")
    @JsonIgnoreProperties(value = { "application", "categories", "technologies", "externalIDS" }, allowSetters = true)
    private Set<ApplicationComponent> components = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "technologies")
    @JsonIgnoreProperties(
        value = { "components", "application", "owner", "technologies", "landscapes", "parent", "businessObject" },
        allowSetters = true
    )
    private Set<DataObject> dataObjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Technology id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Technology name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public Technology type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Technology description(String description) {
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
            this.applications.forEach(i -> i.removeTechnologies(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addTechnologies(this));
        }
        this.applications = applications;
    }

    public Technology applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public Technology addApplications(Application application) {
        this.applications.add(application);
        application.getTechnologies().add(this);
        return this;
    }

    public Technology removeApplications(Application application) {
        this.applications.remove(application);
        application.getTechnologies().remove(this);
        return this;
    }

    public Set<ApplicationComponent> getComponents() {
        return this.components;
    }

    public void setComponents(Set<ApplicationComponent> applicationComponents) {
        if (this.components != null) {
            this.components.forEach(i -> i.removeTechnologies(this));
        }
        if (applicationComponents != null) {
            applicationComponents.forEach(i -> i.addTechnologies(this));
        }
        this.components = applicationComponents;
    }

    public Technology components(Set<ApplicationComponent> applicationComponents) {
        this.setComponents(applicationComponents);
        return this;
    }

    public Technology addComponents(ApplicationComponent applicationComponent) {
        this.components.add(applicationComponent);
        applicationComponent.getTechnologies().add(this);
        return this;
    }

    public Technology removeComponents(ApplicationComponent applicationComponent) {
        this.components.remove(applicationComponent);
        applicationComponent.getTechnologies().remove(this);
        return this;
    }

    public Set<DataObject> getDataObjects() {
        return this.dataObjects;
    }

    public void setDataObjects(Set<DataObject> dataObjects) {
        if (this.dataObjects != null) {
            this.dataObjects.forEach(i -> i.removeTechnologies(this));
        }
        if (dataObjects != null) {
            dataObjects.forEach(i -> i.addTechnologies(this));
        }
        this.dataObjects = dataObjects;
    }

    public Technology dataObjects(Set<DataObject> dataObjects) {
        this.setDataObjects(dataObjects);
        return this;
    }

    public Technology addDataObjects(DataObject dataObject) {
        this.dataObjects.add(dataObject);
        dataObject.getTechnologies().add(this);
        return this;
    }

    public Technology removeDataObjects(DataObject dataObject) {
        this.dataObjects.remove(dataObject);
        dataObject.getTechnologies().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Technology)) {
            return false;
        }
        return getId() != null && getId().equals(((Technology) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Technology{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
