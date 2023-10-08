package com.mauvaisetroupe.eadesignit.repository.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.IFlowInterface;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import org.apache.commons.lang3.ObjectUtils;

public class FlowInterfaceLight implements Comparable<FlowInterfaceLight>, IFlowInterface {

    private Long id;
    private String alias;
    private Application source;
    private Application target;
    private ApplicationComponent sourceComponent;
    private ApplicationComponent targetComponent;
    private Protocol protocol;

    public FlowInterfaceLight(
        Long id,
        String alias,
        Application source,
        Application target,
        ApplicationComponent sourceComponent,
        ApplicationComponent targetComponent,
        Protocol protocol
    ) {
        this.id = id;
        this.alias = alias;
        this.source = source;
        this.target = target;
        this.sourceComponent = sourceComponent;
        this.targetComponent = targetComponent;
        this.protocol = protocol;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    @JsonIgnoreProperties(
        value = {
            "description",
            "comment",
            "documentationURL",
            "categories",
            "technologies",
            "owner",
            "itOwner",
            "businessOwner",
            "applicationsLists",
            "capabilityApplicationMappings",
            "externalIDS",
            "nickname",
            "startDate",
            "endDate",
            "applicationType",
            "softwareType",
        }
    )
    public Application getSource() {
        return this.source;
    }

    public void setSource(Application application) {
        this.source = application;
    }

    @Override
    @JsonIgnoreProperties(
        value = {
            "description",
            "comment",
            "documentationURL",
            "categories",
            "technologies",
            "owner",
            "itOwner",
            "businessOwner",
            "applicationsLists",
            "capabilityApplicationMappings",
            "externalIDS",
            "nickname",
            "startDate",
            "endDate",
            "applicationType",
            "softwareType",
        }
    )
    public Application getTarget() {
        return this.target;
    }

    public void setTarget(Application application) {
        this.target = application;
    }

    @Override
    public ApplicationComponent getSourceComponent() {
        return this.sourceComponent;
    }

    public void setSourceComponent(ApplicationComponent applicationComponent) {
        this.sourceComponent = applicationComponent;
    }

    @Override
    public ApplicationComponent getTargetComponent() {
        return this.targetComponent;
    }

    public void setTargetComponent(ApplicationComponent applicationComponent) {
        this.targetComponent = applicationComponent;
    }

    @Override
    @JsonIgnoreProperties(value = { "type", "description", "scope" })
    public Protocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
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
            "}";
    }

    @Override
    public int compareTo(FlowInterfaceLight arg0) {
        int result = -1;
        if (arg0 == null) {
            result = -1;
        }
        // compare alias is one is not null
        else if (this.alias != null || arg0.alias != null) {
            result = ObjectUtils.compare(this.alias, arg0.alias, true);
        }
        // compare id is one is not null
        else if (this.id != null || arg0.id != null) {
            result = ObjectUtils.compare(this.id, arg0.id, true);
        } else {
            // alias and id are both null
            result = 0;
        }
        return result;
    }
}
