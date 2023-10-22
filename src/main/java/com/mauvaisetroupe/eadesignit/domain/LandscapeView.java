package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ViewPoint;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.hibernate.annotations.SortNatural;

/**
 * A LandscapeView.
 */
@Entity
@Table(name = "landscape_view")
@SuppressWarnings("common-java:DuplicatedBlocks")
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

    @ManyToMany(fetch = FetchType.LAZY)
    @SortNatural
    @JoinTable(
        name = "rel_landscape_view__flows",
        joinColumns = @JoinColumn(name = "landscape_view_id"),
        inverseJoinColumns = @JoinColumn(name = "flows_id")
    )
    @JsonIgnoreProperties(value = { "landscapes", "dataFlows" }, allowSetters = true)
    private SortedSet<FunctionalFlow> flows = new TreeSet<>();

    @ManyToMany(mappedBy = "landscapes")
    @JsonIgnoreProperties(value = { "landscapes" }, allowSetters = true)
    private Set<CapabilityApplicationMapping> capabilityApplicationMappings = new HashSet<>();

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

    public SortedSet<FunctionalFlow> getFlows() {
        return this.flows;
    }

    public void setFlows(SortedSet<FunctionalFlow> functionalFlows) {
        this.flows = functionalFlows;
    }

    public LandscapeView flows(SortedSet<FunctionalFlow> functionalFlows) {
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

    public Set<CapabilityApplicationMapping> getCapabilityApplicationMappings() {
        return this.capabilityApplicationMappings;
    }

    public void setCapabilityApplicationMappings(Set<CapabilityApplicationMapping> capabilityApplicationMappings) {
        if (this.capabilityApplicationMappings != null) {
            this.capabilityApplicationMappings.forEach(i -> i.removeLandscape(this));
        }
        if (capabilityApplicationMappings != null) {
            capabilityApplicationMappings.forEach(i -> i.addLandscape(this));
        }
        this.capabilityApplicationMappings = capabilityApplicationMappings;
    }

    public LandscapeView capabilityApplicationMappings(Set<CapabilityApplicationMapping> capabilityApplicationMappings) {
        this.setCapabilityApplicationMappings(capabilityApplicationMappings);
        return this;
    }

    public LandscapeView addCapabilityApplicationMapping(CapabilityApplicationMapping capabilityApplicationMapping) {
        this.capabilityApplicationMappings.add(capabilityApplicationMapping);
        capabilityApplicationMapping.getLandscapes().add(this);
        return this;
    }

    public LandscapeView removeCapabilityApplicationMapping(CapabilityApplicationMapping capabilityApplicationMapping) {
        if (this.capabilityApplicationMappings.contains(capabilityApplicationMapping)) {
            this.capabilityApplicationMappings.remove(capabilityApplicationMapping);
        } else {
            // hibernate bug due to hashcode ?
            for (Iterator<CapabilityApplicationMapping> iterator = this.capabilityApplicationMappings.iterator(); iterator.hasNext();) {
                CapabilityApplicationMapping cm = iterator.next();
                if (cm.getId() != null && cm.getId().equals(capabilityApplicationMapping.getId())) {
                    iterator.remove();
                }
            }
        }
        if (capabilityApplicationMapping.getLandscapes().contains(this)) {
            capabilityApplicationMapping.getLandscapes().remove(this);
        } else {
            // hibernate bug due to hashcode ?
            for (Iterator<LandscapeView> iterator = capabilityApplicationMapping.getLandscapes().iterator(); iterator.hasNext();) {
                LandscapeView landscapeView = iterator.next();
                if (landscapeView.getId() != null && landscapeView.getId().equals(this.getId())) {
                    iterator.remove();
                }
            }
        }
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
        return getId() != null && getId().equals(((LandscapeView) o).getId());
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
