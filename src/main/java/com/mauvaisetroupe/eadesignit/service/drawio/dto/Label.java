package com.mauvaisetroupe.eadesignit.service.drawio.dto;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;

public class Label implements Comparable<Label> {

    private String label;
    private String url;
    private Map<String, String> metadata;

    public Label(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void addMetadata(String key, String value) {
        if (this.metadata == null) this.metadata = new HashMap<>();
        this.metadata.put(key, value);
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
