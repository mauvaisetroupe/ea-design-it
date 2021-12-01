package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Application;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Application entity.
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query(
        value = "select distinct application from Application application left join fetch application.categories left join fetch application.technologies",
        countQuery = "select count(distinct application) from Application application"
    )
    Page<Application> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct application from Application application left join fetch application.categories left join fetch application.technologies"
    )
    List<Application> findAllWithEagerRelationships();

    @Query(
        "select application from Application application left join fetch application.categories left join fetch application.technologies where application.id =:id"
    )
    Optional<Application> findOneWithEagerRelationships(@Param("id") Long id);

    Application findByNameIgnoreCase(String name);
    Optional<Application> findByAlias(String alias);
}
