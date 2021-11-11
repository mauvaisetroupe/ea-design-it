package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlowInterface entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowInterfaceRepository extends JpaRepository<FlowInterface, Long> {
    Optional<FlowInterface> findByAlias(String idFlowFromExcel);
}
