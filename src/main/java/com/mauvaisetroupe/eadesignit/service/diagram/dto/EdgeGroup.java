package com.mauvaisetroupe.eadesignit.service.diagram.dto;

import java.util.List;

public class EdgeGroup {

    private List<Edge> edges;
    private String title;
    private String url;

    public EdgeGroup(List<Edge> edges, String title, String url) {
        this.edges = edges;
        this.title = title;
        this.url = url;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
