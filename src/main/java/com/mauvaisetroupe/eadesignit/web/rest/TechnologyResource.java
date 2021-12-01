package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.Technology;
import com.mauvaisetroupe.eadesignit.repository.TechnologyRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.Technology}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TechnologyResource {

    private final Logger log = LoggerFactory.getLogger(TechnologyResource.class);

    private static final String ENTITY_NAME = "technology";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechnologyRepository technologyRepository;

    public TechnologyResource(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    /**
     * {@code POST  /technologies} : Create a new technology.
     *
     * @param technology the technology to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new technology, or with status {@code 400 (Bad Request)} if the technology has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/technologies")
    public ResponseEntity<Technology> createTechnology(@RequestBody Technology technology) throws URISyntaxException {
        log.debug("REST request to save Technology : {}", technology);
        if (technology.getId() != null) {
            throw new BadRequestAlertException("A new technology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Technology result = technologyRepository.save(technology);
        return ResponseEntity
            .created(new URI("/api/technologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /technologies/:id} : Updates an existing technology.
     *
     * @param id the id of the technology to save.
     * @param technology the technology to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technology,
     * or with status {@code 400 (Bad Request)} if the technology is not valid,
     * or with status {@code 500 (Internal Server Error)} if the technology couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/technologies/{id}")
    public ResponseEntity<Technology> updateTechnology(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Technology technology
    ) throws URISyntaxException {
        log.debug("REST request to update Technology : {}, {}", id, technology);
        if (technology.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technology.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Technology result = technologyRepository.save(technology);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, technology.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /technologies/:id} : Partial updates given fields of an existing technology, field will ignore if it is null
     *
     * @param id the id of the technology to save.
     * @param technology the technology to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technology,
     * or with status {@code 400 (Bad Request)} if the technology is not valid,
     * or with status {@code 404 (Not Found)} if the technology is not found,
     * or with status {@code 500 (Internal Server Error)} if the technology couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/technologies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Technology> partialUpdateTechnology(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Technology technology
    ) throws URISyntaxException {
        log.debug("REST request to partial update Technology partially : {}, {}", id, technology);
        if (technology.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technology.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Technology> result = technologyRepository
            .findById(technology.getId())
            .map(existingTechnology -> {
                if (technology.getName() != null) {
                    existingTechnology.setName(technology.getName());
                }
                if (technology.getType() != null) {
                    existingTechnology.setType(technology.getType());
                }
                if (technology.getDescription() != null) {
                    existingTechnology.setDescription(technology.getDescription());
                }

                return existingTechnology;
            })
            .map(technologyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, technology.getId().toString())
        );
    }

    /**
     * {@code GET  /technologies} : get all the technologies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of technologies in body.
     */
    @GetMapping("/technologies")
    public List<Technology> getAllTechnologies() {
        log.debug("REST request to get all Technologies");
        return technologyRepository.findAll();
    }

    /**
     * {@code GET  /technologies/:id} : get the "id" technology.
     *
     * @param id the id of the technology to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technology, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/technologies/{id}")
    public ResponseEntity<Technology> getTechnology(@PathVariable Long id) {
        log.debug("REST request to get Technology : {}", id);
        Optional<Technology> technology = technologyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(technology);
    }

    /**
     * {@code DELETE  /technologies/:id} : delete the "id" technology.
     *
     * @param id the id of the technology to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/technologies/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        log.debug("REST request to delete Technology : {}", id);
        technologyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
