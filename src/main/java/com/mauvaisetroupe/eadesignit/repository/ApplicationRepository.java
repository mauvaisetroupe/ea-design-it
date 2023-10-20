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
 * Spring Data JPA repository for the Application entity.
 *
 * When extending this class, extend ApplicationRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ApplicationRepository extends ApplicationRepositoryWithBagRelationships, JpaRepository<Application, Long> {
    default Optional<Application> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Application> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Application> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select application from Application application left join fetch application.owner left join fetch application.itOwner left join fetch application.businessOwner",
        countQuery = "select count(application) from Application application"
    )
    Page<Application> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select application from Application application left join fetch application.owner left join fetch application.itOwner left join fetch application.businessOwner"
    )
    List<Application> findAllWithToOneRelationships();

    @Query(
        "select application from Application application left join fetch application.owner left join fetch application.itOwner left join fetch application.businessOwner where application.id =:id"
    )
    Optional<Application> findOneWithToOneRelationships(@Param("id") Long id);
}
