package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A OrganizationalEntity.
 */
@Entity
@Table(name = "organizational_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganizationalEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organizationalEntity")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrganizationalEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public OrganizationalEntity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.setOrganizationalEntity(null));
        }
        if (applications != null) {
            applications.forEach(i -> i.setOrganizationalEntity(this));
        }
        this.applications = applications;
    }

    public OrganizationalEntity applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public OrganizationalEntity addApplications(Application application) {
        this.applications.add(application);
        application.setOrganizationalEntity(this);
        return this;
    }

    public OrganizationalEntity removeApplications(Application application) {
        this.applications.remove(application);
        application.setOrganizationalEntity(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationalEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((OrganizationalEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationalEntity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
