package com.mauvaisetroupe.eadesignit.service.drawio.dto;

public class Application {

    private Long id;
    private String name;

    public Application(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
