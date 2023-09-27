package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ExternalReference;
import com.mauvaisetroupe.eadesignit.repository.ExternalReferenceRepository;
import com.mauvaisetroupe.eadesignit.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.ExternalReference}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExternalReferenceResource {

    private final Logger log = LoggerFactory.getLogger(ExternalReferenceResource.class);

    private static final String ENTITY_NAME = "externalReference";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExternalReferenceRepository externalReferenceRepository;

    public ExternalReferenceResource(ExternalReferenceRepository externalReferenceRepository) {
        this.externalReferenceRepository = externalReferenceRepository;
    }

    /**
     * {@code POST  /external-references} : Create a new externalReference.
     *
     * @param externalReference the externalReference to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new externalReference, or with status {@code 400 (Bad Request)} if the externalReference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/external-references")
    public ResponseEntity<ExternalReference> createExternalReference(@RequestBody ExternalReference externalReference)
        throws URISyntaxException {
        log.debug("REST request to save ExternalReference : {}", externalReference);
        if (externalReference.getId() != null) {
            throw new BadRequestAlertException("A new externalReference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExternalReference result = externalReferenceRepository.save(externalReference);
        return ResponseEntity
            .created(new URI("/api/external-references/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /external-references/:id} : Updates an existing externalReference.
     *
     * @param id the id of the externalReference to save.
     * @param externalReference the externalReference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalReference,
     * or with status {@code 400 (Bad Request)} if the externalReference is not valid,
     * or with status {@code 500 (Internal Server Error)} if the externalReference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/external-references/{id}")
    public ResponseEntity<ExternalReference> updateExternalReference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExternalReference externalReference
    ) throws URISyntaxException {
        log.debug("REST request to update ExternalReference : {}, {}", id, externalReference);
        if (externalReference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalReference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalReferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExternalReference result = externalReferenceRepository.save(externalReference);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, externalReference.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /external-references/:id} : Partial updates given fields of an existing externalReference, field will ignore if it is null
     *
     * @param id the id of the externalReference to save.
     * @param externalReference the externalReference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalReference,
     * or with status {@code 400 (Bad Request)} if the externalReference is not valid,
     * or with status {@code 404 (Not Found)} if the externalReference is not found,
     * or with status {@code 500 (Internal Server Error)} if the externalReference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/external-references/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExternalReference> partialUpdateExternalReference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExternalReference externalReference
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExternalReference partially : {}, {}", id, externalReference);
        if (externalReference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalReference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalReferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExternalReference> result = externalReferenceRepository
            .findById(externalReference.getId())
            .map(existingExternalReference -> {
                if (externalReference.getExternalID() != null) {
                    existingExternalReference.setExternalID(externalReference.getExternalID());
                }

                return existingExternalReference;
            })
            .map(externalReferenceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, externalReference.getId().toString())
        );
    }

    /**
     * {@code GET  /external-references} : get all the externalReferences.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of externalReferences in body.
     */
    @GetMapping("/external-references")
    public List<ExternalReference> getAllExternalReferences(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ExternalReferences");
        if (eagerload) {
            return externalReferenceRepository.findAllWithEagerRelationships();
        } else {
            return externalReferenceRepository.findAll();
        }
    }

    /**
     * {@code GET  /external-references/:id} : get the "id" externalReference.
     *
     * @param id the id of the externalReference to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the externalReference, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/external-references/{id}")
    public ResponseEntity<ExternalReference> getExternalReference(@PathVariable Long id) {
        log.debug("REST request to get ExternalReference : {}", id);
        Optional<ExternalReference> externalReference = externalReferenceRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(externalReference);
    }

    /**
     * {@code DELETE  /external-references/:id} : delete the "id" externalReference.
     *
     * @param id the id of the externalReference to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/external-references/{id}")
    public ResponseEntity<Void> deleteExternalReference(@PathVariable Long id) {
        log.debug("REST request to delete ExternalReference : {}", id);
        externalReferenceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
