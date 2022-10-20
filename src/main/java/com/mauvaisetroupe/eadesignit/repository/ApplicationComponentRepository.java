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
 * Spring Data SQL repository for the ApplicationComponent entity.
 */
@Repository
public interface ApplicationComponentRepository extends JpaRepository<ApplicationComponent, Long> {
    @Query(
        value = "select distinct applicationComponent from ApplicationComponent applicationComponent left join fetch applicationComponent.categories left join fetch applicationComponent.technologies",
        countQuery = "select count(distinct applicationComponent) from ApplicationComponent applicationComponent"
    )
    Page<ApplicationComponent> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct applicationComponent from ApplicationComponent applicationComponent left join fetch applicationComponent.categories left join fetch applicationComponent.technologies"
    )
    List<ApplicationComponent> findAllWithEagerRelationships();

    @Query(
        "select applicationComponent from ApplicationComponent applicationComponent left join fetch applicationComponent.categories left join fetch applicationComponent.technologies where applicationComponent.id =:id"
    )
    Optional<ApplicationComponent> findOneWithEagerRelationships(@Param("id") Long id);

    ApplicationComponent findByNameIgnoreCase(String name);

    Optional<ApplicationComponent> findByAlias(String alias);
}
