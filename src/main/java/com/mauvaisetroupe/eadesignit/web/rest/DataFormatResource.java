package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.DataFormat;
import com.mauvaisetroupe.eadesignit.repository.DataFormatRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.DataFormat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DataFormatResource {

    private final Logger log = LoggerFactory.getLogger(DataFormatResource.class);

    private static final String ENTITY_NAME = "dataFormat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataFormatRepository dataFormatRepository;

    public DataFormatResource(DataFormatRepository dataFormatRepository) {
        this.dataFormatRepository = dataFormatRepository;
    }

    /**
     * {@code POST  /data-formats} : Create a new dataFormat.
     *
     * @param dataFormat the dataFormat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataFormat, or with status {@code 400 (Bad Request)} if the dataFormat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-formats")
    public ResponseEntity<DataFormat> createDataFormat(@RequestBody DataFormat dataFormat) throws URISyntaxException {
        log.debug("REST request to save DataFormat : {}", dataFormat);
        if (dataFormat.getId() != null) {
            throw new BadRequestAlertException("A new dataFormat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataFormat result = dataFormatRepository.save(dataFormat);
        return ResponseEntity
            .created(new URI("/api/data-formats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-formats/:id} : Updates an existing dataFormat.
     *
     * @param id the id of the dataFormat to save.
     * @param dataFormat the dataFormat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFormat,
     * or with status {@code 400 (Bad Request)} if the dataFormat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataFormat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-formats/{id}")
    public ResponseEntity<DataFormat> updateDataFormat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataFormat dataFormat
    ) throws URISyntaxException {
        log.debug("REST request to update DataFormat : {}, {}", id, dataFormat);
        if (dataFormat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataFormat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataFormatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataFormat result = dataFormatRepository.save(dataFormat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataFormat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-formats/:id} : Partial updates given fields of an existing dataFormat, field will ignore if it is null
     *
     * @param id the id of the dataFormat to save.
     * @param dataFormat the dataFormat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFormat,
     * or with status {@code 400 (Bad Request)} if the dataFormat is not valid,
     * or with status {@code 404 (Not Found)} if the dataFormat is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataFormat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-formats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataFormat> partialUpdateDataFormat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataFormat dataFormat
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataFormat partially : {}, {}", id, dataFormat);
        if (dataFormat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataFormat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataFormatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataFormat> result = dataFormatRepository
            .findById(dataFormat.getId())
            .map(existingDataFormat -> {
                if (dataFormat.getName() != null) {
                    existingDataFormat.setName(dataFormat.getName());
                }

                return existingDataFormat;
            })
            .map(dataFormatRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataFormat.getId().toString())
        );
    }

    /**
     * {@code GET  /data-formats} : get all the dataFormats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataFormats in body.
     */
    @GetMapping("/data-formats")
    public List<DataFormat> getAllDataFormats() {
        log.debug("REST request to get all DataFormats");
        return dataFormatRepository.findAll();
    }

    /**
     * {@code GET  /data-formats/:id} : get the "id" dataFormat.
     *
     * @param id the id of the dataFormat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataFormat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-formats/{id}")
    public ResponseEntity<DataFormat> getDataFormat(@PathVariable Long id) {
        log.debug("REST request to get DataFormat : {}", id);
        Optional<DataFormat> dataFormat = dataFormatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dataFormat);
    }

    /**
     * {@code DELETE  /data-formats/:id} : delete the "id" dataFormat.
     *
     * @param id the id of the dataFormat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-formats/{id}")
    public ResponseEntity<Void> deleteDataFormat(@PathVariable Long id) {
        log.debug("REST request to delete DataFormat : {}", id);
        dataFormatRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
