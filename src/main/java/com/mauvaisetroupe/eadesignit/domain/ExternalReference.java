package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExternalReference.
 */
@Entity
@Table(name = "external_reference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExternalReference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "external_id")
    private String externalID;

    @ManyToOne
    private ExternalSystem externalSystem;

    @ManyToMany(mappedBy = "externalIDS")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "owner", "itOwner", "businessOwner", "categories", "technologies", "capabilities", "externalIDS", "applicationsLists" },
        allowSetters = true
    )
    private Set<Application> applications = new HashSet<>();

    @ManyToMany(mappedBy = "externalIDS")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "application", "categories", "technologies", "externalIDS" }, allowSetters = true)
    private Set<ApplicationComponent> components = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExternalReference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalID() {
        return this.externalID;
    }

    public ExternalReference externalID(String externalID) {
        this.setExternalID(externalID);
        return this;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public ExternalSystem getExternalSystem() {
        return this.externalSystem;
    }

    public void setExternalSystem(ExternalSystem externalSystem) {
        this.externalSystem = externalSystem;
    }

    public ExternalReference externalSystem(ExternalSystem externalSystem) {
        this.setExternalSystem(externalSystem);
        return this;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeExternalIDS(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addExternalIDS(this));
        }
        this.applications = applications;
    }

    public ExternalReference applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public ExternalReference addApplications(Application application) {
        this.applications.add(application);
        application.getExternalIDS().add(this);
        return this;
    }

    public ExternalReference removeApplications(Application application) {
        this.applications.remove(application);
        application.getExternalIDS().remove(this);
        return this;
    }

    public Set<ApplicationComponent> getComponents() {
        return this.components;
    }

    public void setComponents(Set<ApplicationComponent> applicationComponents) {
        if (this.components != null) {
            this.components.forEach(i -> i.removeExternalIDS(this));
        }
        if (applicationComponents != null) {
            applicationComponents.forEach(i -> i.addExternalIDS(this));
        }
        this.components = applicationComponents;
    }

    public ExternalReference components(Set<ApplicationComponent> applicationComponents) {
        this.setComponents(applicationComponents);
        return this;
    }

    public ExternalReference addComponents(ApplicationComponent applicationComponent) {
        this.components.add(applicationComponent);
        applicationComponent.getExternalIDS().add(this);
        return this;
    }

    public ExternalReference removeComponents(ApplicationComponent applicationComponent) {
        this.components.remove(applicationComponent);
        applicationComponent.getExternalIDS().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalReference)) {
            return false;
        }
        return id != null && id.equals(((ExternalReference) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalReference{" +
            "id=" + getId() +
            ", externalID='" + getExternalID() + "'" +
            "}";
    }
}
