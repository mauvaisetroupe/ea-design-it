package com.mauvaisetroupe.eadesignit.web.rest.dto;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.service.dto.CapabilityDTO;
import java.util.Collection;
import java.util.Set;

public class LandscapeDTO {

    LandscapeView landscape;
    Collection<CapabilityDTO> consolidatedCapability;
    Set<Application> applicationsOnlyInCapabilities;
    Set<Application> applicationsOnlyInFlows;

    public Collection<CapabilityDTO> getConsolidatedCapability() {
        return consolidatedCapability;
    }

    public void setConsolidatedCapability(Collection<CapabilityDTO> consolidatedCapability) {
        this.consolidatedCapability = consolidatedCapability;
    }

    public LandscapeView getLandscape() {
        return landscape;
    }

    public void setLandscape(LandscapeView landscape) {
        this.landscape = landscape;
    }

    public Set<Application> getApplicationsOnlyInCapabilities() {
        return applicationsOnlyInCapabilities;
    }

    public void setApplicationsOnlyInCapabilities(Set<Application> applicationsOnlyInCapabilities) {
        this.applicationsOnlyInCapabilities = applicationsOnlyInCapabilities;
    }

    public Set<Application> getApplicationsOnlyInFlows() {
        return applicationsOnlyInFlows;
    }

    public void setApplicationsOnlyInFlows(Set<Application> applicationsOnlyInFlows) {
        this.applicationsOnlyInFlows = applicationsOnlyInFlows;
    }    
}
