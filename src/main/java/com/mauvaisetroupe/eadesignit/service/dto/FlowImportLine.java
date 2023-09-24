package com.mauvaisetroupe.eadesignit.service.dto;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import java.util.List;

public class FlowImportLine {

    private int order;
    private String description;
    private Application source;
    private Application target;
    private List<FlowInterface> potentialInterfaces;
    private FlowInterface selectedInterface;
    private Protocol protocol;
    private String interfaceAlias;
    private int groupOrder;
    private String groupFlowAlias;

    public int getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(int groupOrder) {
        this.groupOrder = groupOrder;
    }

    public String getGroupFlowAlias() {
        return groupFlowAlias;
    }

    public void setGroupFlowAlias(String groupFlowAlias) {
        this.groupFlowAlias = groupFlowAlias;
    }

    public String getInterfaceAlias() {
        return interfaceAlias;
    }

    public void setInterfaceAlias(String alias) {
        this.interfaceAlias = alias;
    }

    public String getDescription() {
        return description;
    }

    public FlowInterface getSelectedInterface() {
        return selectedInterface;
    }

    public void setSelectedInterface(FlowInterface selectedInterface) {
        this.selectedInterface = selectedInterface;
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
