package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.DataFlow}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DataFlowResource {

    private final Logger log = LoggerFactory.getLogger(DataFlowResource.class);

    private static final String ENTITY_NAME = "dataFlow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataFlowRepository dataFlowRepository;

    public DataFlowResource(DataFlowRepository dataFlowRepository) {
        this.dataFlowRepository = dataFlowRepository;
    }

    /**
     * {@code POST  /data-flows} : Create a new dataFlow.
     *
     * @param dataFlow the dataFlow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataFlow, or with status {@code 400 (Bad Request)} if the dataFlow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-flows")
    public ResponseEntity<DataFlow> createDataFlow(@Valid @RequestBody DataFlow dataFlow) throws URISyntaxException {
        log.debug("REST request to save DataFlow : {}", dataFlow);
        if (dataFlow.getId() != null) {
            throw new BadRequestAlertException("A new dataFlow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataFlow result = dataFlowRepository.save(dataFlow);
        return ResponseEntity
            .created(new URI("/api/data-flows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-flows/:id} : Updates an existing dataFlow.
     *
     * @param id the id of the dataFlow to save.
     * @param dataFlow the dataFlow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFlow,
     * or with status {@code 400 (Bad Request)} if the dataFlow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataFlow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-flows/{id}")
    public ResponseEntity<DataFlow> updateDataFlow(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DataFlow dataFlow
    ) throws URISyntaxException {
        log.debug("REST request to update DataFlow : {}, {}", id, dataFlow);
        if (dataFlow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataFlow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataFlowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataFlow result = dataFlowRepository.save(dataFlow);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataFlow.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-flows/:id} : Partial updates given fields of an existing dataFlow, field will ignore if it is null
     *
     * @param id the id of the dataFlow to save.
     * @param dataFlow the dataFlow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFlow,
     * or with status {@code 400 (Bad Request)} if the dataFlow is not valid,
     * or with status {@code 404 (Not Found)} if the dataFlow is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataFlow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-flows/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataFlow> partialUpdateDataFlow(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DataFlow dataFlow
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataFlow partially : {}, {}", id, dataFlow);
        if (dataFlow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataFlow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataFlowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataFlow> result = dataFlowRepository
            .findById(dataFlow.getId())
            .map(existingDataFlow -> {
                if (dataFlow.getFrequency() != null) {
                    existingDataFlow.setFrequency(dataFlow.getFrequency());
                }
                if (dataFlow.getFormat() != null) {
                    existingDataFlow.setFormat(dataFlow.getFormat());
                }
                if (dataFlow.getType() != null) {
                    existingDataFlow.setType(dataFlow.getType());
                }
                if (dataFlow.getDescription() != null) {
                    existingDataFlow.setDescription(dataFlow.getDescription());
                }
                if (dataFlow.getResourceName() != null) {
                    existingDataFlow.setResourceName(dataFlow.getResourceName());
                }
                if (dataFlow.getContractURL() != null) {
                    existingDataFlow.setContractURL(dataFlow.getContractURL());
                }
                if (dataFlow.getDocumentationURL() != null) {
                    existingDataFlow.setDocumentationURL(dataFlow.getDocumentationURL());
                }
                if (dataFlow.getStartDate() != null) {
                    existingDataFlow.setStartDate(dataFlow.getStartDate());
                }
                if (dataFlow.getEndDate() != null) {
                    existingDataFlow.setEndDate(dataFlow.getEndDate());
                }

                return existingDataFlow;
            })
            .map(dataFlowRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataFlow.getId().toString())
        );
    }

    /**
     * {@code GET  /data-flows} : get all the dataFlows.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataFlows in body.
     */
    @GetMapping("/data-flows")
    public List<DataFlow> getAllDataFlows(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DataFlows");
        return dataFlowRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /data-flows/:id} : get the "id" dataFlow.
     *
     * @param id the id of the dataFlow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataFlow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-flows/{id}")
    public ResponseEntity<DataFlow> getDataFlow(@PathVariable Long id) {
        log.debug("REST request to get DataFlow : {}", id);
        Optional<DataFlow> dataFlow = dataFlowRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(dataFlow);
    }

    /**
     * {@code DELETE  /data-flows/:id} : delete the "id" dataFlow.
     *
     * @param id the id of the dataFlow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-flows/{id}")
    public ResponseEntity<Void> deleteDataFlow(@PathVariable Long id) {
        log.debug("REST request to delete DataFlow : {}", id);
        dataFlowRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
