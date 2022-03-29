package com.mauvaisetroupe.eadesignit.repository.view;

import com.mauvaisetroupe.eadesignit.domain.Owner;
import java.time.LocalDate;

public class FunctionalFlowLight {

    public FunctionalFlowLight(
        Long id,
        String alias,
        String description,
        String comment,
        String status,
        String documentationURL,
        String documentationURL2,
        LocalDate startDate,
        LocalDate endDate,
        Owner owner
    ) {
        this.id = id;
        this.alias = alias;
        this.description = description;
        this.comment = comment;
        this.status = status;
        this.documentationURL = documentationURL;
        this.documentationURL2 = documentationURL2;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = owner;
    }

    private Long id;
    private String alias;
    private String description;
    private String comment;
    private String status;
    private String documentationURL;
    private String documentationURL2;
    private LocalDate startDate;
    private LocalDate endDate;
    private Owner owner;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocumentationURL() {
        return this.documentationURL;
    }

    public void setDocumentationURL(String documentationURL) {
        this.documentationURL = documentationURL;
    }

    public String getDocumentationURL2() {
        return this.documentationURL2;
    }

    public void setDocumentationURL2(String documentationURL2) {
        this.documentationURL2 = documentationURL2;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionalFlowLight)) {
            return false;
        }
        return id != null && id.equals(((FunctionalFlowLight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FunctionalFlow{" +
            "id=" + getId() +
            ", alias='" + getAlias() + "'" +
            ", description='" + getDescription() + "'" +
            ", comment='" + getComment() + "'" +
            ", status='" + getStatus() + "'" +
            ", documentationURL='" + getDocumentationURL() + "'" +
            ", documentationURL2='" + getDocumentationURL2() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
