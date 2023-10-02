package com.mauvaisetroupe.eadesignit.service.dto;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import java.util.Set;

public class PlantumlDTO {

    private String svg;
    private Set<FlowInterfaceLight> interfaces;
    private Set<FunctionalFlow> flows;
    private boolean labelsShown;

    public PlantumlDTO(String svg, Set<FlowInterfaceLight> interfaces, Set<FunctionalFlow> flows, boolean labelsShown) {
        this.svg = svg;
        this.interfaces = interfaces;
        this.flows = flows;
        this.labelsShown = labelsShown;
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
