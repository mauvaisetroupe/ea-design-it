package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A BusinessObject.
 */
@Entity
@Table(name = "business_object")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "abstract_business_object")
    private Boolean abstractBusinessObject;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "generalization")
    @JsonIgnoreProperties(
        value = { "specializations", "components", "dataObjects", "owner", "generalization", "parent" },
        allowSetters = true
    )
    private Set<BusinessObject> specializations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @JsonIgnoreProperties(value = { "specializations", "owner", "generalization", "parent" }, allowSetters = true)
    private Set<BusinessObject> components = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessObject")
    @JsonIgnoreProperties(value = { "owner", "technologies", "landscapes", "businessObject" }, allowSetters = true)
    private Set<DataObject> dataObjects = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "specializations", "components", "dataObjects", "owner", "generalization", "parent" },
        allowSetters = true
    )
    private BusinessObject generalization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "specializations", "components", "dataObjects", "owner", "generalization" }, allowSetters = true)
    private BusinessObject parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessObject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BusinessObject name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAbstractBusinessObject() {
        return this.abstractBusinessObject;
    }

    public BusinessObject abstractBusinessObject(Boolean abstractBusinessObject) {
        this.setAbstractBusinessObject(abstractBusinessObject);
        return this;
    }

    public void setAbstractBusinessObject(Boolean abstractBusinessObject) {
        this.abstractBusinessObject = abstractBusinessObject;
    }

    public Set<BusinessObject> getSpecializations() {
        return this.specializations;
    }

    public void setSpecializations(Set<BusinessObject> businessObjects) {
        if (this.specializations != null) {
            this.specializations.forEach(i -> i.setGeneralization(null));
        }
        if (businessObjects != null) {
            businessObjects.forEach(i -> i.setGeneralization(this));
        }
        this.specializations = businessObjects;
    }

    public BusinessObject specializations(Set<BusinessObject> businessObjects) {
        this.setSpecializations(businessObjects);
        return this;
    }

    public BusinessObject addSpecializations(BusinessObject businessObject) {
        this.specializations.add(businessObject);
        businessObject.setGeneralization(this);
        return this;
    }

    public BusinessObject removeSpecializations(BusinessObject businessObject) {
        this.specializations.remove(businessObject);
        businessObject.setGeneralization(null);
        return this;
    }

    public Set<BusinessObject> getComponents() {
        return this.components;
    }

    public void setComponents(Set<BusinessObject> businessObjects) {
        if (this.components != null) {
            this.components.forEach(i -> i.setParent(null));
        }
        if (businessObjects != null) {
            businessObjects.forEach(i -> i.setParent(this));
        }
        this.components = businessObjects;
    }

    public BusinessObject components(Set<BusinessObject> businessObjects) {
        this.setComponents(businessObjects);
        return this;
    }

    public BusinessObject addComponents(BusinessObject businessObject) {
        this.components.add(businessObject);
        businessObject.setParent(this);
        return this;
    }

    public BusinessObject removeComponents(BusinessObject businessObject) {
        this.components.remove(businessObject);
        businessObject.setParent(null);
        return this;
    }

    public Set<DataObject> getDataObjects() {
        return this.dataObjects;
    }

    public void setDataObjects(Set<DataObject> dataObjects) {
        if (this.dataObjects != null) {
            this.dataObjects.forEach(i -> i.setBusinessObject(null));
        }
        if (dataObjects != null) {
            dataObjects.forEach(i -> i.setBusinessObject(this));
        }
        this.dataObjects = dataObjects;
    }

    public BusinessObject dataObjects(Set<DataObject> dataObjects) {
        this.setDataObjects(dataObjects);
        return this;
    }

    public BusinessObject addDataObjects(DataObject dataObject) {
        this.dataObjects.add(dataObject);
        dataObject.setBusinessObject(this);
        return this;
    }

    public BusinessObject removeDataObjects(DataObject dataObject) {
        this.dataObjects.remove(dataObject);
        dataObject.setBusinessObject(null);
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public BusinessObject owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    public BusinessObject getGeneralization() {
        return this.generalization;
    }

    public void setGeneralization(BusinessObject businessObject) {
        this.generalization = businessObject;
    }

    public BusinessObject generalization(BusinessObject businessObject) {
        this.setGeneralization(businessObject);
        return this;
    }

    public BusinessObject getParent() {
        return this.parent;
    }

    public void setParent(BusinessObject businessObject) {
        this.parent = businessObject;
    }

    public BusinessObject parent(BusinessObject businessObject) {
        this.setParent(businessObject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessObject)) {
            return false;
        }
        return getId() != null && getId().equals(((BusinessObject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessObject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", abstractBusinessObject='" + getAbstractBusinessObject() + "'" +
            "}";
    }
}
