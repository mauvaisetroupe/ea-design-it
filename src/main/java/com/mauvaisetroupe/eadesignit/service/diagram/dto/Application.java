package com.mauvaisetroupe.eadesignit.service.diagram.dto;

import java.util.Set;

public class Application {

    private Long id;
    private String name;
    private String url;
    private Set<String> capabilities;
    private boolean actor;

    public Set<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Set<String> capabilities) {
        this.capabilities = capabilities;
    }

    public Application(Long id, String name, String url, Set<String> capabilities, boolean actor) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.capabilities = capabilities;
        this.actor = actor;
    }

    // Getter and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActor() {
        return actor;
    }

    public void setActor(boolean actor) {
        this.actor = actor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Application other = (Application) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }
}
