package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ApplicationCategory;
import com.mauvaisetroupe.eadesignit.repository.ApplicationCategoryRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.ApplicationCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApplicationCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationCategoryResource.class);

    private static final String ENTITY_NAME = "applicationCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationCategoryRepository applicationCategoryRepository;

    public ApplicationCategoryResource(ApplicationCategoryRepository applicationCategoryRepository) {
        this.applicationCategoryRepository = applicationCategoryRepository;
    }

    /**
     * {@code POST  /application-categories} : Create a new applicationCategory.
     *
     * @param applicationCategory the applicationCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationCategory, or with status {@code 400 (Bad Request)} if the applicationCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-categories")
    public ResponseEntity<ApplicationCategory> createApplicationCategory(@Valid @RequestBody ApplicationCategory applicationCategory)
        throws URISyntaxException {
        log.debug("REST request to save ApplicationCategory : {}", applicationCategory);
        if (applicationCategory.getId() != null) {
            throw new BadRequestAlertException("A new applicationCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationCategory result = applicationCategoryRepository.save(applicationCategory);
        return ResponseEntity
            .created(new URI("/api/application-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-categories/:id} : Updates an existing applicationCategory.
     *
     * @param id the id of the applicationCategory to save.
     * @param applicationCategory the applicationCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationCategory,
     * or with status {@code 400 (Bad Request)} if the applicationCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-categories/{id}")
    public ResponseEntity<ApplicationCategory> updateApplicationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApplicationCategory applicationCategory
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationCategory : {}, {}", id, applicationCategory);
        if (applicationCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApplicationCategory result = applicationCategoryRepository.save(applicationCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /application-categories/:id} : Partial updates given fields of an existing applicationCategory, field will ignore if it is null
     *
     * @param id the id of the applicationCategory to save.
     * @param applicationCategory the applicationCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationCategory,
     * or with status {@code 400 (Bad Request)} if the applicationCategory is not valid,
     * or with status {@code 404 (Not Found)} if the applicationCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/application-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationCategory> partialUpdateApplicationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApplicationCategory applicationCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationCategory partially : {}, {}", id, applicationCategory);
        if (applicationCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationCategory> result = applicationCategoryRepository
            .findById(applicationCategory.getId())
            .map(existingApplicationCategory -> {
                if (applicationCategory.getName() != null) {
                    existingApplicationCategory.setName(applicationCategory.getName());
                }
                if (applicationCategory.getType() != null) {
                    existingApplicationCategory.setType(applicationCategory.getType());
                }
                if (applicationCategory.getDescription() != null) {
                    existingApplicationCategory.setDescription(applicationCategory.getDescription());
                }

                return existingApplicationCategory;
            })
            .map(applicationCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /application-categories} : get all the applicationCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationCategories in body.
     */
    @GetMapping("/application-categories")
    public List<ApplicationCategory> getAllApplicationCategories() {
        log.debug("REST request to get all ApplicationCategories");
        return applicationCategoryRepository.findAll();
    }

    /**
     * {@code GET  /application-categories/:id} : get the "id" applicationCategory.
     *
     * @param id the id of the applicationCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-categories/{id}")
    public ResponseEntity<ApplicationCategory> getApplicationCategory(@PathVariable Long id) {
        log.debug("REST request to get ApplicationCategory : {}", id);
        Optional<ApplicationCategory> applicationCategory = applicationCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(applicationCategory);
    }

    /**
     * {@code DELETE  /application-categories/:id} : delete the "id" applicationCategory.
     *
     * @param id the id of the applicationCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-categories/{id}")
    public ResponseEntity<Void> deleteApplicationCategory(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationCategory : {}", id);
        applicationCategoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
