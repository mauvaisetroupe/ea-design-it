package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowStepRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FunctionalFlowStepResource {

    private final Logger log = LoggerFactory.getLogger(FunctionalFlowStepResource.class);

    private static final String ENTITY_NAME = "functionalFlowStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FunctionalFlowStepRepository functionalFlowStepRepository;

    @Autowired
    private FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    public FunctionalFlowStepResource(FunctionalFlowStepRepository functionalFlowStepRepository) {
        this.functionalFlowStepRepository = functionalFlowStepRepository;
    }

    /**
     * {@code POST  /functional-flow-steps} : Create a new functionalFlowStep.
     *
     * @param functionalFlowStep the functionalFlowStep to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new functionalFlowStep, or with status {@code 400 (Bad Request)} if the functionalFlowStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/functional-flow-steps")
    public ResponseEntity<FunctionalFlowStep> createFunctionalFlowStep(@Valid @RequestBody FunctionalFlowStep functionalFlowStep)
        throws URISyntaxException {
        log.debug("REST request to save FunctionalFlowStep : {}", functionalFlowStep);
        if (functionalFlowStep.getId() != null) {
            throw new BadRequestAlertException("A new functionalFlowStep cannot already have an ID", ENTITY_NAME, "idexists");
        }

        FunctionalFlow flow = functionalFlowRepository.getById(functionalFlowStep.getFlow().getId());
        flow.addSteps(functionalFlowStep);
        functionalFlowRepository.save(flow);

        FlowInterface _interface = flowInterfaceRepository.getById(functionalFlowStep.getFlowInterface().getId());
        _interface.addSteps(functionalFlowStep);
        flowInterfaceRepository.save(_interface);

        FunctionalFlowStep result = functionalFlowStepRepository.save(functionalFlowStep);
        return ResponseEntity
            .created(new URI("/api/functional-flow-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /functional-flow-steps/:id} : Updates an existing functionalFlowStep.
     *
     * @param id the id of the functionalFlowStep to save.
     * @param functionalFlowStep the functionalFlowStep to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated functionalFlowStep,
     * or with status {@code 400 (Bad Request)} if the functionalFlowStep is not valid,
     * or with status {@code 500 (Internal Server Error)} if the functionalFlowStep couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/functional-flow-steps/{id}")
    public ResponseEntity<FunctionalFlowStep> updateFunctionalFlowStep(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FunctionalFlowStep functionalFlowStep
    ) throws URISyntaxException {
        log.debug("REST request to update FunctionalFlowStep : {}, {}", id, functionalFlowStep);
        if (functionalFlowStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, functionalFlowStep.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!functionalFlowStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FunctionalFlowStep stepFromDB = functionalFlowStepRepository.findById(id).get();
        FunctionalFlow oldFlow = stepFromDB.getFlow();
        FlowInterface oldInterface = stepFromDB.getFlowInterface();
        FunctionalFlow newFlow = functionalFlowStep.getFlow();
        FlowInterface newInterface = functionalFlowStep.getFlowInterface();

        if (!stepFromDB.getFlow().getId().equals(functionalFlowStep.getFlow().getId())) {
            oldFlow.removeSteps(stepFromDB);
            newFlow.addSteps(stepFromDB);
            functionalFlowRepository.save(oldFlow);
            functionalFlowRepository.save(newFlow);
        }

        if (!stepFromDB.getFlowInterface().getId().equals(functionalFlowStep.getFlowInterface().getId())) {
            oldInterface.removeSteps(stepFromDB);
            newInterface.addSteps(stepFromDB);
            flowInterfaceRepository.save(oldInterface);
            flowInterfaceRepository.save(newInterface);
        }

        stepFromDB.setDescription(functionalFlowStep.getDescription());
        stepFromDB.setStepOrder(functionalFlowStep.getStepOrder());

        FunctionalFlowStep result = functionalFlowStepRepository.save(stepFromDB);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, functionalFlowStep.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /functional-flow-steps/:id} : Partial updates given fields of an existing functionalFlowStep, field will ignore if it is null
     *
     * @param id the id of the functionalFlowStep to save.
     * @param functionalFlowStep the functionalFlowStep to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated functionalFlowStep,
     * or with status {@code 400 (Bad Request)} if the functionalFlowStep is not valid,
     * or with status {@code 404 (Not Found)} if the functionalFlowStep is not found,
     * or with status {@code 500 (Internal Server Error)} if the functionalFlowStep couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/functional-flow-steps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FunctionalFlowStep> partialUpdateFunctionalFlowStep(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FunctionalFlowStep functionalFlowStep
    ) throws URISyntaxException {
        log.debug("REST request to partial update FunctionalFlowStep partially : {}, {}", id, functionalFlowStep);
        if (functionalFlowStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, functionalFlowStep.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!functionalFlowStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FunctionalFlowStep> result = functionalFlowStepRepository
            .findById(functionalFlowStep.getId())
            .map(existingFunctionalFlowStep -> {
                if (functionalFlowStep.getDescription() != null) {
                    existingFunctionalFlowStep.setDescription(functionalFlowStep.getDescription());
                }
                if (functionalFlowStep.getStepOrder() != null) {
                    existingFunctionalFlowStep.setStepOrder(functionalFlowStep.getStepOrder());
                }

                return existingFunctionalFlowStep;
            })
            .map(functionalFlowStepRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, functionalFlowStep.getId().toString())
        );
    }

    /**
     * {@code GET  /functional-flow-steps} : get all the functionalFlowSteps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of functionalFlowSteps in body.
     */
    @GetMapping("/functional-flow-steps")
    public List<FunctionalFlowStep> getAllFunctionalFlowSteps() {
        log.debug("REST request to get all FunctionalFlowSteps");
        return functionalFlowStepRepository.findAll();
    }

    /**
     * {@code GET  /functional-flow-steps/:id} : get the "id" functionalFlowStep.
     *
     * @param id the id of the functionalFlowStep to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the functionalFlowStep, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/functional-flow-steps/{id}")
    public ResponseEntity<FunctionalFlowStep> getFunctionalFlowStep(@PathVariable Long id) {
        log.debug("REST request to get FunctionalFlowStep : {}", id);
        Optional<FunctionalFlowStep> functionalFlowStep = functionalFlowStepRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(functionalFlowStep);
    }

    /**
     * {@code DELETE  /functional-flow-steps/:id} : delete the "id" functionalFlowStep.
     *
     * @param id the id of the functionalFlowStep to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/functional-flow-steps/{id}")
    public ResponseEntity<Void> deleteFunctionalFlowStep(@PathVariable Long id) {
        FunctionalFlowStep step = functionalFlowStepRepository.findById(id).get();

        FunctionalFlow functionalFlow = step.getFlow();
        functionalFlow.removeSteps(step);
        functionalFlowRepository.save(functionalFlow);

        FlowInterface flowInterface = step.getFlowInterface();
        flowInterface.removeSteps(step);
        flowInterfaceRepository.save(flowInterface);

        log.debug("REST request to delete FunctionalFlowStep : {}", id);
        functionalFlowStepRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
