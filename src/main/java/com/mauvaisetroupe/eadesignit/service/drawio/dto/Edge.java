package com.mauvaisetroupe.eadesignit.service.drawio.dto;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Edge {

    // for edge, key = flow.id + interface.id
    // for consolidated = source.id + target.id
    // for bidirectional consolidated = min(source,target).id + max(source,target).id
    private String id;

    private Long sourceId;
    private Long targetId;
    private SortedSet<String> labels = new TreeSet<>();
    private boolean bidirectional = false;

    public Edge(String id, Long sourceId, Long targetId, SortedSet<String> labels, boolean bidirectional) {
        this.setId(id);
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.labels = labels;
        this.bidirectional = bidirectional;
    }

    public Edge(String id, Long sourceId, Long targetId, String labels, boolean bidirectional) {
        this.setId(id);
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.labels.add(labels);
        this.bidirectional = bidirectional;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public SortedSet<String> getLabels() {
        return labels;
    }

    public void setLabels(SortedSet<String> label) {
        this.labels = label;
    }

    public void addLabel(Set<String> label) {
        this.labels.addAll(label);
    }

    public boolean isBidirectional() {
        return bidirectional;
    }

    public void setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    public String getLabelsAsString() {
        String result = "";
        String separator = "";
        for (String string : labels) {
            result = result + separator + string;
            separator = ",";
        }
        return result;
    }
}
