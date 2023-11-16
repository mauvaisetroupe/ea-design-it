package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
@Repository
public class LandscapeWithBagRelationshipsImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public LandscapeView fetchDataObjects(LandscapeView landscapeView) {
        LandscapeView result = entityManager
            .createQuery(
                "select l from LandscapeView l " +
                " left join fetch l.dataObjects do" +
                " left join fetch do.businessObject " +
                " where l=:landscapeView",
                LandscapeView.class
            )
            .setParameter("landscapeView", landscapeView)
            .getSingleResult();
        return result;
    }

    public LandscapeView fetchCapanilityApplicationMapping(LandscapeView landscapeView) {
        LandscapeView result = entityManager
            .createQuery(
                "select l from LandscapeView l " +
                " left join fetch l.capabilityApplicationMappings cm " +
                " left join fetch cm.application a " + // needed to build landscape report (appli w/ capa not in landscape)
                " left join fetch cm.capability ca " +
                " left join fetch ca.parent ca_p_1 " +
                " left join fetch ca_p_1.parent ca_p_2" +
                " left join fetch ca_p_2.parent ca_p_3" +
                " left join fetch ca_p_3.parent ca_p_4" +
                " left join fetch ca_p_4.parent ca_p_5" +
                " left join fetch cm.landscapes cm_l " +
                " left join fetch cm_l.owner cm_l_o" +
                " where l=:landscapeView",
                LandscapeView.class
            )
            .setParameter("landscapeView", landscapeView)
            .getSingleResult();
        return result;
    }
}
