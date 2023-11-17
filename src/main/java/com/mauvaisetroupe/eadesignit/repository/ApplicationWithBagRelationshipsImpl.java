package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Application;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
@Repository
public class ApplicationWithBagRelationshipsImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Application fetchDataObjects(Application landscapeView) {
        Application result = entityManager
            .createQuery(
                "select a from Application a " +
                " left join fetch a.dataObjects do" +
                " left join fetch do.businessObject " +
                " where a=:application",
                Application.class
            )
            .setParameter("application", landscapeView)
            .getSingleResult();
        return result;
    }
}
