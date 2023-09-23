package com.mauvaisetroupe.eadesignit.service.dto;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import java.util.Set;

public class PlantumlDTO {

    private String svg;
    private Set<FlowInterfaceLight> interfaces;
    private Set<FunctionalFlow> flows;

    public PlantumlDTO(String svg, Set<FlowInterfaceLight> interfaces, Set<FunctionalFlow> flows) {
        this.svg = svg;
        this.interfaces = interfaces;
        this.flows = flows;
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
}
