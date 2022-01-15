package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CapabilityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private String comment;
    private Integer level;

    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Set<CapabilityDTO> subCapabilities = new HashSet<>();

    private CapabilityDTO parent;

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CapabilityDTO getParent() {
        return parent;
    }

    public void setParent(CapabilityDTO parent) {
        this.parent = parent;
    }

    public Set<CapabilityDTO> getSubCapabilities() {
        return subCapabilities;
    }

    public void setSubCapabilities(Set<CapabilityDTO> subCapabilities) {
        this.subCapabilities = subCapabilities;
    }

    public CapabilityDTO addSubCapabilities(CapabilityDTO capability) {
        this.subCapabilities.add(capability);
        capability.setParent(this);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "CapabilityDTO [comment=" + comment + ", description=" + description + ", level=" + level + ", name=" + name + "]";
    }
}
