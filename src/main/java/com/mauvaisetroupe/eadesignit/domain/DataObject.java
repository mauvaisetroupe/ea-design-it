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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "container")
    @JsonIgnoreProperties(
        value = { "components", "owner", "application", "technologies", "businessObject", "container" },
        allowSetters = true
    )
    private Set<DataObject> components = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Owner owner;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_data_object__technologies",
        joinColumns = @JoinColumn(name = "data_object_id"),
        inverseJoinColumns = @JoinColumn(name = "technologies_id")
    )
    @JsonIgnoreProperties(value = { "applications", "components", "dataObjects" }, allowSetters = true)
    private Set<Technology> technologies = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "spacilizations", "components", "dataObjects", "owner", "generalization", "container" },
        allowSetters = true
    )
    private BusinessObject businessObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "components", "owner", "application", "technologies", "businessObject", "container" },
        allowSetters = true
    )
    private DataObject container;

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
            this.components.forEach(i -> i.setContainer(null));
        }
        if (dataObjects != null) {
            dataObjects.forEach(i -> i.setContainer(this));
        }
        this.components = dataObjects;
    }

    public DataObject components(Set<DataObject> dataObjects) {
        this.setComponents(dataObjects);
        return this;
    }

    public DataObject addComponents(DataObject dataObject) {
        this.components.add(dataObject);
        dataObject.setContainer(this);
        return this;
    }

    public DataObject removeComponents(DataObject dataObject) {
        this.components.remove(dataObject);
        dataObject.setContainer(null);
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

    public DataObject getContainer() {
        return this.container;
    }

    public void setContainer(DataObject dataObject) {
        this.container = dataObject;
    }

    public DataObject container(DataObject dataObject) {
        this.setContainer(dataObject);
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
