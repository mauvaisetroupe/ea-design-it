package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CapabilityApplicationMapping.
 */
@Entity
@Table(name = "capability_application_mapping")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CapabilityApplicationMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "subCapabilities", "capabilityApplicationMappings" }, allowSetters = true)
    private Capability capability;

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
        },
        allowSetters = true
    )
    private Application application;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_capability_ap__landscap_b2",
        joinColumns = @JoinColumn(name = "capability_application_mapping_id"),
        inverseJoinColumns = @JoinColumn(name = "landscape_id")
    )
    @JsonIgnoreProperties(value = { "owner", "flows", "capabilityApplicationMappings" }, allowSetters = true)
    private Set<LandscapeView> landscapes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CapabilityApplicationMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Capability getCapability() {
        return this.capability;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    public CapabilityApplicationMapping capability(Capability capability) {
        this.setCapability(capability);
        return this;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public CapabilityApplicationMapping application(Application application) {
        this.setApplication(application);
        return this;
    }

    public Set<LandscapeView> getLandscapes() {
        return this.landscapes;
    }

    public void setLandscapes(Set<LandscapeView> landscapeViews) {
        this.landscapes = landscapeViews;
    }

    public CapabilityApplicationMapping landscapes(Set<LandscapeView> landscapeViews) {
        this.setLandscapes(landscapeViews);
        return this;
    }

    public CapabilityApplicationMapping addLandscape(LandscapeView landscapeView) {
        this.landscapes.add(landscapeView);
        landscapeView.getCapabilityApplicationMappings().add(this);
        return this;
    }

    public CapabilityApplicationMapping removeLandscape(LandscapeView landscapeView) {
        this.landscapes.remove(landscapeView);
        landscapeView.getCapabilityApplicationMappings().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CapabilityApplicationMapping)) {
            return false;
        }
        return getId() != null && getId().equals(((CapabilityApplicationMapping) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CapabilityApplicationMapping{" +
            "id=" + getId() +
            "}";
    }
}
