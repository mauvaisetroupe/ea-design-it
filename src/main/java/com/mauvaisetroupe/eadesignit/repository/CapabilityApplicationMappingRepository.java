package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CapabilityApplicationMapping entity.
 */
@Repository
public interface CapabilityApplicationMappingRepository extends JpaRepository<CapabilityApplicationMapping, Long> {
    @Query(
        value = "select distinct capabilityApplicationMapping from CapabilityApplicationMapping capabilityApplicationMapping left join fetch capabilityApplicationMapping.landscapes",
        countQuery = "select count(distinct capabilityApplicationMapping) from CapabilityApplicationMapping capabilityApplicationMapping"
    )
    Page<CapabilityApplicationMapping> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct capabilityApplicationMapping from CapabilityApplicationMapping capabilityApplicationMapping left join fetch capabilityApplicationMapping.landscapes"
    )
    List<CapabilityApplicationMapping> findAllWithEagerRelationships();

    @Query(
        "select capabilityApplicationMapping from CapabilityApplicationMapping capabilityApplicationMapping left join fetch capabilityApplicationMapping.landscapes where capabilityApplicationMapping.id =:id"
    )
    Optional<CapabilityApplicationMapping> findOneWithEagerRelationships(@Param("id") Long id);

    CapabilityApplicationMapping findByApplicationAndCapability(Application application, Capability capability);
}
