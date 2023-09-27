package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class LandscapeViewRepositoryWithBagRelationshipsImpl implements LandscapeViewRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<LandscapeView> fetchBagRelationships(Optional<LandscapeView> landscapeView) {
        return landscapeView.map(this::fetchFlows);
    }

    @Override
    public Page<LandscapeView> fetchBagRelationships(Page<LandscapeView> landscapeViews) {
        return new PageImpl<>(
            fetchBagRelationships(landscapeViews.getContent()),
            landscapeViews.getPageable(),
            landscapeViews.getTotalElements()
        );
    }

    @Override
    public List<LandscapeView> fetchBagRelationships(List<LandscapeView> landscapeViews) {
        return Optional.of(landscapeViews).map(this::fetchFlows).orElse(Collections.emptyList());
    }

    LandscapeView fetchFlows(LandscapeView result) {
        return entityManager
            .createQuery(
                "select landscapeView from LandscapeView landscapeView left join fetch landscapeView.flows where landscapeView is :landscapeView",
                LandscapeView.class
            )
            .setParameter("landscapeView", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<LandscapeView> fetchFlows(List<LandscapeView> landscapeViews) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, landscapeViews.size()).forEach(index -> order.put(landscapeViews.get(index).getId(), index));
        List<LandscapeView> result = entityManager
            .createQuery(
                "select distinct landscapeView from LandscapeView landscapeView left join fetch landscapeView.flows where landscapeView in :landscapeViews",
                LandscapeView.class
            )
            .setParameter("landscapeViews", landscapeViews)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
