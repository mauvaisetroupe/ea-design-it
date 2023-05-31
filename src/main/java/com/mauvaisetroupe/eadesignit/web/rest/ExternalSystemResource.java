package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ExternalSystem;
import com.mauvaisetroupe.eadesignit.repository.ExternalSystemRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.ExternalSystem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExternalSystemResource {

    private final Logger log = LoggerFactory.getLogger(ExternalSystemResource.class);

    private static final String ENTITY_NAME = "externalSystem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExternalSystemRepository externalSystemRepository;

    public ExternalSystemResource(ExternalSystemRepository externalSystemRepository) {
        this.externalSystemRepository = externalSystemRepository;
    }

    /**
     * {@code POST  /external-systems} : Create a new externalSystem.
     *
     * @param externalSystem the externalSystem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new externalSystem, or with status {@code 400 (Bad Request)} if the externalSystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/external-systems")
    public ResponseEntity<ExternalSystem> createExternalSystem(@RequestBody ExternalSystem externalSystem) throws URISyntaxException {
        log.debug("REST request to save ExternalSystem : {}", externalSystem);
        if (externalSystem.getId() != null) {
            throw new BadRequestAlertException("A new externalSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExternalSystem result = externalSystemRepository.save(externalSystem);
        return ResponseEntity
            .created(new URI("/api/external-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /external-systems/:id} : Updates an existing externalSystem.
     *
     * @param id the id of the externalSystem to save.
     * @param externalSystem the externalSystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalSystem,
     * or with status {@code 400 (Bad Request)} if the externalSystem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the externalSystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/external-systems/{id}")
    public ResponseEntity<ExternalSystem> updateExternalSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExternalSystem externalSystem
    ) throws URISyntaxException {
        log.debug("REST request to update ExternalSystem : {}, {}", id, externalSystem);
        if (externalSystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalSystem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExternalSystem result = externalSystemRepository.save(externalSystem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, externalSystem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /external-systems/:id} : Partial updates given fields of an existing externalSystem, field will ignore if it is null
     *
     * @param id the id of the externalSystem to save.
     * @param externalSystem the externalSystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalSystem,
     * or with status {@code 400 (Bad Request)} if the externalSystem is not valid,
     * or with status {@code 404 (Not Found)} if the externalSystem is not found,
     * or with status {@code 500 (Internal Server Error)} if the externalSystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/external-systems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExternalSystem> partialUpdateExternalSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExternalSystem externalSystem
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExternalSystem partially : {}, {}", id, externalSystem);
        if (externalSystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalSystem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalSystemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExternalSystem> result = externalSystemRepository
            .findById(externalSystem.getId())
            .map(existingExternalSystem -> {
                if (externalSystem.getExternalSystemID() != null) {
                    existingExternalSystem.setExternalSystemID(externalSystem.getExternalSystemID());
                }

                return existingExternalSystem;
            })
            .map(externalSystemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, externalSystem.getId().toString())
        );
    }

    /**
     * {@code GET  /external-systems} : get all the externalSystems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of externalSystems in body.
     */
    @GetMapping("/external-systems")
    public List<ExternalSystem> getAllExternalSystems() {
        log.debug("REST request to get all ExternalSystems");
        return externalSystemRepository.findAll();
    }

    /**
     * {@code GET  /external-systems/:id} : get the "id" externalSystem.
     *
     * @param id the id of the externalSystem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the externalSystem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/external-systems/{id}")
    public ResponseEntity<ExternalSystem> getExternalSystem(@PathVariable Long id) {
        log.debug("REST request to get ExternalSystem : {}", id);
        Optional<ExternalSystem> externalSystem = externalSystemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(externalSystem);
    }

    /**
     * {@code DELETE  /external-systems/:id} : delete the "id" externalSystem.
     *
     * @param id the id of the externalSystem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/external-systems/{id}")
    public ResponseEntity<Void> deleteExternalSystem(@PathVariable Long id) {
        log.debug("REST request to delete ExternalSystem : {}", id);
        externalSystemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
