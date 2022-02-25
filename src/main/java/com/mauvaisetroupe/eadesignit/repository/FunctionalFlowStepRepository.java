package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FunctionalFlowStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FunctionalFlowStepRepository extends JpaRepository<FunctionalFlowStep, Long> {}
