package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FlowGroup entity.
 */
@Repository
public interface FlowGroupRepository extends JpaRepository<FlowGroup, Long> {
    default Optional<FlowGroup> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FlowGroup> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FlowGroup> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select flowGroup from FlowGroup flowGroup left join fetch flowGroup.flow",
        countQuery = "select count(flowGroup) from FlowGroup flowGroup"
    )
    Page<FlowGroup> findAllWithToOneRelationships(Pageable pageable);

    @Query("select flowGroup from FlowGroup flowGroup left join fetch flowGroup.flow")
    List<FlowGroup> findAllWithToOneRelationships();

    @Query("select flowGroup from FlowGroup flowGroup left join fetch flowGroup.flow where flowGroup.id =:id")
    Optional<FlowGroup> findOneWithToOneRelationships(@Param("id") Long id);
}
