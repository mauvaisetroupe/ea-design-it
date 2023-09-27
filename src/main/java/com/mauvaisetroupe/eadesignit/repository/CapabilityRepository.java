package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Capability entity.
 */
@Repository
public interface CapabilityRepository extends JpaRepository<Capability, Long> {
    default Optional<Capability> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Capability> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Capability> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct capability from Capability capability left join fetch capability.parent",
        countQuery = "select count(distinct capability) from Capability capability"
    )
    Page<Capability> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct capability from Capability capability left join fetch capability.parent")
    List<Capability> findAllWithToOneRelationships();

    @Query("select capability from Capability capability left join fetch capability.parent where capability.id =:id")
    Optional<Capability> findOneWithToOneRelationships(@Param("id") Long id);
}
