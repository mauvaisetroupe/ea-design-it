package com.mauvaisetroupe.eadesignit.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import java.util.Set;

public class PlantumlDTO {

    private String svg;
    private Set<FlowInterfaceLight> interfaces;
    private Set<FunctionalFlow> flows;
    private boolean labelsShown;

    public PlantumlDTO(String svg, Set<FlowInterfaceLight> interfaces, Set<FunctionalFlow> flows, boolean labelsShown) {
        this.svg = svg; // used by landscape    // used by application
        this.interfaces = interfaces; //                      // used by application
        this.flows = flows; //                      // used by application
        this.labelsShown = labelsShown; // used by landscape    // used by application
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    public Set<FlowInterfaceLight> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Set<FlowInterfaceLight> interfaces) {
        this.interfaces = interfaces;
    }

    @JsonIgnoreProperties(
        value = { "steps", "owner", "comment", "dataFlows", "documentationURL", "documentationURL2", "startDate", "endDate", "status" }
    )
    public Set<FunctionalFlow> getFlows() {
        return flows;
    }

    public void setFlows(Set<FunctionalFlow> flows) {
        this.flows = flows;
    }

    public boolean getLabelsShown() {
        return labelsShown;
    }
}
