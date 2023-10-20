package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FlowInterface entity.
 */
@Repository
public interface FlowInterfaceRepository extends JpaRepository<FlowInterface, Long> {
    default Optional<FlowInterface> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FlowInterface> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FlowInterface> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select flowInterface from FlowInterface flowInterface left join fetch flowInterface.source left join fetch flowInterface.target left join fetch flowInterface.sourceComponent left join fetch flowInterface.targetComponent left join fetch flowInterface.protocol left join fetch flowInterface.owner",
        countQuery = "select count(flowInterface) from FlowInterface flowInterface"
    )
    Page<FlowInterface> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select flowInterface from FlowInterface flowInterface left join fetch flowInterface.source left join fetch flowInterface.target left join fetch flowInterface.sourceComponent left join fetch flowInterface.targetComponent left join fetch flowInterface.protocol left join fetch flowInterface.owner"
    )
    List<FlowInterface> findAllWithToOneRelationships();

    @Query(
        "select flowInterface from FlowInterface flowInterface left join fetch flowInterface.source left join fetch flowInterface.target left join fetch flowInterface.sourceComponent left join fetch flowInterface.targetComponent left join fetch flowInterface.protocol left join fetch flowInterface.owner where flowInterface.id =:id"
    )
    Optional<FlowInterface> findOneWithToOneRelationships(@Param("id") Long id);
}
