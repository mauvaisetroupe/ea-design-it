package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
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

    @Query("select landscapeView from LandscapeView landscapeView left join fetch landscapeView.flows where landscapeView.id =:id")
    Optional<LandscapeView> findOneWithEagerRelationships(@Param("id") Long id);
}
