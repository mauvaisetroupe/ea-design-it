package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.repository.view.FunctionalFlowLight;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
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
        value = "select f from FunctionalFlow f " +
        "left join fetch f.steps s " +
        "left join fetch f.landscapes l " +
        "left join fetch l.owner " +
        "left join fetch s.flowInterface i " +
        "left join fetch s.group g " +
        "left join fetch i.source so " +
        "left join fetch i.target ta " +
        "where so=:appli or ta=:appli "
    )
    SortedSet<FunctionalFlow> findFunctionalFlowsForInterfacesIn(@Param("appli") Application application);

    SortedSet<FunctionalFlow> findByLandscapesIsEmpty();

    @Query(
        "select f from FunctionalFlow f " +
        "left join fetch f.steps s " +
        "left join fetch f.landscapes l " +
        "left join fetch l.owner " +
        "left join fetch s.flowInterface i " +
        "left join fetch s.group g " +
        "left join fetch i.source so " +
        "left join fetch i.target ta " +
        " where f.id =:id"
    )
    Optional<FunctionalFlow> findOneWithEagerRelationships(@Param("id") Long id);
}
