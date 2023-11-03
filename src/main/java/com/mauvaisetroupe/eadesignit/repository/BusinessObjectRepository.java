package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.BusinessObject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BusinessObject entity.
 */
@Repository
public interface BusinessObjectRepository extends JpaRepository<BusinessObject, Long> {
    default Optional<BusinessObject> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BusinessObject> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BusinessObject> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select businessObject from BusinessObject businessObject left join fetch businessObject.owner left join fetch businessObject.generalization left join fetch businessObject.parent",
        countQuery = "select count(businessObject) from BusinessObject businessObject"
    )
    Page<BusinessObject> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select businessObject from BusinessObject businessObject left join fetch businessObject.owner left join fetch businessObject.generalization left join fetch businessObject.parent"
    )
    List<BusinessObject> findAllWithToOneRelationships();

    @Query(
        "select businessObject from BusinessObject businessObject left join fetch businessObject.owner left join fetch businessObject.generalization left join fetch businessObject.parent where businessObject.id =:id"
    )
    Optional<BusinessObject> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<BusinessObject> findByNameIgnoreCase(String string);

    Optional<BusinessObject> findByNameIgnoreCaseAndParentNameIgnoreCase(String name, String parentName);
}
