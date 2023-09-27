package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CapabilityApplicationMappingRepositoryWithBagRelationships {
    Optional<CapabilityApplicationMapping> fetchBagRelationships(Optional<CapabilityApplicationMapping> capabilityApplicationMapping);

    List<CapabilityApplicationMapping> fetchBagRelationships(List<CapabilityApplicationMapping> capabilityApplicationMappings);

    Page<CapabilityApplicationMapping> fetchBagRelationships(Page<CapabilityApplicationMapping> capabilityApplicationMappings);
}
