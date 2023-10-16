package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import com.mauvaisetroupe.eadesignit.domain.Capability;

public class CapabilityAction {
    public enum Action {ADD, DELETE, FORCE_DELETE, MOVE}

    private Capability capability;
    private Action action;
    
    public CapabilityAction(Capability capability, Action action) {
        this.capability = capability;
        this.action = action;
    }

    public CapabilityAction(Capability capability) {
        this.capability = capability;
    }

    public Capability getCapability() {
        return capability;
    }
    public void setCapability(Capability capability) {
        this.capability = capability;
    }
    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }
}
