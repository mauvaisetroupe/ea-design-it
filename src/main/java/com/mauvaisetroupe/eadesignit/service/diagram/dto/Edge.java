package com.mauvaisetroupe.eadesignit.service.diagram.dto;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Edge {

    // for edge
    //  - landascape : key = flow.id + step.order
    //    but not used because we are
    //  - FunctionalFlow, key = flow.id + step.order
    //    but not used because we are using "bidirectional consolidated" edges
    //  - Set<Interfaces>, interface.id
    //    but not used because we are using "consolidated" edges
    // for consolidated = source.id + target.id
    // for bidirectional consolidated = min(source,target).id + max(source,target).id
    private String id;

    private Long sourceId;
    private Long targetId;
    private SortedSet<Label> labels = new TreeSet<>();
    private boolean bidirectional = false;

    public Edge(String id, Long sourceId, Long targetId, SortedSet<Label> labels, boolean bidirectional) {
        this.setId(id);
        this.sourceId = sourceId;
        this.targetId = targetId;
        if (labels != null) this.labels = new TreeSet<>(labels);
        this.bidirectional = bidirectional;
    }

    public Edge(String id, Long sourceId, Long targetId, Label labels, boolean bidirectional) {
        this.setId(id);
        this.sourceId = sourceId;
        this.targetId = targetId;
        if (labels != null) this.labels.add(labels);
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

    public SortedSet<Label> getLabels() {
        return labels;
    }

    public void addLabel(Set<Label> label) {
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
        for (Label label : labels) {
            result = result + separator + label.getLabel();
            separator = ",";
        }
        return result;
    }
}
