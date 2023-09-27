package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.ExternalReference;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExternalReference entity.
 */
@Repository
public interface ExternalReferenceRepository extends JpaRepository<ExternalReference, Long> {
    default Optional<ExternalReference> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ExternalReference> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ExternalReference> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct externalReference from ExternalReference externalReference left join fetch externalReference.externalSystem",
        countQuery = "select count(distinct externalReference) from ExternalReference externalReference"
    )
    Page<ExternalReference> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct externalReference from ExternalReference externalReference left join fetch externalReference.externalSystem")
    List<ExternalReference> findAllWithToOneRelationships();

    @Query(
        "select externalReference from ExternalReference externalReference left join fetch externalReference.externalSystem where externalReference.id =:id"
    )
    Optional<ExternalReference> findOneWithToOneRelationships(@Param("id") Long id);
}
