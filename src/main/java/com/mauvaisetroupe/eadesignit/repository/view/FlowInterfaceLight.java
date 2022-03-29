package com.mauvaisetroupe.eadesignit.repository.view;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import java.time.LocalDate;

public class FlowInterfaceLight {

    private Long id;
    private String alias;
    private String status;
    private String documentationURL;
    private String documentationURL2;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Application source;
    private Application target;
    private ApplicationComponent sourceComponent;
    private ApplicationComponent targetComponent;
    private Protocol protocol;
    private Owner owner;

    public FlowInterfaceLight(
        Long id,
        String alias,
        String status,
        String documentationURL,
        String documentationURL2,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        Application source,
        Application target,
        ApplicationComponent sourceComponent,
        ApplicationComponent targetComponent,
        Protocol protocol,
        Owner owner
    ) {
        this.id = id;
        this.alias = alias;
        this.status = status;
        this.documentationURL = documentationURL;
        this.documentationURL2 = documentationURL2;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.source = source;
        this.target = target;
        this.sourceComponent = sourceComponent;
        this.targetComponent = targetComponent;
        this.protocol = protocol;
        this.owner = owner;
    }

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Application getSource() {
        return this.source;
    }

    public void setSource(Application application) {
        this.source = application;
    }

    public Application getTarget() {
        return this.target;
    }

    public void setTarget(Application application) {
        this.target = application;
    }

    public ApplicationComponent getSourceComponent() {
        return this.sourceComponent;
    }

    public void setSourceComponent(ApplicationComponent applicationComponent) {
        this.sourceComponent = applicationComponent;
    }

    public ApplicationComponent getTargetComponent() {
        return this.targetComponent;
    }

    public void setTargetComponent(ApplicationComponent applicationComponent) {
        this.targetComponent = applicationComponent;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
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
        if (!(o instanceof FlowInterfaceLight)) {
            return false;
        }
        return id != null && id.equals(((FlowInterfaceLight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowInterface{" +
            "id=" + getId() +
            ", alias='" + getAlias() + "'" +
            ", status='" + getStatus() + "'" +
            ", documentationURL='" + getDocumentationURL() + "'" +
            ", documentationURL2='" + getDocumentationURL2() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
