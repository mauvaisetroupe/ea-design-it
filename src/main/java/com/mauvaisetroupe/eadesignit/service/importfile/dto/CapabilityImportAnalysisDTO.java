package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import java.util.List;

public class CapabilityImportAnalysisDTO {

    private List<CapabilityAction> capabilitiesToAdd;
    private List<CapabilityAction> capabilitiesToDelete;
    private List<CapabilityAction> capabilitiesToDeleteWithMappings;
    private List<CapabilityAction> ancestorsOfCapabilitiesWithMappings;          

    private List<String> errorLines; 

    public List<CapabilityAction> getCapabilitiesToAdd() {
        return capabilitiesToAdd;
    }
    public void setCapabilitiesToAdd(List<CapabilityAction> capabilitiesToAdd) {
        this.capabilitiesToAdd = capabilitiesToAdd;
    }
    public List<CapabilityAction> getCapabilitiesToDelete() {
        return capabilitiesToDelete;
    }
    public void setCapabilitiesToDelete(List<CapabilityAction> capabilitiesToDelete) {
        this.capabilitiesToDelete = capabilitiesToDelete;
    }
    public List<CapabilityAction> getCapabilitiesToDeleteWithMappings() {
        return capabilitiesToDeleteWithMappings;
    }
    public void setCapabilitiesToDeleteWithMappings(List<CapabilityAction> capabilitiesToDeleteWithMappings) {
        this.capabilitiesToDeleteWithMappings = capabilitiesToDeleteWithMappings;
    }
    public List<String> getErrorLines() {
        return errorLines;
    }
    public void setErrorLines(List<String> errorLines) {
        this.errorLines = errorLines;
    }    
    public List<CapabilityAction> getAncestorsOfCapabilitiesWithMappings() {
        return ancestorsOfCapabilitiesWithMappings;
    }
    public void setAncestorsOfCapabilitiesWithMappings(List<CapabilityAction> ancestorsOfCapabilitiesWithMappings) {
        this.ancestorsOfCapabilitiesWithMappings = ancestorsOfCapabilitiesWithMappings;
    }    
}
