package com.mauvaisetroupe.eadesignit.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.util.Set;

public class LandscapeDTO {

    @JsonIgnoreProperties(value = { "capabilityApplicationMappings" })
    LandscapeView landscape;

    Capability consolidatedCapability;

    @JsonIncludeProperties(value = { "id", "alias", "name" })
    Set<Application> applicationsOnlyInCapabilities;

    @JsonIncludeProperties(value = { "id", "alias", "name" })
    Set<Application> applicationsOnlyInFlows;

    public Capability getConsolidatedCapability() {
        return consolidatedCapability;
    }

    public void setConsolidatedCapability(Capability consolidatedCapability) {
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
