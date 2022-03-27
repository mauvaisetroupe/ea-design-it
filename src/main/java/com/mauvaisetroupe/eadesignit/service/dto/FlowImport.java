package com.mauvaisetroupe.eadesignit.service.dto;

import java.util.ArrayList;
import java.util.List;

public class FlowImport {

    private List<FlowImportLine> flowImportLines = new ArrayList<>();
    private String description;
    private boolean onError;

    public List<FlowImportLine> getFlowImportLines() {
        return flowImportLines;
    }

    public boolean isOnError() {
        return onError;
    }

    public void setOnError(boolean onError) {
        this.onError = onError;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addLine(FlowImportLine flowImportLine) {
        this.flowImportLines.add(flowImportLine);
    }
}
