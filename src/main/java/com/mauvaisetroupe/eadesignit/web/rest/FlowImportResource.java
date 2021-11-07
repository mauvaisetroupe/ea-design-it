package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.repository.FlowImportRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.FlowImport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FlowImportResource {

    private final Logger log = LoggerFactory.getLogger(FlowImportResource.class);

    private static final String ENTITY_NAME = "flowImport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlowImportRepository flowImportRepository;

    public FlowImportResource(FlowImportRepository flowImportRepository) {
        this.flowImportRepository = flowImportRepository;
    }

    /**
     * {@code POST  /flow-imports} : Create a new flowImport.
     *
     * @param flowImport the flowImport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flowImport, or with status {@code 400 (Bad Request)} if the flowImport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flow-imports")
    public ResponseEntity<FlowImport> createFlowImport(@RequestBody FlowImport flowImport) throws URISyntaxException {
        log.debug("REST request to save FlowImport : {}", flowImport);
        if (flowImport.getId() != null) {
            throw new BadRequestAlertException("A new flowImport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlowImport result = flowImportRepository.save(flowImport);
        return ResponseEntity
            .created(new URI("/api/flow-imports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flow-imports/:id} : Updates an existing flowImport.
     *
     * @param id the id of the flowImport to save.
     * @param flowImport the flowImport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowImport,
     * or with status {@code 400 (Bad Request)} if the flowImport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flowImport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flow-imports/{id}")
    public ResponseEntity<FlowImport> updateFlowImport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlowImport flowImport
    ) throws URISyntaxException {
        log.debug("REST request to update FlowImport : {}, {}", id, flowImport);
        if (flowImport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowImport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlowImport result = flowImportRepository.save(flowImport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flowImport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flow-imports/:id} : Partial updates given fields of an existing flowImport, field will ignore if it is null
     *
     * @param id the id of the flowImport to save.
     * @param flowImport the flowImport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowImport,
     * or with status {@code 400 (Bad Request)} if the flowImport is not valid,
     * or with status {@code 404 (Not Found)} if the flowImport is not found,
     * or with status {@code 500 (Internal Server Error)} if the flowImport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flow-imports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlowImport> partialUpdateFlowImport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FlowImport flowImport
    ) throws URISyntaxException {
        log.debug("REST request to partial update FlowImport partially : {}, {}", id, flowImport);
        if (flowImport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowImport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlowImport> result = flowImportRepository
            .findById(flowImport.getId())
            .map(existingFlowImport -> {
                if (flowImport.getIdFlowFromExcel() != null) {
                    existingFlowImport.setIdFlowFromExcel(flowImport.getIdFlowFromExcel());
                }
                if (flowImport.getFlowAlias() != null) {
                    existingFlowImport.setFlowAlias(flowImport.getFlowAlias());
                }
                if (flowImport.getSourceElement() != null) {
                    existingFlowImport.setSourceElement(flowImport.getSourceElement());
                }
                if (flowImport.getTargetElement() != null) {
                    existingFlowImport.setTargetElement(flowImport.getTargetElement());
                }
                if (flowImport.getDescription() != null) {
                    existingFlowImport.setDescription(flowImport.getDescription());
                }
                if (flowImport.getIntegrationPattern() != null) {
                    existingFlowImport.setIntegrationPattern(flowImport.getIntegrationPattern());
                }
                if (flowImport.getFrequency() != null) {
                    existingFlowImport.setFrequency(flowImport.getFrequency());
                }
                if (flowImport.getFormat() != null) {
                    existingFlowImport.setFormat(flowImport.getFormat());
                }
                if (flowImport.getSwagger() != null) {
                    existingFlowImport.setSwagger(flowImport.getSwagger());
                }
                if (flowImport.getBlueprint() != null) {
                    existingFlowImport.setBlueprint(flowImport.getBlueprint());
                }
                if (flowImport.getBlueprintStatus() != null) {
                    existingFlowImport.setBlueprintStatus(flowImport.getBlueprintStatus());
                }
                if (flowImport.getFlowStatus() != null) {
                    existingFlowImport.setFlowStatus(flowImport.getFlowStatus());
                }
                if (flowImport.getComment() != null) {
                    existingFlowImport.setComment(flowImport.getComment());
                }
                if (flowImport.getDocumentName() != null) {
                    existingFlowImport.setDocumentName(flowImport.getDocumentName());
                }
                if (flowImport.getImportInterfaceStatus() != null) {
                    existingFlowImport.setImportInterfaceStatus(flowImport.getImportInterfaceStatus());
                }
                if (flowImport.getImportFunctionalFlowStatus() != null) {
                    existingFlowImport.setImportFunctionalFlowStatus(flowImport.getImportFunctionalFlowStatus());
                }
                if (flowImport.getImportDataFlowStatus() != null) {
                    existingFlowImport.setImportDataFlowStatus(flowImport.getImportDataFlowStatus());
                }
                if (flowImport.getImportStatusMessage() != null) {
                    existingFlowImport.setImportStatusMessage(flowImport.getImportStatusMessage());
                }
                if (flowImport.getExistingApplicationID() != null) {
                    existingFlowImport.setExistingApplicationID(flowImport.getExistingApplicationID());
                }

                return existingFlowImport;
            })
            .map(flowImportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flowImport.getId().toString())
        );
    }

    /**
     * {@code GET  /flow-imports} : get all the flowImports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flowImports in body.
     */
    @GetMapping("/flow-imports")
    public List<FlowImport> getAllFlowImports() {
        log.debug("REST request to get all FlowImports");
        return flowImportRepository.findAll();
    }

    /**
     * {@code GET  /flow-imports/:id} : get the "id" flowImport.
     *
     * @param id the id of the flowImport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flowImport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flow-imports/{id}")
    public ResponseEntity<FlowImport> getFlowImport(@PathVariable Long id) {
        log.debug("REST request to get FlowImport : {}", id);
        Optional<FlowImport> flowImport = flowImportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(flowImport);
    }

    /**
     * {@code DELETE  /flow-imports/:id} : delete the "id" flowImport.
     *
     * @param id the id of the flowImport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flow-imports/{id}")
    public ResponseEntity<Void> deleteFlowImport(@PathVariable Long id) {
        log.debug("REST request to delete FlowImport : {}", id);
        flowImportRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
