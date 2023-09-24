package com.mauvaisetroupe.eadesignit.service.dto;

import com.mauvaisetroupe.eadesignit.domain.Owner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlowImport {

    private List<FlowImportLine> flowImportLines = new ArrayList<>();
    private Long id;
    private String alias;
    private String description;
    private String comment;
    private String status;
    private String documentationURL;
    private String documentationURL2;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean onError;
    private Owner owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public String getDocumentationURL2() {
        return documentationURL2;
    }

    public void setDocumentationURL2(String documentationURL2) {
        this.documentationURL2 = documentationURL2;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<FlowImportLine> getFlowImportLines() {
        return flowImportLines;
    }

    public boolean isOnError() {
        return onError;
    }

    public void setOnError(boolean onError) {
        this.onError = onError;
    }

    public void addLine(FlowImportLine flowImportLine) {
        this.flowImportLines.add(flowImportLine);
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    private Set<String> potentialIdentifier;

    public Set<String> getPotentialIdentifier() {
        return potentialIdentifier;
    }

    public void setPotentialIdentifier(Set<String> potentialIdentifier) {
        this.potentialIdentifier = potentialIdentifier;
    }
}
