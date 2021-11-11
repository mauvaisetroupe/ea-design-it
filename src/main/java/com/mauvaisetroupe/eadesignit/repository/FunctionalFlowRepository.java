package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FunctionalFlow entity.
 */
@Repository
public interface FunctionalFlowRepository extends JpaRepository<FunctionalFlow, Long> {
    @Query(
        value = "select distinct functionalFlow from FunctionalFlow functionalFlow left join fetch functionalFlow.interfaces",
        countQuery = "select count(distinct functionalFlow) from FunctionalFlow functionalFlow"
    )
    Page<FunctionalFlow> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct functionalFlow from FunctionalFlow functionalFlow left join fetch functionalFlow.interfaces")
    List<FunctionalFlow> findAllWithEagerRelationships();

    @Query(
        "select functionalFlow from FunctionalFlow functionalFlow left join fetch functionalFlow.interfaces where functionalFlow.id =:id"
    )
    Optional<FunctionalFlow> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<FunctionalFlow> findByAlias(String flowAlias);
}
