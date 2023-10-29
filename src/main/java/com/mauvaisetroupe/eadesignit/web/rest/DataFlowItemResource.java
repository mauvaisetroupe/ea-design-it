package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.DataFlowItem;
import com.mauvaisetroupe.eadesignit.repository.DataFlowItemRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.DataFlowItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DataFlowItemResource {

    private final Logger log = LoggerFactory.getLogger(DataFlowItemResource.class);

    private static final String ENTITY_NAME = "dataFlowItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataFlowItemRepository dataFlowItemRepository;

    public DataFlowItemResource(DataFlowItemRepository dataFlowItemRepository) {
        this.dataFlowItemRepository = dataFlowItemRepository;
    }

    /**
     * {@code POST  /data-flow-items} : Create a new dataFlowItem.
     *
     * @param dataFlowItem the dataFlowItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataFlowItem, or with status {@code 400 (Bad Request)} if the dataFlowItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-flow-items")
    public ResponseEntity<DataFlowItem> createDataFlowItem(@Valid @RequestBody DataFlowItem dataFlowItem) throws URISyntaxException {
        log.debug("REST request to save DataFlowItem : {}", dataFlowItem);
        if (dataFlowItem.getId() != null) {
            throw new BadRequestAlertException("A new dataFlowItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataFlowItem result = dataFlowItemRepository.save(dataFlowItem);
        return ResponseEntity
            .created(new URI("/api/data-flow-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-flow-items/:id} : Updates an existing dataFlowItem.
     *
     * @param id the id of the dataFlowItem to save.
     * @param dataFlowItem the dataFlowItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFlowItem,
     * or with status {@code 400 (Bad Request)} if the dataFlowItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataFlowItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-flow-items/{id}")
    public ResponseEntity<DataFlowItem> updateDataFlowItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DataFlowItem dataFlowItem
    ) throws URISyntaxException {
        log.debug("REST request to update DataFlowItem : {}, {}", id, dataFlowItem);
        if (dataFlowItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataFlowItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataFlowItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataFlowItem result = dataFlowItemRepository.save(dataFlowItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataFlowItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-flow-items/:id} : Partial updates given fields of an existing dataFlowItem, field will ignore if it is null
     *
     * @param id the id of the dataFlowItem to save.
     * @param dataFlowItem the dataFlowItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFlowItem,
     * or with status {@code 400 (Bad Request)} if the dataFlowItem is not valid,
     * or with status {@code 404 (Not Found)} if the dataFlowItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataFlowItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-flow-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataFlowItem> partialUpdateDataFlowItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DataFlowItem dataFlowItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataFlowItem partially : {}, {}", id, dataFlowItem);
        if (dataFlowItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataFlowItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataFlowItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataFlowItem> result = dataFlowItemRepository
            .findById(dataFlowItem.getId())
            .map(existingDataFlowItem -> {
                if (dataFlowItem.getResourceName() != null) {
                    existingDataFlowItem.setResourceName(dataFlowItem.getResourceName());
                }
                if (dataFlowItem.getResourceType() != null) {
                    existingDataFlowItem.setResourceType(dataFlowItem.getResourceType());
                }
                if (dataFlowItem.getDescription() != null) {
                    existingDataFlowItem.setDescription(dataFlowItem.getDescription());
                }
                if (dataFlowItem.getContractURL() != null) {
                    existingDataFlowItem.setContractURL(dataFlowItem.getContractURL());
                }
                if (dataFlowItem.getDocumentationURL() != null) {
                    existingDataFlowItem.setDocumentationURL(dataFlowItem.getDocumentationURL());
                }
                if (dataFlowItem.getStartDate() != null) {
                    existingDataFlowItem.setStartDate(dataFlowItem.getStartDate());
                }
                if (dataFlowItem.getEndDate() != null) {
                    existingDataFlowItem.setEndDate(dataFlowItem.getEndDate());
                }

                return existingDataFlowItem;
            })
            .map(dataFlowItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataFlowItem.getId().toString())
        );
    }

    /**
     * {@code GET  /data-flow-items} : get all the dataFlowItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataFlowItems in body.
     */
    @GetMapping("/data-flow-items")
    public List<DataFlowItem> getAllDataFlowItems() {
        log.debug("REST request to get all DataFlowItems");
        return dataFlowItemRepository.findAll();
    }

    /**
     * {@code GET  /data-flow-items/:id} : get the "id" dataFlowItem.
     *
     * @param id the id of the dataFlowItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataFlowItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-flow-items/{id}")
    public ResponseEntity<DataFlowItem> getDataFlowItem(@PathVariable Long id) {
        log.debug("REST request to get DataFlowItem : {}", id);
        Optional<DataFlowItem> dataFlowItem = dataFlowItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dataFlowItem);
    }

    /**
     * {@code DELETE  /data-flow-items/:id} : delete the "id" dataFlowItem.
     *
     * @param id the id of the dataFlowItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-flow-items/{id}")
    public ResponseEntity<Void> deleteDataFlowItem(@PathVariable Long id) {
        log.debug("REST request to delete DataFlowItem : {}", id);
        dataFlowItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
