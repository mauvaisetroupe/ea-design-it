package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.view.LandscapeLight;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LandscapeView entity.
 */
@Repository
public interface LandscapeViewRepository extends JpaRepository<LandscapeView, Long> {
    @Query(
        value = "select distinct landscapeView from LandscapeView landscapeView left join fetch landscapeView.flows",
        countQuery = "select count(distinct landscapeView) from LandscapeView landscapeView"
    )
    Page<LandscapeView> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct landscapeView from LandscapeView landscapeView left join fetch landscapeView.flows")
    List<LandscapeView> findAllWithEagerRelationships();

    @Query(
        value = "select l from LandscapeView l " +
        " left join fetch l.owner o " +
        " left join fetch l.flows f " +
        " left join fetch f.landscapes l2 " +
        " left join fetch f.steps s " +
        " left join fetch s.group g " +
        " left join fetch s.flowInterface fi " +
        " left join fetch fi.protocol p " +
        " left join fetch fi.source so " +
        " left join fetch fi.sourceComponent sc " +
        " left join fetch fi.target ta " +
        " left join fetch fi.targetComponent tc " +
        " left join fetch l.capabilityApplicationMappings cm " +
        " left join fetch cm.application a " +
        " left join fetch cm.capability ca " +
        " left join fetch l.dataObjects do " +
        " where l.id =:id"
    )
    Optional<LandscapeView> findOneWithEagerRelationships(@Param("id") Long id);

    LandscapeView findByDiagramNameIgnoreCase(String diagramName);

    List<LandscapeLight> findAllByOrderByDiagramNameAsc();
}
