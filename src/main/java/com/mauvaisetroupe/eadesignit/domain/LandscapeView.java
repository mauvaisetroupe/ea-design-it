package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ViewPoint;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LandscapeView.
 */
@Entity
@Table(name = "landscape_view")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LandscapeView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "viewpoint")
    private ViewPoint viewpoint;

    @Column(name = "diagram_name")
    private String diagramName;

    @OneToMany(mappedBy = "landscape")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "interfaces", "landscape", "dataFlows" }, allowSetters = true)
    private Set<FunctionalFlow> flows = new HashSet<>();

    @ManyToOne
    private Owner owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LandscapeView id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ViewPoint getViewpoint() {
        return this.viewpoint;
    }

    public LandscapeView viewpoint(ViewPoint viewpoint) {
        this.setViewpoint(viewpoint);
        return this;
    }

    public void setViewpoint(ViewPoint viewpoint) {
        this.viewpoint = viewpoint;
    }

    public String getDiagramName() {
        return this.diagramName;
    }

    public LandscapeView diagramName(String diagramName) {
        this.setDiagramName(diagramName);
        return this;
    }

    public void setDiagramName(String diagramName) {
        this.diagramName = diagramName;
    }

    public Set<FunctionalFlow> getFlows() {
        return this.flows;
    }

    public void setFlows(Set<FunctionalFlow> functionalFlows) {
        if (this.flows != null) {
            this.flows.forEach(i -> i.setLandscape(null));
        }
        if (functionalFlows != null) {
            functionalFlows.forEach(i -> i.setLandscape(this));
        }
        this.flows = functionalFlows;
    }

    public LandscapeView flows(Set<FunctionalFlow> functionalFlows) {
        this.setFlows(functionalFlows);
        return this;
    }

    public LandscapeView addFlows(FunctionalFlow functionalFlow) {
        this.flows.add(functionalFlow);
        functionalFlow.setLandscape(this);
        return this;
    }

    public LandscapeView removeFlows(FunctionalFlow functionalFlow) {
        this.flows.remove(functionalFlow);
        functionalFlow.setLandscape(null);
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public LandscapeView owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LandscapeView)) {
            return false;
        }
        return id != null && id.equals(((LandscapeView) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LandscapeView{" +
            "id=" + getId() +
            ", viewpoint='" + getViewpoint() + "'" +
            ", diagramName='" + getDiagramName() + "'" +
            "}";
    }
}
