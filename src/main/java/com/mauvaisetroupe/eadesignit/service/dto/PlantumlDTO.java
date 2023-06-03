package com.mauvaisetroupe.eadesignit.service.dto;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import java.util.Set;

public class PlantumlDTO {

    private String svg;
    private Set<FlowInterface> interfaces;

    public PlantumlDTO(String svg, Set<FlowInterface> interfaces) {
        this.svg = svg;
        this.interfaces = interfaces;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    public Set<FlowInterface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Set<FlowInterface> interfaces) {
        this.interfaces = interfaces;
    }
}
