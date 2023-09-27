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
 * Spring Data JPA repository for the LandscapeView entity.
 *
 * When extending this class, extend LandscapeViewRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface LandscapeViewRepository extends LandscapeViewRepositoryWithBagRelationships, JpaRepository<LandscapeView, Long> {
    default Optional<LandscapeView> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<LandscapeView> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<LandscapeView> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct landscapeView from LandscapeView landscapeView left join fetch landscapeView.owner",
        countQuery = "select count(distinct landscapeView) from LandscapeView landscapeView"
    )
    Page<LandscapeView> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct landscapeView from LandscapeView landscapeView left join fetch landscapeView.owner")
    List<LandscapeView> findAllWithToOneRelationships();

    @Query("select landscapeView from LandscapeView landscapeView left join fetch landscapeView.owner where landscapeView.id =:id")
    Optional<LandscapeView> findOneWithToOneRelationships(@Param("id") Long id);
}
