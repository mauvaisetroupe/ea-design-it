package com.mauvaisetroupe.eadesignit.web.rest.dto;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.service.dto.CapabilityDTO;
import java.util.Collection;

public class LandscapeDTO {

    LandscapeView landscape;
    Collection<CapabilityDTO> consolidatedCapability;

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
}
