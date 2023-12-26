package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.OrganizationalEntity;
import com.mauvaisetroupe.eadesignit.repository.OrganizationalEntityRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.OrganizationalEntity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrganizationalEntityResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationalEntityResource.class);

    private static final String ENTITY_NAME = "organizationalEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationalEntityRepository organizationalEntityRepository;

    public OrganizationalEntityResource(OrganizationalEntityRepository organizationalEntityRepository) {
        this.organizationalEntityRepository = organizationalEntityRepository;
    }

    /**
     * {@code POST  /organizational-entities} : Create a new organizationalEntity.
     *
     * @param organizationalEntity the organizationalEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationalEntity, or with status {@code 400 (Bad Request)} if the organizationalEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organizational-entities")
    public ResponseEntity<OrganizationalEntity> createOrganizationalEntity(@Valid @RequestBody OrganizationalEntity organizationalEntity)
        throws URISyntaxException {
        log.debug("REST request to save OrganizationalEntity : {}", organizationalEntity);
        if (organizationalEntity.getId() != null) {
            throw new BadRequestAlertException("A new organizationalEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationalEntity result = organizationalEntityRepository.save(organizationalEntity);
        return ResponseEntity
            .created(new URI("/api/organizational-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organizational-entities/:id} : Updates an existing organizationalEntity.
     *
     * @param id the id of the organizationalEntity to save.
     * @param organizationalEntity the organizationalEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationalEntity,
     * or with status {@code 400 (Bad Request)} if the organizationalEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationalEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organizational-entities/{id}")
    public ResponseEntity<OrganizationalEntity> updateOrganizationalEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrganizationalEntity organizationalEntity
    ) throws URISyntaxException {
        log.debug("REST request to update OrganizationalEntity : {}, {}", id, organizationalEntity);
        if (organizationalEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationalEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationalEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrganizationalEntity result = organizationalEntityRepository.save(organizationalEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationalEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /organizational-entities/:id} : Partial updates given fields of an existing organizationalEntity, field will ignore if it is null
     *
     * @param id the id of the organizationalEntity to save.
     * @param organizationalEntity the organizationalEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationalEntity,
     * or with status {@code 400 (Bad Request)} if the organizationalEntity is not valid,
     * or with status {@code 404 (Not Found)} if the organizationalEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the organizationalEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organizational-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrganizationalEntity> partialUpdateOrganizationalEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrganizationalEntity organizationalEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrganizationalEntity partially : {}, {}", id, organizationalEntity);
        if (organizationalEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationalEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationalEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganizationalEntity> result = organizationalEntityRepository
            .findById(organizationalEntity.getId())
            .map(existingOrganizationalEntity -> {
                if (organizationalEntity.getName() != null) {
                    existingOrganizationalEntity.setName(organizationalEntity.getName());
                }

                return existingOrganizationalEntity;
            })
            .map(organizationalEntityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationalEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /organizational-entities} : get all the organizationalEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationalEntities in body.
     */
    @GetMapping("/organizational-entities")
    public List<OrganizationalEntity> getAllOrganizationalEntities() {
        log.debug("REST request to get all OrganizationalEntities");
        return organizationalEntityRepository.findAll();
    }

    /**
     * {@code GET  /organizational-entities/:id} : get the "id" organizationalEntity.
     *
     * @param id the id of the organizationalEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationalEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organizational-entities/{id}")
    public ResponseEntity<OrganizationalEntity> getOrganizationalEntity(@PathVariable Long id) {
        log.debug("REST request to get OrganizationalEntity : {}", id);
        Optional<OrganizationalEntity> organizationalEntity = organizationalEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(organizationalEntity);
    }

    /**
     * {@code DELETE  /organizational-entities/:id} : delete the "id" organizationalEntity.
     *
     * @param id the id of the organizationalEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organizational-entities/{id}")
    public ResponseEntity<Void> deleteOrganizationalEntity(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationalEntity : {}", id);
        organizationalEntityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
