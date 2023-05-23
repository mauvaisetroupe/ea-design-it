package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlowGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowGroupRepository extends JpaRepository<FlowGroup, Long> {
    Set<FlowGroup> findByFlowId(Long flowId);

    @Query("select g from FlowGroup g left join fetch g.steps s left join fetch s.flow f where g.flow is null")
    Set<FlowGroup> findByFlowIsNull();
}
