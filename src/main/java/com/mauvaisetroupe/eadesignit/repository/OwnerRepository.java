package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Owner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Owner entity.
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Query(
        value = "select distinct owner from Owner owner left join fetch owner.users",
        countQuery = "select count(distinct owner) from Owner owner"
    )
    Page<Owner> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct owner from Owner owner left join fetch owner.users")
    List<Owner> findAllWithEagerRelationships();

    @Query("select owner from Owner owner left join fetch owner.users where owner.id =:id")
    Optional<Owner> findOneWithEagerRelationships(@Param("id") Long id);
}
