package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DataFlow entity.
 *
 * When extending this class, extend DataFlowRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DataFlowRepository extends DataFlowRepositoryWithBagRelationships, JpaRepository<DataFlow, Long> {
    default Optional<DataFlow> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<DataFlow> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<DataFlow> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select dataFlow from DataFlow dataFlow left join fetch dataFlow.format left join fetch dataFlow.flowInterface",
        countQuery = "select count(dataFlow) from DataFlow dataFlow"
    )
    Page<DataFlow> findAllWithToOneRelationships(Pageable pageable);

    @Query("select dataFlow from DataFlow dataFlow left join fetch dataFlow.format left join fetch dataFlow.flowInterface")
    List<DataFlow> findAllWithToOneRelationships();

    @Query(
        "select dataFlow from DataFlow dataFlow left join fetch dataFlow.format left join fetch dataFlow.flowInterface where dataFlow.id =:id"
    )
    Optional<DataFlow> findOneWithToOneRelationships(@Param("id") Long id);
}
