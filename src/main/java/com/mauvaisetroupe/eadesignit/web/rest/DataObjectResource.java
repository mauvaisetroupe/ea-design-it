package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.DataObject;
import com.mauvaisetroupe.eadesignit.repository.DataObjectRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.DataObject}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DataObjectResource {

    private final Logger log = LoggerFactory.getLogger(DataObjectResource.class);

    private static final String ENTITY_NAME = "dataObject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataObjectRepository dataObjectRepository;

    public DataObjectResource(DataObjectRepository dataObjectRepository) {
        this.dataObjectRepository = dataObjectRepository;
    }

    /**
     * {@code POST  /data-objects} : Create a new dataObject.
     *
     * @param dataObject the dataObject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataObject, or with status {@code 400 (Bad Request)} if the dataObject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-objects")
    public ResponseEntity<DataObject> createDataObject(@Valid @RequestBody DataObject dataObject) throws URISyntaxException {
        log.debug("REST request to save DataObject : {}", dataObject);
        if (dataObject.getId() != null) {
            throw new BadRequestAlertException("A new dataObject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataObject result = dataObjectRepository.save(dataObject);
        return ResponseEntity
            .created(new URI("/api/data-objects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-objects/:id} : Updates an existing dataObject.
     *
     * @param id the id of the dataObject to save.
     * @param dataObject the dataObject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataObject,
     * or with status {@code 400 (Bad Request)} if the dataObject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataObject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-objects/{id}")
    public ResponseEntity<DataObject> updateDataObject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DataObject dataObject
    ) throws URISyntaxException {
        log.debug("REST request to update DataObject : {}, {}", id, dataObject);
        if (dataObject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataObject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataObjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataObject result = dataObjectRepository.save(dataObject);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataObject.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-objects/:id} : Partial updates given fields of an existing dataObject, field will ignore if it is null
     *
     * @param id the id of the dataObject to save.
     * @param dataObject the dataObject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataObject,
     * or with status {@code 400 (Bad Request)} if the dataObject is not valid,
     * or with status {@code 404 (Not Found)} if the dataObject is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataObject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-objects/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataObject> partialUpdateDataObject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DataObject dataObject
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataObject partially : {}, {}", id, dataObject);
        if (dataObject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataObject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataObjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataObject> result = dataObjectRepository
            .findById(dataObject.getId())
            .map(existingDataObject -> {
                if (dataObject.getName() != null) {
                    existingDataObject.setName(dataObject.getName());
                }
                if (dataObject.getType() != null) {
                    existingDataObject.setType(dataObject.getType());
                }

                return existingDataObject;
            })
            .map(dataObjectRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataObject.getId().toString())
        );
    }

    /**
     * {@code GET  /data-objects} : get all the dataObjects.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataObjects in body.
     */
    @GetMapping("/data-objects")
    public List<DataObject> getAllDataObjects(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DataObjects");
        if (eagerload) {
            return dataObjectRepository.findAllWithAllChildrens();
        } else {
            return dataObjectRepository.findAll();
        }
    }

    /**
     * {@code GET  /data-objects/:id} : get the "id" dataObject.
     *
     * @param id the id of the dataObject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataObject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-objects/{id}")
    public ResponseEntity<DataObject> getDataObject(@PathVariable Long id) {
        log.debug("REST request to get DataObject : {}", id);
        Optional<DataObject> dataObject = dataObjectRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(dataObject);
    }

    /**
     * {@code DELETE  /data-objects/:id} : delete the "id" dataObject.
     *
     * @param id the id of the dataObject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-objects/{id}")
    public ResponseEntity<Void> deleteDataObject(@PathVariable Long id) {
        log.debug("REST request to delete DataObject : {}", id);
        dataObjectRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
