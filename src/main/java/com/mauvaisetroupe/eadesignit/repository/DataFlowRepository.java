package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DataFlow entity.
 */
@Repository
public interface DataFlowRepository extends JpaRepository<DataFlow, Long> {
    @Query(
        value = "select distinct dataFlow from DataFlow dataFlow left join fetch dataFlow.functionalFlows",
        countQuery = "select count(distinct dataFlow) from DataFlow dataFlow"
    )
    Page<DataFlow> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct dataFlow from DataFlow dataFlow left join fetch dataFlow.functionalFlows")
    List<DataFlow> findAllWithEagerRelationships();

    @Query("select dataFlow from DataFlow dataFlow left join fetch dataFlow.functionalFlows where dataFlow.id =:id")
    Optional<DataFlow> findOneWithEagerRelationships(@Param("id") Long id);
}
