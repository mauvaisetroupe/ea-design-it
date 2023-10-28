package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.Capability}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CapabilityResource {

    private final Logger log = LoggerFactory.getLogger(CapabilityResource.class);

    private static final String ENTITY_NAME = "capability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapabilityRepository capabilityRepository;

    public CapabilityResource(CapabilityRepository capabilityRepository) {
        this.capabilityRepository = capabilityRepository;
    }

    /**
     * {@code POST  /capabilities} : Create a new capability.
     *
     * @param capability the capability to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capability, or with status {@code 400 (Bad Request)} if the capability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capabilities")
    public ResponseEntity<Capability> createCapability(@Valid @RequestBody Capability capability) throws URISyntaxException {
        log.debug("REST request to save Capability : {}", capability);
        if (capability.getId() != null) {
            throw new BadRequestAlertException("A new capability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Capability result = capabilityRepository.save(capability);
        return ResponseEntity
            .created(new URI("/api/capabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /capabilities/:id} : Updates an existing capability.
     *
     * @param id the id of the capability to save.
     * @param capability the capability to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capability,
     * or with status {@code 400 (Bad Request)} if the capability is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capability couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capabilities/{id}")
    public ResponseEntity<Capability> updateCapability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Capability capability
    ) throws URISyntaxException {
        log.debug("REST request to update Capability : {}, {}", id, capability);
        if (capability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capability.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Capability result = capabilityRepository.save(capability);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, capability.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /capabilities/:id} : Partial updates given fields of an existing capability, field will ignore if it is null
     *
     * @param id the id of the capability to save.
     * @param capability the capability to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capability,
     * or with status {@code 400 (Bad Request)} if the capability is not valid,
     * or with status {@code 404 (Not Found)} if the capability is not found,
     * or with status {@code 500 (Internal Server Error)} if the capability couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capabilities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Capability> partialUpdateCapability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Capability capability
    ) throws URISyntaxException {
        log.debug("REST request to partial update Capability partially : {}, {}", id, capability);
        if (capability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capability.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Capability> result = capabilityRepository
            .findById(capability.getId())
            .map(existingCapability -> {
                if (capability.getName() != null) {
                    existingCapability.setName(capability.getName());
                }
                if (capability.getDescription() != null) {
                    existingCapability.setDescription(capability.getDescription());
                }
                if (capability.getComment() != null) {
                    existingCapability.setComment(capability.getComment());
                }
                if (capability.getLevel() != null) {
                    existingCapability.setLevel(capability.getLevel());
                }

                return existingCapability;
            })
            .map(capabilityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, capability.getId().toString())
        );
    }

    /**
     * {@code GET  /capabilities} : get all the capabilities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capabilities in body.
     */
    @GetMapping("/capabilities")
    public List<Capability> getAllCapabilities() {
        log.debug("REST request to get all Capabilities");
        return capabilityRepository.findAll();
    }

    /**
     * {@code GET  /capabilities/:id} : get the "id" capability.
     *
     * @param id the id of the capability to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capability, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capabilities/{id}")
    public ResponseEntity<Capability> getCapability(@PathVariable Long id) {
        log.debug("REST request to get Capability : {}", id);
        Optional<Capability> capability = capabilityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(capability);
    }

    @GetMapping("/capabilities/find-root")
    public Capability getRootCapability() {
        List<Capability> capabilities = capabilityRepository.findByLevel(-2);
        Assert.isTrue(capabilities.size() == 1, "Should exist and be unique");
        Capability rootCapability = capabilityRepository.findById(capabilities.get(0).getId()).orElseThrow();
        return rootCapability;
    }

    /**
     * {@code DELETE  /capabilities/:id} : delete the "id" capability.
     *
     * @param id the id of the capability to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capabilities/{id}")
    public ResponseEntity<Void> deleteCapability(@PathVariable Long id) {
        log.debug("REST request to delete Capability : {}", id);
        capabilityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
