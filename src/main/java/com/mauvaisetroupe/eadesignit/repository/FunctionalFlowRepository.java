package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FunctionalFlow entity.
 */
@Repository
public interface FunctionalFlowRepository extends JpaRepository<FunctionalFlow, Long> {
    default Optional<FunctionalFlow> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FunctionalFlow> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FunctionalFlow> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select functionalFlow from FunctionalFlow functionalFlow left join fetch functionalFlow.owner",
        countQuery = "select count(functionalFlow) from FunctionalFlow functionalFlow"
    )
    Page<FunctionalFlow> findAllWithToOneRelationships(Pageable pageable);

    @Query("select functionalFlow from FunctionalFlow functionalFlow left join fetch functionalFlow.owner")
    List<FunctionalFlow> findAllWithToOneRelationships();

    @Query("select functionalFlow from FunctionalFlow functionalFlow left join fetch functionalFlow.owner where functionalFlow.id =:id")
    Optional<FunctionalFlow> findOneWithToOneRelationships(@Param("id") Long id);
}
