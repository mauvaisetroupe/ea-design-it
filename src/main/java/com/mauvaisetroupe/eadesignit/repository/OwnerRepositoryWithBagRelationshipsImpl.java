package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.Owner;
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
public class OwnerRepositoryWithBagRelationshipsImpl implements OwnerRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Owner> fetchBagRelationships(Optional<Owner> owner) {
        return owner.map(this::fetchUsers);
    }

    @Override
    public Page<Owner> fetchBagRelationships(Page<Owner> owners) {
        return new PageImpl<>(fetchBagRelationships(owners.getContent()), owners.getPageable(), owners.getTotalElements());
    }

    @Override
    public List<Owner> fetchBagRelationships(List<Owner> owners) {
        return Optional.of(owners).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Owner fetchUsers(Owner result) {
        return entityManager
            .createQuery("select owner from Owner owner left join fetch owner.users where owner is :owner", Owner.class)
            .setParameter("owner", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Owner> fetchUsers(List<Owner> owners) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, owners.size()).forEach(index -> order.put(owners.get(index).getId(), index));
        List<Owner> result = entityManager
            .createQuery("select distinct owner from Owner owner left join fetch owner.users where owner in :owners", Owner.class)
            .setParameter("owners", owners)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
