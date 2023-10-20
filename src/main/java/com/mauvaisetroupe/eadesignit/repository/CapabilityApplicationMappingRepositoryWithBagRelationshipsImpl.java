package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CapabilityApplicationMappingRepositoryWithBagRelationshipsImpl
    implements CapabilityApplicationMappingRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CapabilityApplicationMapping> fetchBagRelationships(
        Optional<CapabilityApplicationMapping> capabilityApplicationMapping
    ) {
        return capabilityApplicationMapping.map(this::fetchLandscapes);
    }

    @Override
    public Page<CapabilityApplicationMapping> fetchBagRelationships(Page<CapabilityApplicationMapping> capabilityApplicationMappings) {
        return new PageImpl<>(
            fetchBagRelationships(capabilityApplicationMappings.getContent()),
            capabilityApplicationMappings.getPageable(),
            capabilityApplicationMappings.getTotalElements()
        );
    }

    @Override
    public List<CapabilityApplicationMapping> fetchBagRelationships(List<CapabilityApplicationMapping> capabilityApplicationMappings) {
        return Optional.of(capabilityApplicationMappings).map(this::fetchLandscapes).orElse(Collections.emptyList());
    }

    CapabilityApplicationMapping fetchLandscapes(CapabilityApplicationMapping result) {
        return entityManager
            .createQuery(
                "select capabilityApplicationMapping from CapabilityApplicationMapping capabilityApplicationMapping left join fetch capabilityApplicationMapping.landscapes where capabilityApplicationMapping.id = :id",
                CapabilityApplicationMapping.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<CapabilityApplicationMapping> fetchLandscapes(List<CapabilityApplicationMapping> capabilityApplicationMappings) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream
            .range(0, capabilityApplicationMappings.size())
            .forEach(index -> order.put(capabilityApplicationMappings.get(index).getId(), index));
        List<CapabilityApplicationMapping> result = entityManager
            .createQuery(
                "select capabilityApplicationMapping from CapabilityApplicationMapping capabilityApplicationMapping left join fetch capabilityApplicationMapping.landscapes where capabilityApplicationMapping in :capabilityApplicationMappings",
                CapabilityApplicationMapping.class
            )
            .setParameter("capabilityApplicationMappings", capabilityApplicationMappings)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
