package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlowGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowGroupRepository extends JpaRepository<FlowGroup, Long> {}
