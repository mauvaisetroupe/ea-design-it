package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FunctionalFlowStep entity.
 */
@Repository
public interface FunctionalFlowStepRepository extends JpaRepository<FunctionalFlowStep, Long> {
    default Optional<FunctionalFlowStep> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FunctionalFlowStep> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FunctionalFlowStep> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select functionalFlowStep from FunctionalFlowStep functionalFlowStep left join fetch functionalFlowStep.flowInterface left join fetch functionalFlowStep.group left join fetch functionalFlowStep.flow",
        countQuery = "select count(functionalFlowStep) from FunctionalFlowStep functionalFlowStep"
    )
    Page<FunctionalFlowStep> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select functionalFlowStep from FunctionalFlowStep functionalFlowStep left join fetch functionalFlowStep.flowInterface left join fetch functionalFlowStep.group left join fetch functionalFlowStep.flow"
    )
    List<FunctionalFlowStep> findAllWithToOneRelationships();

    @Query(
        "select functionalFlowStep from FunctionalFlowStep functionalFlowStep left join fetch functionalFlowStep.flowInterface left join fetch functionalFlowStep.group left join fetch functionalFlowStep.flow where functionalFlowStep.id =:id"
    )
    Optional<FunctionalFlowStep> findOneWithToOneRelationships(@Param("id") Long id);
}
