package com.mauvaisetroupe.eadesignit.repository.view;

import com.mauvaisetroupe.eadesignit.domain.DataFormat;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Frequency;
import java.time.LocalDate;
import java.util.Set;

public class DataFlowLight {

    private Long id;
    private String resourceName;
    private String resourceType;
    private String description;
    private Frequency frequency;
    private String contractURL;
    private String documentationURL;
    private LocalDate startDate;
    private LocalDate endDate;
    private DataFormat format;

    public DataFlowLight(
        Long id,
        String resourceName,
        String resourceType,
        String description,
        Frequency frequency,
        String contractURL,
        String documentationURL,
        LocalDate startDate,
        LocalDate endDate,
        DataFormat format
    ) {
        this.id = id;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.description = description;
        this.frequency = frequency;
        this.contractURL = contractURL;
        this.documentationURL = documentationURL;
        this.startDate = startDate;
        this.endDate = endDate;
        this.format = format;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public String getContractURL() {
        return contractURL;
    }

    public void setContractURL(String contractURL) {
        this.contractURL = contractURL;
    }

    public String getDocumentationURL() {
        return documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
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

    public DataFormat getFormat() {
        return format;
    }

    public void setFormat(DataFormat format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataFlowLight)) {
            return false;
        }
        return id != null && id.equals(((DataFlowLight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
