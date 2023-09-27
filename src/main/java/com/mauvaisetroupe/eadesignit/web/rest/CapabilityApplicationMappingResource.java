package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import com.mauvaisetroupe.eadesignit.repository.CapabilityApplicationMappingRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CapabilityApplicationMappingResource {

    private final Logger log = LoggerFactory.getLogger(CapabilityApplicationMappingResource.class);

    private static final String ENTITY_NAME = "capabilityApplicationMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapabilityApplicationMappingRepository capabilityApplicationMappingRepository;

    public CapabilityApplicationMappingResource(CapabilityApplicationMappingRepository capabilityApplicationMappingRepository) {
        this.capabilityApplicationMappingRepository = capabilityApplicationMappingRepository;
    }

    /**
     * {@code POST  /capability-application-mappings} : Create a new capabilityApplicationMapping.
     *
     * @param capabilityApplicationMapping the capabilityApplicationMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capabilityApplicationMapping, or with status {@code 400 (Bad Request)} if the capabilityApplicationMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capability-application-mappings")
    public ResponseEntity<CapabilityApplicationMapping> createCapabilityApplicationMapping(
        @RequestBody CapabilityApplicationMapping capabilityApplicationMapping
    ) throws URISyntaxException {
        log.debug("REST request to save CapabilityApplicationMapping : {}", capabilityApplicationMapping);
        if (capabilityApplicationMapping.getId() != null) {
            throw new BadRequestAlertException("A new capabilityApplicationMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CapabilityApplicationMapping result = capabilityApplicationMappingRepository.save(capabilityApplicationMapping);
        return ResponseEntity
            .created(new URI("/api/capability-application-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /capability-application-mappings/:id} : Updates an existing capabilityApplicationMapping.
     *
     * @param id the id of the capabilityApplicationMapping to save.
     * @param capabilityApplicationMapping the capabilityApplicationMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capabilityApplicationMapping,
     * or with status {@code 400 (Bad Request)} if the capabilityApplicationMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capabilityApplicationMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capability-application-mappings/{id}")
    public ResponseEntity<CapabilityApplicationMapping> updateCapabilityApplicationMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CapabilityApplicationMapping capabilityApplicationMapping
    ) throws URISyntaxException {
        log.debug("REST request to update CapabilityApplicationMapping : {}, {}", id, capabilityApplicationMapping);
        if (capabilityApplicationMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capabilityApplicationMapping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capabilityApplicationMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CapabilityApplicationMapping result = capabilityApplicationMappingRepository.save(capabilityApplicationMapping);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, capabilityApplicationMapping.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /capability-application-mappings/:id} : Partial updates given fields of an existing capabilityApplicationMapping, field will ignore if it is null
     *
     * @param id the id of the capabilityApplicationMapping to save.
     * @param capabilityApplicationMapping the capabilityApplicationMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capabilityApplicationMapping,
     * or with status {@code 400 (Bad Request)} if the capabilityApplicationMapping is not valid,
     * or with status {@code 404 (Not Found)} if the capabilityApplicationMapping is not found,
     * or with status {@code 500 (Internal Server Error)} if the capabilityApplicationMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capability-application-mappings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CapabilityApplicationMapping> partialUpdateCapabilityApplicationMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CapabilityApplicationMapping capabilityApplicationMapping
    ) throws URISyntaxException {
        log.debug("REST request to partial update CapabilityApplicationMapping partially : {}, {}", id, capabilityApplicationMapping);
        if (capabilityApplicationMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capabilityApplicationMapping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capabilityApplicationMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CapabilityApplicationMapping> result = capabilityApplicationMappingRepository
            .findById(capabilityApplicationMapping.getId())
            .map(existingCapabilityApplicationMapping -> {
                return existingCapabilityApplicationMapping;
            })
            .map(capabilityApplicationMappingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, capabilityApplicationMapping.getId().toString())
        );
    }

    /**
     * {@code GET  /capability-application-mappings} : get all the capabilityApplicationMappings.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capabilityApplicationMappings in body.
     */
    @GetMapping("/capability-application-mappings")
    public List<CapabilityApplicationMapping> getAllCapabilityApplicationMappings(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all CapabilityApplicationMappings");
        if (eagerload) {
            return capabilityApplicationMappingRepository.findAllWithEagerRelationships();
        } else {
            return capabilityApplicationMappingRepository.findAll();
        }
    }

    /**
     * {@code GET  /capability-application-mappings/:id} : get the "id" capabilityApplicationMapping.
     *
     * @param id the id of the capabilityApplicationMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capabilityApplicationMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capability-application-mappings/{id}")
    public ResponseEntity<CapabilityApplicationMapping> getCapabilityApplicationMapping(@PathVariable Long id) {
        log.debug("REST request to get CapabilityApplicationMapping : {}", id);
        Optional<CapabilityApplicationMapping> capabilityApplicationMapping = capabilityApplicationMappingRepository.findOneWithEagerRelationships(
            id
        );
        return ResponseUtil.wrapOrNotFound(capabilityApplicationMapping);
    }

    /**
     * {@code DELETE  /capability-application-mappings/:id} : delete the "id" capabilityApplicationMapping.
     *
     * @param id the id of the capabilityApplicationMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capability-application-mappings/{id}")
    public ResponseEntity<Void> deleteCapabilityApplicationMapping(@PathVariable Long id) {
        log.debug("REST request to delete CapabilityApplicationMapping : {}", id);
        capabilityApplicationMappingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
