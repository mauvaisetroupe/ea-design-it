package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Capability.
 */
@Entity
@Table(name = "capability")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Capability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1500)
    @Column(name = "description", length = 1500)
    private String description;

    @Size(max = 1500)
    @Column(name = "jhi_comment", length = 1500)
    private String comment;

    @Column(name = "jhi_level")
    private Integer level;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subCapabilities", "parent", "capabilityApplicationMappings" }, allowSetters = true)
    private Set<Capability> subCapabilities = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "subCapabilities", "parent", "capabilityApplicationMappings" }, allowSetters = true)
    private Capability parent;

    @OneToMany(mappedBy = "capability")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "capability", "application", "landscapes" }, allowSetters = true)
    private Set<CapabilityApplicationMapping> capabilityApplicationMappings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Capability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Capability name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Capability description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return this.comment;
    }

    public Capability comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Capability level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Set<Capability> getSubCapabilities() {
        return this.subCapabilities;
    }

    public void setSubCapabilities(Set<Capability> capabilities) {
        if (this.subCapabilities != null) {
            this.subCapabilities.forEach(i -> i.setParent(null));
        }
        if (capabilities != null) {
            capabilities.forEach(i -> i.setParent(this));
        }
        this.subCapabilities = capabilities;
    }

    public Capability subCapabilities(Set<Capability> capabilities) {
        this.setSubCapabilities(capabilities);
        return this;
    }

    public Capability addSubCapabilities(Capability capability) {
        this.subCapabilities.add(capability);
        capability.setParent(this);
        return this;
    }

    public Capability removeSubCapabilities(Capability capability) {
        this.subCapabilities.remove(capability);
        capability.setParent(null);
        return this;
    }

    public Capability getParent() {
        return this.parent;
    }

    public void setParent(Capability capability) {
        this.parent = capability;
    }

    public Capability parent(Capability capability) {
        this.setParent(capability);
        return this;
    }

    public Set<CapabilityApplicationMapping> getCapabilityApplicationMappings() {
        return this.capabilityApplicationMappings;
    }

    public void setCapabilityApplicationMappings(Set<CapabilityApplicationMapping> capabilityApplicationMappings) {
        if (this.capabilityApplicationMappings != null) {
            this.capabilityApplicationMappings.forEach(i -> i.setCapability(null));
        }
        if (capabilityApplicationMappings != null) {
            capabilityApplicationMappings.forEach(i -> i.setCapability(this));
        }
        this.capabilityApplicationMappings = capabilityApplicationMappings;
    }

    public Capability capabilityApplicationMappings(Set<CapabilityApplicationMapping> capabilityApplicationMappings) {
        this.setCapabilityApplicationMappings(capabilityApplicationMappings);
        return this;
    }

    public Capability addCapabilityApplicationMapping(CapabilityApplicationMapping capabilityApplicationMapping) {
        this.capabilityApplicationMappings.add(capabilityApplicationMapping);
        capabilityApplicationMapping.setCapability(this);
        return this;
    }

    public Capability removeCapabilityApplicationMapping(CapabilityApplicationMapping capabilityApplicationMapping) {
        this.capabilityApplicationMappings.remove(capabilityApplicationMapping);
        capabilityApplicationMapping.setCapability(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Capability)) {
            return false;
        }
        return id != null && id.equals(((Capability) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Capability{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", comment='" + getComment() + "'" +
            ", level=" + getLevel() +
            "}";
    }
}
