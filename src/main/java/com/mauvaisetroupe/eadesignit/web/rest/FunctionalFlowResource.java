package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.service.FunctionalflowService;
import com.mauvaisetroupe.eadesignit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.FunctionalFlow}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FunctionalFlowResource {

    private final Logger log = LoggerFactory.getLogger(FunctionalFlowResource.class);

    private static final String ENTITY_NAME = "functionalFlow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private FunctionalflowService functionalflowService;

    /**
     * {@code POST  /functional-flows} : Create a new functionalFlow.
     *
     * @param functionalFlow the functionalFlow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new functionalFlow, or with status {@code 400 (Bad Request)} if the functionalFlow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/functional-flows")
    public ResponseEntity<FunctionalFlow> createFunctionalFlow(@Valid @RequestBody FunctionalFlow functionalFlow)
        throws URISyntaxException {
        log.debug("REST request to save FunctionalFlow : {}", functionalFlow);
        if (functionalFlow.getId() != null) {
            throw new BadRequestAlertException("A new functionalFlow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FunctionalFlow result = functionalFlowRepository.save(functionalFlow);
        return ResponseEntity
            .created(new URI("/api/functional-flows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /functional-flows/:id} : Updates an existing functionalFlow.
     *
     * @param id the id of the functionalFlow to save.
     * @param functionalFlow the functionalFlow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated functionalFlow,
     * or with status {@code 400 (Bad Request)} if the functionalFlow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the functionalFlow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/functional-flows/{id}")
    public ResponseEntity<FunctionalFlow> updateFunctionalFlow(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FunctionalFlow functionalFlow
    ) throws URISyntaxException {
        log.debug("REST request to update FunctionalFlow : {}, {}", id, functionalFlow);
        if (functionalFlow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, functionalFlow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!functionalFlowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FunctionalFlow result = functionalFlowRepository.save(functionalFlow);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, functionalFlow.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /functional-flows/:id} : Partial updates given fields of an existing functionalFlow, field will ignore if it is null
     *
     * @param id the id of the functionalFlow to save.
     * @param functionalFlow the functionalFlow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated functionalFlow,
     * or with status {@code 400 (Bad Request)} if the functionalFlow is not valid,
     * or with status {@code 404 (Not Found)} if the functionalFlow is not found,
     * or with status {@code 500 (Internal Server Error)} if the functionalFlow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/functional-flows/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FunctionalFlow> partialUpdateFunctionalFlow(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FunctionalFlow functionalFlow
    ) throws URISyntaxException {
        log.debug("REST request to partial update FunctionalFlow partially : {}, {}", id, functionalFlow);
        if (functionalFlow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, functionalFlow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!functionalFlowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FunctionalFlow> result = functionalFlowRepository
            .findById(functionalFlow.getId())
            .map(existingFunctionalFlow -> {
                if (functionalFlow.getAlias() != null) {
                    existingFunctionalFlow.setAlias(functionalFlow.getAlias());
                }
                if (functionalFlow.getDescription() != null) {
                    existingFunctionalFlow.setDescription(functionalFlow.getDescription());
                }
                if (functionalFlow.getComment() != null) {
                    existingFunctionalFlow.setComment(functionalFlow.getComment());
                }
                if (functionalFlow.getStatus() != null) {
                    existingFunctionalFlow.setStatus(functionalFlow.getStatus());
                }
                if (functionalFlow.getDocumentationURL() != null) {
                    existingFunctionalFlow.setDocumentationURL(functionalFlow.getDocumentationURL());
                }
                if (functionalFlow.getDocumentationURL2() != null) {
                    existingFunctionalFlow.setDocumentationURL2(functionalFlow.getDocumentationURL2());
                }
                if (functionalFlow.getStartDate() != null) {
                    existingFunctionalFlow.setStartDate(functionalFlow.getStartDate());
                }
                if (functionalFlow.getEndDate() != null) {
                    existingFunctionalFlow.setEndDate(functionalFlow.getEndDate());
                }

                return existingFunctionalFlow;
            })
            .map(functionalFlowRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, functionalFlow.getId().toString())
        );
    }

    /**
     * {@code GET  /functional-flows} : get all the functionalFlows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of functionalFlows in body.
     */
    @GetMapping("/functional-flows")
    public List<FunctionalFlow> getAllFunctionalFlows() {
        log.debug("REST request to get all FunctionalFlows");
        return functionalFlowRepository.findAll();
    }

    /**
     * {@code GET  /functional-flows/:id} : get the "id" functionalFlow.
     *
     * @param id the id of the functionalFlow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the functionalFlow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/functional-flows/{id}")
    public ResponseEntity<FunctionalFlow> getFunctionalFlow(@PathVariable Long id) {
        log.debug("REST request to get FunctionalFlow : {}", id);
        Optional<FunctionalFlow> functionalFlow = functionalFlowRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(functionalFlow);
    }

    /**
     * {@code DELETE  /functional-flows/:id} : delete the "id" functionalFlow.
     *
     * @param id the id of the functionalFlow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/functional-flows/{id}")
    public ResponseEntity<Void> deleteFunctionalFlow(
        @PathVariable Long id,
        @RequestParam boolean deleteFlowInterfaces,
        @RequestParam boolean deleteDatas
    ) {
        log.debug("REST request to delete FunctionalFlow : {}", id);
        functionalflowService.delete(id, deleteFlowInterfaces, deleteDatas);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/functional-flows/new/applications")
    public FunctionalFlow getFunctionalNonPersistedFlowFromApplications(@RequestParam(value = "ids[]") Long[] ids) {
        log.debug("REST request to build non persisted FunctionalFlow with applications ids: {}", Arrays.toString(ids));
        FunctionalFlow functionalFlow = new FunctionalFlow();
        functionalFlow.setAlias("not persisted");
        Set<FlowInterface> interfaces = flowInterfaceRepository.findBySourceIdInAndTargetIdIn(ids, ids);
        SortedSet<FunctionalFlowStep> steps = new TreeSet<>();
        for (FlowInterface flowInterface : interfaces) {
            FunctionalFlowStep step = new FunctionalFlowStep();
            step.setFlowInterface(flowInterface);
            steps.add(step);
        }
        functionalFlow.setSteps(steps);
        return functionalFlow;
    }
}
