package com.mauvaisetroupe.eadesignit.service.dto.mapper;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;

public class CapabilityMapper {

    public Capability clone(Capability capability) {
        Capability capability2 = new Capability();
        capability2.setId(capability.getId());
        capability2.setName(capability.getName());
        capability2.setDescription(capability.getDescription());
        capability2.setComment(capability.getComment());
        capability2.setLevel(capability.getLevel());
        for (CapabilityApplicationMapping cm : capability.getCapabilityApplicationMappings()) {
            CapabilityApplicationMapping cm2 = new CapabilityApplicationMapping();
            cm2.setCapability(capability2);
            Application application2 = new Application();
            application2.setAlias(cm.getApplication().getAlias());
            application2.setId(cm.getApplication().getId());
            application2.setName(cm.getApplication().getName());
            cm2.setApplication(application2);
        }
        capability2.setCapabilityApplicationMappings(capability.getCapabilityApplicationMappings());
        return capability2;
    }
}
