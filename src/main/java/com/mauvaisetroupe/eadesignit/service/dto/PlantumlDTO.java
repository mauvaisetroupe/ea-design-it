package com.mauvaisetroupe.eadesignit.service.dto;

import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import java.util.Set;

public class PlantumlDTO {

    private String svg;
    private Set<FlowInterfaceLight> interfaces;

    public PlantumlDTO(String svg, Set<FlowInterfaceLight> interfaces) {
        this.svg = svg;
        this.interfaces = interfaces;
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
}
