package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
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
public class DataFlowRepositoryWithBagRelationshipsImpl implements DataFlowRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<DataFlow> fetchBagRelationships(Optional<DataFlow> dataFlow) {
        return dataFlow.map(this::fetchFunctionalFlows);
    }

    @Override
    public Page<DataFlow> fetchBagRelationships(Page<DataFlow> dataFlows) {
        return new PageImpl<>(fetchBagRelationships(dataFlows.getContent()), dataFlows.getPageable(), dataFlows.getTotalElements());
    }

    @Override
    public List<DataFlow> fetchBagRelationships(List<DataFlow> dataFlows) {
        return Optional.of(dataFlows).map(this::fetchFunctionalFlows).orElse(Collections.emptyList());
    }

    DataFlow fetchFunctionalFlows(DataFlow result) {
        return entityManager
            .createQuery(
                "select dataFlow from DataFlow dataFlow left join fetch dataFlow.functionalFlows where dataFlow is :dataFlow",
                DataFlow.class
            )
            .setParameter("dataFlow", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<DataFlow> fetchFunctionalFlows(List<DataFlow> dataFlows) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, dataFlows.size()).forEach(index -> order.put(dataFlows.get(index).getId(), index));
        List<DataFlow> result = entityManager
            .createQuery(
                "select distinct dataFlow from DataFlow dataFlow left join fetch dataFlow.functionalFlows where dataFlow in :dataFlows",
                DataFlow.class
            )
            .setParameter("dataFlows", dataFlows)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
