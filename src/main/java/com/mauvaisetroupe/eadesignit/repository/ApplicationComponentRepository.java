package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApplicationComponent entity.
 *
 * When extending this class, extend ApplicationComponentRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ApplicationComponentRepository
    extends ApplicationComponentRepositoryWithBagRelationships, JpaRepository<ApplicationComponent, Long> {
    default Optional<ApplicationComponent> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<ApplicationComponent> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<ApplicationComponent> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select applicationComponent from ApplicationComponent applicationComponent left join fetch applicationComponent.application",
        countQuery = "select count(applicationComponent) from ApplicationComponent applicationComponent"
    )
    Page<ApplicationComponent> findAllWithToOneRelationships(Pageable pageable);

    @Query("select applicationComponent from ApplicationComponent applicationComponent left join fetch applicationComponent.application")
    List<ApplicationComponent> findAllWithToOneRelationships();

    @Query(
        "select applicationComponent from ApplicationComponent applicationComponent left join fetch applicationComponent.application where applicationComponent.id =:id"
    )
    Optional<ApplicationComponent> findOneWithToOneRelationships(@Param("id") Long id);
}
