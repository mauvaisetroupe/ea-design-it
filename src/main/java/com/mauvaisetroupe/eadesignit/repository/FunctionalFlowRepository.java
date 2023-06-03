package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.repository.view.FunctionalFlowLight;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FunctionalFlow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FunctionalFlowRepository extends JpaRepository<FunctionalFlow, Long> {
    Optional<FunctionalFlow> findByAlias(String flowAlias);

    @Query(value = "select distinct(f.alias) from FunctionalFlow f")
    List<String> findAlias();

    List<FunctionalFlowLight> findAllProjectedBy();

    @Query(
        value = "select f from FunctionalFlow f" +
        " left join fetch f.steps s " +
        " left join fetch s.flowInterface i " +
        " left join fetch i.source" +
        " left join fetch i.target" +
        " left join fetch i.sourceComponent" +
        " left join fetch i.targetComponent " +
        " left join fetch i.protocol" +
        " left join fetch i.owner" +
        " left join fetch i.dataFlows" +
        " where f.id=:id"
    )
    Optional<FunctionalFlow> findOneWithEagerRelationships(@Param("id") Long id);
}
