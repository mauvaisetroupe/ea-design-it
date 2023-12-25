package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DataObject entity.
 *
 * When extending this class, extend DataObjectRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DataObjectRepository extends DataObjectRepositoryWithBagRelationships, JpaRepository<DataObject, Long> {
    default Optional<DataObject> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<DataObject> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<DataObject> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select dataObject from DataObject dataObject left join fetch dataObject.application left join fetch dataObject.owner left join fetch dataObject.parent left join fetch dataObject.businessObject",
        countQuery = "select count(dataObject) from DataObject dataObject"
    )
    Page<DataObject> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select dataObject from DataObject dataObject left join fetch dataObject.application left join fetch dataObject.owner left join fetch dataObject.parent left join fetch dataObject.businessObject"
    )
    List<DataObject> findAllWithToOneRelationships();

    @Query(
        "select dataObject from DataObject dataObject left join fetch dataObject.application left join fetch dataObject.owner left join fetch dataObject.parent left join fetch dataObject.businessObject where dataObject.id =:id"
    )
    Optional<DataObject> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select do_0 from DataObject do_0 " +
        " left join fetch do_0.application a_0" +
        " left join fetch do_0.owner " +
        " left join fetch do_0.parent " +
        " left join fetch do_0.businessObject " +
        " left join fetch do_0.components do_1" +
        " left join fetch do_1.application a_1" +
        " left join fetch do_1.components do_2 " +
        " left join fetch do_1.application a_2" +
        " where do_0.id =:id"
    )
    Optional<DataObject> findOneWithAllChildrens(@Param("id") Long id);

    @Query(
        "select do_0 from DataObject do_0 " +
        " left join fetch do_0.application a_0" +
        " left join fetch do_0.owner " +
        " left join fetch do_0.parent " +
        " left join fetch do_0.businessObject " +
        " left join fetch do_0.components do_1" +
        " left join fetch do_1.application a_1" +
        " left join fetch do_1.components do_2 " +
        " left join fetch do_1.application a_2"
    )
    List<DataObject> findAllWithAllChildrens();

    Optional<DataObject> findByNameIgnoreCaseAndParentAndApplication(String boName, DataObject parent, Application application);
    List<DataObject> findByNameIgnoreCase(String boName);
}
