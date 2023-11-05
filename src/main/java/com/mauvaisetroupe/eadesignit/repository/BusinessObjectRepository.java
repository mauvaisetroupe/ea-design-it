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

    @Query(
        "select bo_0 from BusinessObject bo_0 " +
        " left join fetch bo_0.generalization bo_g" +
        " left join fetch bo_0.parent bo_p" +
        " left join fetch bo_0.specializations sp" +
        " left join fetch sp.components sp_bo_1" +
        " left join fetch sp.dataObjects sp_do_1" +
        " left join fetch sp.components sp_do_1_1" +
        " left join fetch bo_0.components bo_1" +
        " left join fetch bo_0.dataObjects do_1" +
        " left join fetch do_1.components do_1_1" +
        " left join fetch bo_1.components bo_2" +
        " left join fetch bo_1.dataObjects do_2" +
        " left join fetch do_2.components do_2_1" +
        " where bo_0.id =:id"
    )
    Optional<BusinessObject> findOneWithAllChildrens(@Param("id") Long id);

    @Query(
        "select bo_0 from BusinessObject bo_0 " +
        " left join fetch bo_0.generalization bo_g" +
        " left join fetch bo_0.parent bo_p" +
        " left join fetch bo_0.specializations sp" +
        " left join fetch sp.components sp_bo_1" +
        " left join fetch sp.dataObjects sp_do_1" +
        " left join fetch sp.components sp_do_1_1" +
        " left join fetch bo_0.components bo_1" +
        " left join fetch bo_0.dataObjects do_1" +
        " left join fetch do_1.application a" +
        " left join fetch do_1.components do_1_1" +
        " left join fetch bo_1.components bo_2" +
        " left join fetch bo_1.dataObjects do_2" +
        " left join fetch do_2.components do_2_1"
    )
    List<BusinessObject> findAllWithAllChildrens();
}
