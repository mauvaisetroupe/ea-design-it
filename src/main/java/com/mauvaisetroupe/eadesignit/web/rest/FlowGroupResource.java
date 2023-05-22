package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import com.mauvaisetroupe.eadesignit.repository.FlowGroupRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.FlowGroup}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FlowGroupResource {

    private final Logger log = LoggerFactory.getLogger(FlowGroupResource.class);

    private static final String ENTITY_NAME = "flowGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlowGroupRepository flowGroupRepository;

    public FlowGroupResource(FlowGroupRepository flowGroupRepository) {
        this.flowGroupRepository = flowGroupRepository;
    }

    /**
     * {@code POST  /flow-groups} : Create a new flowGroup.
     *
     * @param flowGroup the flowGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flowGroup, or with status {@code 400 (Bad Request)} if the flowGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flow-groups")
    public ResponseEntity<FlowGroup> createFlowGroup(@Valid @RequestBody FlowGroup flowGroup) throws URISyntaxException {
        log.debug("REST request to save FlowGroup : {}", flowGroup);
        if (flowGroup.getId() != null) {
            throw new BadRequestAlertException("A new flowGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlowGroup result = flowGroupRepository.save(flowGroup);
        return ResponseEntity
            .created(new URI("/api/flow-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flow-groups/:id} : Updates an existing flowGroup.
     *
     * @param id the id of the flowGroup to save.
     * @param flowGroup the flowGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowGroup,
     * or with status {@code 400 (Bad Request)} if the flowGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flowGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flow-groups/{id}")
    public ResponseEntity<FlowGroup> updateFlowGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FlowGroup flowGroup
    ) throws URISyntaxException {
        log.debug("REST request to update FlowGroup : {}, {}", id, flowGroup);
        if (flowGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlowGroup result = flowGroupRepository.save(flowGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flowGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flow-groups/:id} : Partial updates given fields of an existing flowGroup, field will ignore if it is null
     *
     * @param id the id of the flowGroup to save.
     * @param flowGroup the flowGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowGroup,
     * or with status {@code 400 (Bad Request)} if the flowGroup is not valid,
     * or with status {@code 404 (Not Found)} if the flowGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the flowGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flow-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlowGroup> partialUpdateFlowGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FlowGroup flowGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update FlowGroup partially : {}, {}", id, flowGroup);
        if (flowGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlowGroup> result = flowGroupRepository
            .findById(flowGroup.getId())
            .map(existingFlowGroup -> {
                if (flowGroup.getTitle() != null) {
                    existingFlowGroup.setTitle(flowGroup.getTitle());
                }
                if (flowGroup.getUrl() != null) {
                    existingFlowGroup.setUrl(flowGroup.getUrl());
                }
                if (flowGroup.getDescription() != null) {
                    existingFlowGroup.setDescription(flowGroup.getDescription());
                }

                return existingFlowGroup;
            })
            .map(flowGroupRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flowGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /flow-groups} : get all the flowGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flowGroups in body.
     */
    @GetMapping("/flow-groups")
    public List<FlowGroup> getAllFlowGroups() {
        log.debug("REST request to get all FlowGroups");
        return flowGroupRepository.findAll();
    }

    /**
     * {@code GET  /flow-groups/:id} : get the "id" flowGroup.
     *
     * @param id the id of the flowGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flowGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flow-groups/{id}")
    public ResponseEntity<FlowGroup> getFlowGroup(@PathVariable Long id) {
        log.debug("REST request to get FlowGroup : {}", id);
        Optional<FlowGroup> flowGroup = flowGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(flowGroup);
    }

    /**
     * {@code DELETE  /flow-groups/:id} : delete the "id" flowGroup.
     *
     * @param id the id of the flowGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flow-groups/{id}")
    public ResponseEntity<Void> deleteFlowGroup(@PathVariable Long id) {
        log.debug("REST request to delete FlowGroup : {}", id);
        flowGroupRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
