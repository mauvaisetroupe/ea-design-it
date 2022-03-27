package com.mauvaisetroupe.eadesignit.service.dto;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlowImportLine {

    private int order;
    private String description;
    private Application source;
    private Application target;
    private List<FlowInterface> potentialInterfaces;
    private FlowInterface selectedInterface;
    private Protocol protocol;
    private List<DataFlow> potentialDataFlows;
    private DataFlow selectedDataFlow;

    public String getDescription() {
        return description;
    }

    public FlowInterface getSelectedInterface() {
        return selectedInterface;
    }

    public void setSelectedInterface(FlowInterface selectedInterface) {
        this.selectedInterface = selectedInterface;
    }

    public DataFlow getSelectedDataFlow() {
        return selectedDataFlow;
    }

    public void setSelectedDataFlow(DataFlow selectedDataFlow) {
        this.selectedDataFlow = selectedDataFlow;
    }

    public List<DataFlow> getPotentialDataFlows() {
        return potentialDataFlows;
    }

    public void setPotentialDataFlows(List<DataFlow> potentialDataFlows) {
        this.potentialDataFlows = potentialDataFlows;
    }

    public void addPotentialDataFlow(DataFlow potentialDataFlow) {
        if (this.potentialDataFlows == null) this.potentialDataFlows = new ArrayList<>();
        this.potentialDataFlows.add(potentialDataFlow);
    }

    public void addPotentialDataFlow(Set<DataFlow> dataFlows) {
        if (this.potentialDataFlows == null) this.potentialDataFlows = new ArrayList<>();
        this.potentialDataFlows.addAll(dataFlows);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Application getSource() {
        return source;
    }

    public void setSource(Application source) {
        this.source = source;
    }

    public Application getTarget() {
        return target;
    }

    public void setTarget(Application target) {
        this.target = target;
    }

    public List<FlowInterface> getPotentialInterfaces() {
        return potentialInterfaces;
    }

    public void setPotentialInterfaces(List<FlowInterface> potentialInterfaces) {
        this.potentialInterfaces = potentialInterfaces;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
