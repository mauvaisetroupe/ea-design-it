package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.repository.ApplicationComponentRepository;
import com.mauvaisetroupe.eadesignit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.ApplicationComponent}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApplicationComponentResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationComponentResource.class);

    private static final String ENTITY_NAME = "applicationComponent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationComponentRepository applicationComponentRepository;

    public ApplicationComponentResource(ApplicationComponentRepository applicationComponentRepository) {
        this.applicationComponentRepository = applicationComponentRepository;
    }

    /**
     * {@code POST  /application-components} : Create a new applicationComponent.
     *
     * @param applicationComponent the applicationComponent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationComponent, or with status {@code 400 (Bad Request)} if the applicationComponent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-components")
    public ResponseEntity<ApplicationComponent> createApplicationComponent(@Valid @RequestBody ApplicationComponent applicationComponent)
        throws URISyntaxException {
        log.debug("REST request to save ApplicationComponent : {}", applicationComponent);
        if (applicationComponent.getId() != null) {
            throw new BadRequestAlertException("A new applicationComponent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationComponent result = applicationComponentRepository.save(applicationComponent);
        return ResponseEntity
            .created(new URI("/api/application-components/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-components/:id} : Updates an existing applicationComponent.
     *
     * @param id the id of the applicationComponent to save.
     * @param applicationComponent the applicationComponent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationComponent,
     * or with status {@code 400 (Bad Request)} if the applicationComponent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationComponent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-components/{id}")
    public ResponseEntity<ApplicationComponent> updateApplicationComponent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApplicationComponent applicationComponent
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationComponent : {}, {}", id, applicationComponent);
        if (applicationComponent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationComponent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationComponentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApplicationComponent result = applicationComponentRepository.save(applicationComponent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationComponent.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /application-components/:id} : Partial updates given fields of an existing applicationComponent, field will ignore if it is null
     *
     * @param id the id of the applicationComponent to save.
     * @param applicationComponent the applicationComponent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationComponent,
     * or with status {@code 400 (Bad Request)} if the applicationComponent is not valid,
     * or with status {@code 404 (Not Found)} if the applicationComponent is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationComponent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/application-components/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationComponent> partialUpdateApplicationComponent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApplicationComponent applicationComponent
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationComponent partially : {}, {}", id, applicationComponent);
        if (applicationComponent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationComponent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationComponentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationComponent> result = applicationComponentRepository
            .findById(applicationComponent.getId())
            .map(existingApplicationComponent -> {
                if (applicationComponent.getAlias() != null) {
                    existingApplicationComponent.setAlias(applicationComponent.getAlias());
                }
                if (applicationComponent.getName() != null) {
                    existingApplicationComponent.setName(applicationComponent.getName());
                }
                if (applicationComponent.getDescription() != null) {
                    existingApplicationComponent.setDescription(applicationComponent.getDescription());
                }
                if (applicationComponent.getComment() != null) {
                    existingApplicationComponent.setComment(applicationComponent.getComment());
                }
                if (applicationComponent.getDocumentationURL() != null) {
                    existingApplicationComponent.setDocumentationURL(applicationComponent.getDocumentationURL());
                }
                if (applicationComponent.getStartDate() != null) {
                    existingApplicationComponent.setStartDate(applicationComponent.getStartDate());
                }
                if (applicationComponent.getEndDate() != null) {
                    existingApplicationComponent.setEndDate(applicationComponent.getEndDate());
                }
                if (applicationComponent.getApplicationType() != null) {
                    existingApplicationComponent.setApplicationType(applicationComponent.getApplicationType());
                }
                if (applicationComponent.getSoftwareType() != null) {
                    existingApplicationComponent.setSoftwareType(applicationComponent.getSoftwareType());
                }

                return existingApplicationComponent;
            })
            .map(applicationComponentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationComponent.getId().toString())
        );
    }

    /**
     * {@code GET  /application-components} : get all the applicationComponents.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationComponents in body.
     */
    @GetMapping("/application-components")
    public List<ApplicationComponent> getAllApplicationComponents(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all ApplicationComponents");
        return applicationComponentRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /application-components/:id} : get the "id" applicationComponent.
     *
     * @param id the id of the applicationComponent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationComponent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-components/{id}")
    public ResponseEntity<ApplicationComponent> getApplicationComponent(@PathVariable Long id) {
        log.debug("REST request to get ApplicationComponent : {}", id);
        Optional<ApplicationComponent> applicationComponent = applicationComponentRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(applicationComponent);
    }

    /**
     * {@code DELETE  /application-components/:id} : delete the "id" applicationComponent.
     *
     * @param id the id of the applicationComponent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-components/{id}")
    public ResponseEntity<Void> deleteApplicationComponent(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationComponent : {}", id);
        applicationComponentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
