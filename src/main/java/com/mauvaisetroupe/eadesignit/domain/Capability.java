package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;

/**
 * A Capability.
 */
@Entity
@Table(name = "capability")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Capability implements Serializable, Comparable<Capability> {

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

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @SortNatural
    @JsonIgnoreProperties(value = { "applications", "landscapes" }, allowSetters = true)
    private SortedSet<Capability> subCapabilities = new TreeSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "subCapabilities", "applications", "landscapes" }, allowSetters = true)
    private Capability parent;

    @ManyToMany(mappedBy = "capabilities", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "owner", "itOwner", "businessOwner", "categories", "technologies", "capabilities", "externalIDS", "applicationsLists" },
        allowSetters = true
    )
    private Set<Application> applications = new HashSet<>();

    @ManyToMany(mappedBy = "capabilities")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owner", "flows", "capabilities" }, allowSetters = true)
    private Set<LandscapeView> landscapes = new HashSet<>();

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

    public void setSubCapabilities(SortedSet<Capability> capabilities) {
        if (this.subCapabilities != null) {
            this.subCapabilities.forEach(i -> i.setParent(null));
        }
        if (capabilities != null) {
            capabilities.forEach(i -> i.setParent(this));
        }
        this.subCapabilities = capabilities;
    }

    public Capability subCapabilities(SortedSet<Capability> capabilities) {
        this.setSubCapabilities(capabilities);
        return this;
    }

    public Capability addSubCapabilities(Capability capability) {
        this.subCapabilities.add(capability);
        capability.setParent(this);
        return this;
    }

    public Capability removeSubCapabilities(Capability capability) {
        if (this.subCapabilities.contains(capability)) {
            this.subCapabilities.remove(capability);
        } else {
            // hibernate bug due to hashcode ?
            for (Iterator<Capability> iterator = this.subCapabilities.iterator(); iterator.hasNext();) {
                Capability capa = iterator.next();
                if (capa.getId() != null && capa.getId().equals(capability.getId())) {
                    iterator.remove();
                }
            }
        }
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

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeCapabilities(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addCapabilities(this));
        }
        this.applications = applications;
    }

    public Capability applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public Capability addApplications(Application application) {
        this.applications.add(application);
        application.getCapabilities().add(this);
        return this;
    }

    public Capability removeApplications(Application application) {
        this.applications.remove(application);
        application.getCapabilities().remove(this);
        return this;
    }

    public Set<LandscapeView> getLandscapes() {
        return this.landscapes;
    }

    public void setLandscapes(Set<LandscapeView> landscapeViews) {
        if (this.landscapes != null) {
            this.landscapes.forEach(i -> i.removeCapabilities(this));
        }
        if (landscapeViews != null) {
            landscapeViews.forEach(i -> i.addCapabilities(this));
        }
        this.landscapes = landscapeViews;
    }

    public Capability landscapes(Set<LandscapeView> landscapeViews) {
        this.setLandscapes(landscapeViews);
        return this;
    }

    public Capability addLandscapes(LandscapeView landscapeView) {
        this.landscapes.add(landscapeView);
        landscapeView.getCapabilities().add(this);
        return this;
    }

    public Capability removeLandscapes(LandscapeView landscapeView) {
        this.landscapes.remove(landscapeView);
        landscapeView.getCapabilities().remove(this);
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

    @Override
    public int compareTo(Capability o) {
        int result = -1;
        if (o == null) {
            result = -1;
        }
        // compare alias is one is not null
        else if (this.name != null || o.name != null) {
            result = ObjectUtils.compare(this.name, o.name, true);
        }
        // compare id is one is not null
        else if (this.id != null || o.id != null) {
            result = ObjectUtils.compare(this.id, o.id, true);
        } else {
            // alias and id are both null
            result = 0;
        }
        return result;
    }
}
