package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.FlowInterface}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FlowInterfaceResource {

    private final Logger log = LoggerFactory.getLogger(FlowInterfaceResource.class);

    private static final String ENTITY_NAME = "flowInterface";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlowInterfaceRepository flowInterfaceRepository;

    public FlowInterfaceResource(FlowInterfaceRepository flowInterfaceRepository) {
        this.flowInterfaceRepository = flowInterfaceRepository;
    }

    /**
     * {@code POST  /flow-interfaces} : Create a new flowInterface.
     *
     * @param flowInterface the flowInterface to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flowInterface, or with status {@code 400 (Bad Request)} if the flowInterface has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flow-interfaces")
    public ResponseEntity<FlowInterface> createFlowInterface(@Valid @RequestBody FlowInterface flowInterface) throws URISyntaxException {
        log.debug("REST request to save FlowInterface : {}", flowInterface);
        if (flowInterface.getId() != null) {
            throw new BadRequestAlertException("A new flowInterface cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlowInterface result = flowInterfaceRepository.save(flowInterface);
        return ResponseEntity
            .created(new URI("/api/flow-interfaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flow-interfaces/:id} : Updates an existing flowInterface.
     *
     * @param id the id of the flowInterface to save.
     * @param flowInterface the flowInterface to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowInterface,
     * or with status {@code 400 (Bad Request)} if the flowInterface is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flowInterface couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flow-interfaces/{id}")
    @PreAuthorize("@ownershipChecker.check(#flowInterface)")
    public ResponseEntity<FlowInterface> updateFlowInterface(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FlowInterface flowInterface
    ) throws URISyntaxException {
        log.debug("REST request to update FlowInterface : {}, {}", id, flowInterface);
        if (flowInterface.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowInterface.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowInterfaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlowInterface result = flowInterfaceRepository.save(flowInterface);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flowInterface.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flow-interfaces/:id} : Partial updates given fields of an existing flowInterface, field will ignore if it is null
     *
     * @param id the id of the flowInterface to save.
     * @param flowInterface the flowInterface to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowInterface,
     * or with status {@code 400 (Bad Request)} if the flowInterface is not valid,
     * or with status {@code 404 (Not Found)} if the flowInterface is not found,
     * or with status {@code 500 (Internal Server Error)} if the flowInterface couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flow-interfaces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("@ownershipChecker.check(#flowInterface)")
    public ResponseEntity<FlowInterface> partialUpdateFlowInterface(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FlowInterface flowInterface
    ) throws URISyntaxException {
        log.debug("REST request to partial update FlowInterface partially : {}, {}", id, flowInterface);
        if (flowInterface.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowInterface.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowInterfaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlowInterface> result = flowInterfaceRepository
            .findById(flowInterface.getId())
            .map(existingFlowInterface -> {
                if (flowInterface.getAlias() != null) {
                    existingFlowInterface.setAlias(flowInterface.getAlias());
                }
                if (flowInterface.getStatus() != null) {
                    existingFlowInterface.setStatus(flowInterface.getStatus());
                }
                if (flowInterface.getDocumentationURL() != null) {
                    existingFlowInterface.setDocumentationURL(flowInterface.getDocumentationURL());
                }
                if (flowInterface.getDocumentationURL2() != null) {
                    existingFlowInterface.setDocumentationURL2(flowInterface.getDocumentationURL2());
                }
                if (flowInterface.getStartDate() != null) {
                    existingFlowInterface.setStartDate(flowInterface.getStartDate());
                }
                if (flowInterface.getEndDate() != null) {
                    existingFlowInterface.setEndDate(flowInterface.getEndDate());
                }

                return existingFlowInterface;
            })
            .map(flowInterfaceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flowInterface.getId().toString())
        );
    }

    /**
     * {@code GET  /flow-interfaces} : get all the flowInterfaces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flowInterfaces in body.
     */
    @GetMapping("/flow-interfaces")
    public List<FlowInterface> getAllFlowInterfaces(@RequestParam(value = "search", required = false) String search) {
        if (search != null) {
            Long sourceId = null, targetId = null, protocolId = null;
            Pattern pattern = Pattern.compile("(\\w+?)(:)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                String key = matcher.group(1);
                String operator = matcher.group(2);
                String value = matcher.group(3);

                switch (key) {
                    case "sourceId":
                        sourceId = Long.parseLong(value);
                        break;
                    case "targetId":
                        targetId = Long.parseLong(value);
                        break;
                    case "protocolId":
                        protocolId = Long.parseLong(value);
                        break;
                    default:
                        break;
                }
            }
            if (protocolId == null) {
                return flowInterfaceRepository.findBySourceIdAndTargetId(sourceId, targetId);
            } else {
                return flowInterfaceRepository.findBySourceIdAndTargetIdAndProtocolId(sourceId, targetId, protocolId);
            }
        } else {
            log.debug("REST request to get all FlowInterfaces");
            return flowInterfaceRepository.findAll();
        }
    }

    /**
     * {@code GET  /flow-interfaces/:id} : get the "id" flowInterface.
     *
     * @param id the id of the flowInterface to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flowInterface, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flow-interfaces/{id}")
    public ResponseEntity<FlowInterface> getFlowInterface(@PathVariable Long id) {
        log.debug("REST request to get FlowInterface : {}", id);
        Optional<FlowInterface> flowInterface = flowInterfaceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(flowInterface);
    }

    /**
     * {@code DELETE  /flow-interfaces/:id} : delete the "id" flowInterface.
     *
     * @param id the id of the flowInterface to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flow-interfaces/{id}")
    public ResponseEntity<Void> deleteFlowInterface(@PathVariable Long id) {
        log.debug("REST request to delete FlowInterface : {}", id);
        flowInterfaceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
