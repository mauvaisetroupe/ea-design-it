package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ViewPoint;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;

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

    @Lob
    @Column(name = "compressed_draw_xml")
    private String compressedDrawXML;

    @Lob
    @Column(name = "compressed_draw_svg")
    private String compressedDrawSVG;

    @ManyToOne(fetch = FetchType.EAGER)
    private Owner owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @SortNatural
    @JoinTable(
        name = "rel_landscape_view__flows",
        joinColumns = @JoinColumn(name = "landscape_view_id"),
        inverseJoinColumns = @JoinColumn(name = "flows_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "landscapes", "dataFlows" }, allowSetters = true)
    private Set<FunctionalFlow> flows = new TreeSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_landscape_vie__capabili_21",
        joinColumns = @JoinColumn(name = "landscape_view_id"),
        inverseJoinColumns = @JoinColumn(name = "capabilities_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subCapabilities", "parent", "applications", "landscapes" }, allowSetters = true)
    private Set<Capability> capabilities = new HashSet<>();

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

    public String getCompressedDrawXML() {
        return this.compressedDrawXML;
    }

    public LandscapeView compressedDrawXML(String compressedDrawXML) {
        this.setCompressedDrawXML(compressedDrawXML);
        return this;
    }

    public void setCompressedDrawXML(String compressedDrawXML) {
        this.compressedDrawXML = compressedDrawXML;
    }

    public String getCompressedDrawSVG() {
        return this.compressedDrawSVG;
    }

    public LandscapeView compressedDrawSVG(String compressedDrawSVG) {
        this.setCompressedDrawSVG(compressedDrawSVG);
        return this;
    }

    public void setCompressedDrawSVG(String compressedDrawSVG) {
        this.compressedDrawSVG = compressedDrawSVG;
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

    public Set<FunctionalFlow> getFlows() {
        return this.flows;
    }

    public void setFlows(Set<FunctionalFlow> functionalFlows) {
        this.flows = functionalFlows;
    }

    public LandscapeView flows(Set<FunctionalFlow> functionalFlows) {
        this.setFlows(functionalFlows);
        return this;
    }

    public LandscapeView addFlows(FunctionalFlow functionalFlow) {
        this.flows.add(functionalFlow);
        functionalFlow.getLandscapes().add(this);
        return this;
    }

    public LandscapeView removeFlows(FunctionalFlow functionalFlow) {
        if (this.flows.contains(functionalFlow)) {
            this.flows.remove(functionalFlow);
        } else {
            // hibernate bug due to hashcode ?
            for (Iterator<FunctionalFlow> iterator = this.flows.iterator(); iterator.hasNext();) {
                FunctionalFlow flow = iterator.next();
                if (flow.getId() != null && flow.getId().equals(functionalFlow.getId())) {
                    iterator.remove();
                }
            }
        }
        if (functionalFlow.getLandscapes().contains(this)) {
            functionalFlow.getLandscapes().remove(this);
        } else {
            // hibernate bug due to hashcode ?
            for (Iterator<LandscapeView> iterator = functionalFlow.getLandscapes().iterator(); iterator.hasNext();) {
                LandscapeView landscapeView = iterator.next();
                if (landscapeView.getId() != null && landscapeView.getId().equals(this.getId())) {
                    iterator.remove();
                }
            }
        }
        return this;
    }

    public Set<Capability> getCapabilities() {
        return this.capabilities;
    }

    public void setCapabilities(Set<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public LandscapeView capabilities(Set<Capability> capabilities) {
        this.setCapabilities(capabilities);
        return this;
    }

    public LandscapeView addCapabilities(Capability capability) {
        this.capabilities.add(capability);
        capability.getLandscapes().add(this);
        return this;
    }

    public LandscapeView removeCapabilities(Capability capability) {
        this.capabilities.remove(capability);
        capability.getLandscapes().remove(this);
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
            ", compressedDrawXML='" + getCompressedDrawXML() + "'" +
            ", compressedDrawSVG='" + getCompressedDrawSVG() + "'" +
            "}";
    }
}
