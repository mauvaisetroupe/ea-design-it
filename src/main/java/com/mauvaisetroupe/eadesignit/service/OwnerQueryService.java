package com.mauvaisetroupe.eadesignit.service;

import com.mauvaisetroupe.eadesignit.domain.*; // for static metamodels
import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
import com.mauvaisetroupe.eadesignit.service.criteria.OwnerCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Owner} entities in the database.
 * The main input is a {@link OwnerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Owner} or a {@link Page} of {@link Owner} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OwnerQueryService extends QueryService<Owner> {

    private final Logger log = LoggerFactory.getLogger(OwnerQueryService.class);

    private final OwnerRepository ownerRepository;

    public OwnerQueryService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * Return a {@link List} of {@link Owner} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Owner> findByCriteria(OwnerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Owner> specification = createSpecification(criteria);
        return ownerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Owner} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Owner> findByCriteria(OwnerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Owner> specification = createSpecification(criteria);
        return ownerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OwnerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Owner> specification = createSpecification(criteria);
        return ownerRepository.count(specification);
    }

    /**
     * Function to convert {@link OwnerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Owner> createSpecification(OwnerCriteria criteria) {
        Specification<Owner> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Owner_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Owner_.name));
            }
        }
        return specification;
    }
}
