package com.mauvaisetroupe.eadesignit.repository.view;

import com.mauvaisetroupe.eadesignit.domain.Owner;

public class LandscapeLight {

    private String diagramName;
    private Long id;
    private Owner owner;

    public LandscapeLight(Long id, String diagramName, Owner owner) {
        this.diagramName = diagramName;
        this.id = id;
        this.owner = owner;
    }

    // Getters and setters

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getDiagramName() {
        return diagramName;
    }

    public void setDiagramName(String diagramName) {
        this.diagramName = diagramName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LandscapeLight)) {
            return false;
        }
        return id != null && id.equals(((LandscapeLight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
