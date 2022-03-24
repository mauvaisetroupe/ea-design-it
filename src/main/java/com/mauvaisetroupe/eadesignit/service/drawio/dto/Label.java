package com.mauvaisetroupe.eadesignit.service.drawio.dto;

import org.apache.commons.lang3.ObjectUtils;

public class Label implements Comparable<Label> {

    String label;
    String url;

    public Label(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Label o) {
        if (o == null) return -1;
        return ObjectUtils.compare(this.label, o.label);
    }
}
