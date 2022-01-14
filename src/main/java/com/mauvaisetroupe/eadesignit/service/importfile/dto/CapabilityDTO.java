package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import java.io.Serializable;

public class CapabilityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String comment;
    private Integer level;

    public String getName() {
        return this.name;
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
}
