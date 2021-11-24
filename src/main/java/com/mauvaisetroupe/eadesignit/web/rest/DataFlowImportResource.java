package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import com.mauvaisetroupe.eadesignit.repository.DataFlowImportRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.DataFlowImport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DataFlowImportResource {

    private final Logger log = LoggerFactory.getLogger(DataFlowImportResource.class);

    private static final String ENTITY_NAME = "dataFlowImport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataFlowImportRepository dataFlowImportRepository;

    public DataFlowImportResource(DataFlowImportRepository dataFlowImportRepository) {
        this.dataFlowImportRepository = dataFlowImportRepository;
    }

    /**
     * {@code POST  /data-flow-imports} : Create a new dataFlowImport.
     *
     * @param dataFlowImport the dataFlowImport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataFlowImport, or with status {@code 400 (Bad Request)} if the dataFlowImport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-flow-imports")
    public ResponseEntity<DataFlowImport> createDataFlowImport(@RequestBody DataFlowImport dataFlowImport) throws URISyntaxException {
        log.debug("REST request to save DataFlowImport : {}", dataFlowImport);
        if (dataFlowImport.getId() != null) {
            throw new BadRequestAlertException("A new dataFlowImport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataFlowImport result = dataFlowImportRepository.save(dataFlowImport);
        return ResponseEntity
            .created(new URI("/api/data-flow-imports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-flow-imports/:id} : Updates an existing dataFlowImport.
     *
     * @param id the id of the dataFlowImport to save.
     * @param dataFlowImport the dataFlowImport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFlowImport,
     * or with status {@code 400 (Bad Request)} if the dataFlowImport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataFlowImport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-flow-imports/{id}")
    public ResponseEntity<DataFlowImport> updateDataFlowImport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataFlowImport dataFlowImport
    ) throws URISyntaxException {
        log.debug("REST request to update DataFlowImport : {}, {}", id, dataFlowImport);
        if (dataFlowImport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataFlowImport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataFlowImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataFlowImport result = dataFlowImportRepository.save(dataFlowImport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataFlowImport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-flow-imports/:id} : Partial updates given fields of an existing dataFlowImport, field will ignore if it is null
     *
     * @param id the id of the dataFlowImport to save.
     * @param dataFlowImport the dataFlowImport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFlowImport,
     * or with status {@code 400 (Bad Request)} if the dataFlowImport is not valid,
     * or with status {@code 404 (Not Found)} if the dataFlowImport is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataFlowImport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-flow-imports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataFlowImport> partialUpdateDataFlowImport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DataFlowImport dataFlowImport
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataFlowImport partially : {}, {}", id, dataFlowImport);
        if (dataFlowImport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataFlowImport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataFlowImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataFlowImport> result = dataFlowImportRepository
            .findById(dataFlowImport.getId())
            .map(existingDataFlowImport -> {
                if (dataFlowImport.getDataResourceName() != null) {
                    existingDataFlowImport.setDataResourceName(dataFlowImport.getDataResourceName());
                }
                if (dataFlowImport.getDataResourceType() != null) {
                    existingDataFlowImport.setDataResourceType(dataFlowImport.getDataResourceType());
                }
                if (dataFlowImport.getDataDescription() != null) {
                    existingDataFlowImport.setDataDescription(dataFlowImport.getDataDescription());
                }
                if (dataFlowImport.getDataDocumentationURL() != null) {
                    existingDataFlowImport.setDataDocumentationURL(dataFlowImport.getDataDocumentationURL());
                }
                if (dataFlowImport.getDataItemResourceName() != null) {
                    existingDataFlowImport.setDataItemResourceName(dataFlowImport.getDataItemResourceName());
                }
                if (dataFlowImport.getDataItemResourceType() != null) {
                    existingDataFlowImport.setDataItemResourceType(dataFlowImport.getDataItemResourceType());
                }
                if (dataFlowImport.getDataItemDescription() != null) {
                    existingDataFlowImport.setDataItemDescription(dataFlowImport.getDataItemDescription());
                }
                if (dataFlowImport.getDataItemDocumentationURL() != null) {
                    existingDataFlowImport.setDataItemDocumentationURL(dataFlowImport.getDataItemDocumentationURL());
                }
                if (dataFlowImport.getFrequency() != null) {
                    existingDataFlowImport.setFrequency(dataFlowImport.getFrequency());
                }
                if (dataFlowImport.getFormat() != null) {
                    existingDataFlowImport.setFormat(dataFlowImport.getFormat());
                }
                if (dataFlowImport.getContractURL() != null) {
                    existingDataFlowImport.setContractURL(dataFlowImport.getContractURL());
                }
                if (dataFlowImport.getImportDataFlowStatus() != null) {
                    existingDataFlowImport.setImportDataFlowStatus(dataFlowImport.getImportDataFlowStatus());
                }
                if (dataFlowImport.getImportDataFlowItemStatus() != null) {
                    existingDataFlowImport.setImportDataFlowItemStatus(dataFlowImport.getImportDataFlowItemStatus());
                }
                if (dataFlowImport.getImportStatusMessage() != null) {
                    existingDataFlowImport.setImportStatusMessage(dataFlowImport.getImportStatusMessage());
                }

                return existingDataFlowImport;
            })
            .map(dataFlowImportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataFlowImport.getId().toString())
        );
    }

    /**
     * {@code GET  /data-flow-imports} : get all the dataFlowImports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataFlowImports in body.
     */
    @GetMapping("/data-flow-imports")
    public List<DataFlowImport> getAllDataFlowImports() {
        log.debug("REST request to get all DataFlowImports");
        return dataFlowImportRepository.findAll();
    }

    /**
     * {@code GET  /data-flow-imports/:id} : get the "id" dataFlowImport.
     *
     * @param id the id of the dataFlowImport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataFlowImport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-flow-imports/{id}")
    public ResponseEntity<DataFlowImport> getDataFlowImport(@PathVariable Long id) {
        log.debug("REST request to get DataFlowImport : {}", id);
        Optional<DataFlowImport> dataFlowImport = dataFlowImportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dataFlowImport);
    }

    /**
     * {@code DELETE  /data-flow-imports/:id} : delete the "id" dataFlowImport.
     *
     * @param id the id of the dataFlowImport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-flow-imports/{id}")
    public ResponseEntity<Void> deleteDataFlowImport(@PathVariable Long id) {
        log.debug("REST request to delete DataFlowImport : {}", id);
        dataFlowImportRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
