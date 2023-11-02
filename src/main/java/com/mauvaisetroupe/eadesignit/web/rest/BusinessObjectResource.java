package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.BusinessObject;
import com.mauvaisetroupe.eadesignit.repository.BusinessObjectRepository;
import com.mauvaisetroupe.eadesignit.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.BusinessObject}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BusinessObjectResource {

    private final Logger log = LoggerFactory.getLogger(BusinessObjectResource.class);

    private static final String ENTITY_NAME = "businessObject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessObjectRepository businessObjectRepository;

    public BusinessObjectResource(BusinessObjectRepository businessObjectRepository) {
        this.businessObjectRepository = businessObjectRepository;
    }

    /**
     * {@code POST  /business-objects} : Create a new businessObject.
     *
     * @param businessObject the businessObject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessObject, or with status {@code 400 (Bad Request)} if the businessObject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-objects")
    public ResponseEntity<BusinessObject> createBusinessObject(@Valid @RequestBody BusinessObject businessObject)
        throws URISyntaxException {
        log.debug("REST request to save BusinessObject : {}", businessObject);
        if (businessObject.getId() != null) {
            throw new BadRequestAlertException("A new businessObject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessObject result = businessObjectRepository.save(businessObject);
        return ResponseEntity
            .created(new URI("/api/business-objects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-objects/:id} : Updates an existing businessObject.
     *
     * @param id the id of the businessObject to save.
     * @param businessObject the businessObject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessObject,
     * or with status {@code 400 (Bad Request)} if the businessObject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessObject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-objects/{id}")
    public ResponseEntity<BusinessObject> updateBusinessObject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessObject businessObject
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessObject : {}, {}", id, businessObject);
        if (businessObject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessObject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessObjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessObject result = businessObjectRepository.save(businessObject);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessObject.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-objects/:id} : Partial updates given fields of an existing businessObject, field will ignore if it is null
     *
     * @param id the id of the businessObject to save.
     * @param businessObject the businessObject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessObject,
     * or with status {@code 400 (Bad Request)} if the businessObject is not valid,
     * or with status {@code 404 (Not Found)} if the businessObject is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessObject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-objects/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessObject> partialUpdateBusinessObject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessObject businessObject
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessObject partially : {}, {}", id, businessObject);
        if (businessObject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessObject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessObjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessObject> result = businessObjectRepository
            .findById(businessObject.getId())
            .map(existingBusinessObject -> {
                if (businessObject.getName() != null) {
                    existingBusinessObject.setName(businessObject.getName());
                }
                if (businessObject.getImplementable() != null) {
                    existingBusinessObject.setImplementable(businessObject.getImplementable());
                }

                return existingBusinessObject;
            })
            .map(businessObjectRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessObject.getId().toString())
        );
    }

    /**
     * {@code GET  /business-objects} : get all the businessObjects.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessObjects in body.
     */
    @GetMapping("/business-objects")
    public List<BusinessObject> getAllBusinessObjects(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all BusinessObjects");
        if (eagerload) {
            return businessObjectRepository.findAllWithEagerRelationships();
        } else {
            return businessObjectRepository.findAll();
        }
    }

    /**
     * {@code GET  /business-objects/:id} : get the "id" businessObject.
     *
     * @param id the id of the businessObject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessObject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-objects/{id}")
    public ResponseEntity<BusinessObject> getBusinessObject(@PathVariable Long id) {
        log.debug("REST request to get BusinessObject : {}", id);
        Optional<BusinessObject> businessObject = businessObjectRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(businessObject);
    }

    /**
     * {@code DELETE  /business-objects/:id} : delete the "id" businessObject.
     *
     * @param id the id of the businessObject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-objects/{id}")
    public ResponseEntity<Void> deleteBusinessObject(@PathVariable Long id) {
        log.debug("REST request to delete BusinessObject : {}", id);
        businessObjectRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
