package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.domain.DataObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DataObjectRepositoryWithBagRelationshipsImpl implements DataObjectRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<DataObject> fetchBagRelationships(Optional<DataObject> dataObject) {
        return dataObject.map(this::fetchTechnologies).map(this::fetchLandscapes);
    }

    @Override
    public Page<DataObject> fetchBagRelationships(Page<DataObject> dataObjects) {
        return new PageImpl<>(fetchBagRelationships(dataObjects.getContent()), dataObjects.getPageable(), dataObjects.getTotalElements());
    }

    @Override
    public List<DataObject> fetchBagRelationships(List<DataObject> dataObjects) {
        return Optional.of(dataObjects).map(this::fetchTechnologies).map(this::fetchLandscapes).orElse(Collections.emptyList());
    }

    DataObject fetchTechnologies(DataObject result) {
        return entityManager
            .createQuery(
                "select dataObject from DataObject dataObject left join fetch dataObject.technologies where dataObject.id = :id",
                DataObject.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<DataObject> fetchTechnologies(List<DataObject> dataObjects) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, dataObjects.size()).forEach(index -> order.put(dataObjects.get(index).getId(), index));
        List<DataObject> result = entityManager
            .createQuery(
                "select dataObject from DataObject dataObject left join fetch dataObject.technologies where dataObject in :dataObjects",
                DataObject.class
            )
            .setParameter("dataObjects", dataObjects)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    DataObject fetchLandscapes(DataObject result) {
        return entityManager
            .createQuery(
                "select dataObject from DataObject dataObject left join fetch dataObject.landscapes where dataObject.id = :id",
                DataObject.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<DataObject> fetchLandscapes(List<DataObject> dataObjects) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, dataObjects.size()).forEach(index -> order.put(dataObjects.get(index).getId(), index));
        List<DataObject> result = entityManager
            .createQuery(
                "select dataObject from DataObject dataObject left join fetch dataObject.landscapes where dataObject in :dataObjects",
                DataObject.class
            )
            .setParameter("dataObjects", dataObjects)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
