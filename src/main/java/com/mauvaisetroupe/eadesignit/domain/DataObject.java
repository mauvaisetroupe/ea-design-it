package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.enumeration.DataObjectType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DataObject.
 */
@Entity
@Table(name = "data_object")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DataObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DataObjectType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @JsonIgnoreProperties(
        value = { "components", "application", "owner", "technologies", "landscapes", "parent", "businessObject" },
        allowSetters = true
    )
    private Set<DataObject> components = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
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
            "dataObjects",
        },
        allowSetters = true
    )
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_data_object__technologies",
        joinColumns = @JoinColumn(name = "data_object_id"),
        inverseJoinColumns = @JoinColumn(name = "technologies_id")
    )
    @JsonIgnoreProperties(value = { "applications", "components", "dataObjects" }, allowSetters = true)
    private Set<Technology> technologies = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_data_object__landscapes",
        joinColumns = @JoinColumn(name = "data_object_id"),
        inverseJoinColumns = @JoinColumn(name = "landscapes_id")
    )
    @JsonIgnoreProperties(value = { "owner", "flows", "capabilityApplicationMappings", "dataObjects" }, allowSetters = true)
    private Set<LandscapeView> landscapes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "components", "application", "owner", "technologies", "landscapes", "parent", "businessObject" },
        allowSetters = true
    )
    private DataObject parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "specializations", "components", "dataObjects", "owner", "generalization", "parent" },
        allowSetters = true
    )
    private BusinessObject businessObject;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DataObject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DataObject name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataObjectType getType() {
        return this.type;
    }

    public DataObject type(DataObjectType type) {
        this.setType(type);
        return this;
    }

    public void setType(DataObjectType type) {
        this.type = type;
    }

    public Set<DataObject> getComponents() {
        return this.components;
    }

    public void setComponents(Set<DataObject> dataObjects) {
        if (this.components != null) {
            this.components.forEach(i -> i.setParent(null));
        }
        if (dataObjects != null) {
            dataObjects.forEach(i -> i.setParent(this));
        }
        this.components = dataObjects;
    }

    public DataObject components(Set<DataObject> dataObjects) {
        this.setComponents(dataObjects);
        return this;
    }

    public DataObject addComponents(DataObject dataObject) {
        this.components.add(dataObject);
        dataObject.setParent(this);
        return this;
    }

    public DataObject removeComponents(DataObject dataObject) {
        this.components.remove(dataObject);
        dataObject.setParent(null);
        return this;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public DataObject application(Application application) {
        this.setApplication(application);
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public DataObject owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    public Set<Technology> getTechnologies() {
        return this.technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }

    public DataObject technologies(Set<Technology> technologies) {
        this.setTechnologies(technologies);
        return this;
    }

    public DataObject addTechnologies(Technology technology) {
        this.technologies.add(technology);
        technology.getDataObjects().add(this);
        return this;
    }

    public DataObject removeTechnologies(Technology technology) {
        this.technologies.remove(technology);
        technology.getDataObjects().remove(this);
        return this;
    }

    public Set<LandscapeView> getLandscapes() {
        return this.landscapes;
    }

    public void setLandscapes(Set<LandscapeView> landscapeViews) {
        this.landscapes = landscapeViews;
    }

    public DataObject landscapes(Set<LandscapeView> landscapeViews) {
        this.setLandscapes(landscapeViews);
        return this;
    }

    public DataObject addLandscapes(LandscapeView landscapeView) {
        this.landscapes.add(landscapeView);
        landscapeView.getDataObjects().add(this);
        return this;
    }

    public DataObject removeLandscapes(LandscapeView landscapeView) {
        this.landscapes.remove(landscapeView);
        landscapeView.getDataObjects().remove(this);
        return this;
    }

    public DataObject getParent() {
        return this.parent;
    }

    public void setParent(DataObject dataObject) {
        this.parent = dataObject;
    }

    public DataObject parent(DataObject dataObject) {
        this.setParent(dataObject);
        return this;
    }

    public BusinessObject getBusinessObject() {
        return this.businessObject;
    }

    public void setBusinessObject(BusinessObject businessObject) {
        this.businessObject = businessObject;
    }

    public DataObject businessObject(BusinessObject businessObject) {
        this.setBusinessObject(businessObject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataObject)) {
            return false;
        }
        return getId() != null && getId().equals(((DataObject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataObject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
