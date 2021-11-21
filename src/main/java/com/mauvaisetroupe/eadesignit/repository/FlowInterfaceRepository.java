package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlowInterface entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowInterfaceRepository extends JpaRepository<FlowInterface, Long> {
    Optional<FlowInterface> findByAlias(String idFlowFromExcel);
    Set<FlowInterface> findBySource_NameOrTargetName(String sourceName, String targetName);
}
