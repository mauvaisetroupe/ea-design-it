package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.EventData;
import com.mauvaisetroupe.eadesignit.repository.EventDataRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.EventData}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EventDataResource {

    private final Logger log = LoggerFactory.getLogger(EventDataResource.class);

    private static final String ENTITY_NAME = "eventData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventDataRepository eventDataRepository;

    public EventDataResource(EventDataRepository eventDataRepository) {
        this.eventDataRepository = eventDataRepository;
    }

    /**
     * {@code POST  /event-data} : Create a new eventData.
     *
     * @param eventData the eventData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventData, or with status {@code 400 (Bad Request)} if the eventData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-data")
    public ResponseEntity<EventData> createEventData(@Valid @RequestBody EventData eventData) throws URISyntaxException {
        log.debug("REST request to save EventData : {}", eventData);
        if (eventData.getId() != null) {
            throw new BadRequestAlertException("A new eventData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventData result = eventDataRepository.save(eventData);
        return ResponseEntity
            .created(new URI("/api/event-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-data/:id} : Updates an existing eventData.
     *
     * @param id the id of the eventData to save.
     * @param eventData the eventData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventData,
     * or with status {@code 400 (Bad Request)} if the eventData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-data/{id}")
    public ResponseEntity<EventData> updateEventData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventData eventData
    ) throws URISyntaxException {
        log.debug("REST request to update EventData : {}, {}", id, eventData);
        if (eventData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventData result = eventDataRepository.save(eventData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-data/:id} : Partial updates given fields of an existing eventData, field will ignore if it is null
     *
     * @param id the id of the eventData to save.
     * @param eventData the eventData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventData,
     * or with status {@code 400 (Bad Request)} if the eventData is not valid,
     * or with status {@code 404 (Not Found)} if the eventData is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventData> partialUpdateEventData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventData eventData
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventData partially : {}, {}", id, eventData);
        if (eventData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventData> result = eventDataRepository
            .findById(eventData.getId())
            .map(existingEventData -> {
                if (eventData.getName() != null) {
                    existingEventData.setName(eventData.getName());
                }
                if (eventData.getContractURL() != null) {
                    existingEventData.setContractURL(eventData.getContractURL());
                }
                if (eventData.getDocumentationURL() != null) {
                    existingEventData.setDocumentationURL(eventData.getDocumentationURL());
                }
                if (eventData.getStartDate() != null) {
                    existingEventData.setStartDate(eventData.getStartDate());
                }
                if (eventData.getEndDate() != null) {
                    existingEventData.setEndDate(eventData.getEndDate());
                }

                return existingEventData;
            })
            .map(eventDataRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventData.getId().toString())
        );
    }

    /**
     * {@code GET  /event-data} : get all the eventData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventData in body.
     */
    @GetMapping("/event-data")
    public List<EventData> getAllEventData() {
        log.debug("REST request to get all EventData");
        return eventDataRepository.findAll();
    }

    /**
     * {@code GET  /event-data/:id} : get the "id" eventData.
     *
     * @param id the id of the eventData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-data/{id}")
    public ResponseEntity<EventData> getEventData(@PathVariable Long id) {
        log.debug("REST request to get EventData : {}", id);
        Optional<EventData> eventData = eventDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(eventData);
    }

    /**
     * {@code DELETE  /event-data/:id} : delete the "id" eventData.
     *
     * @param id the id of the eventData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-data/{id}")
    public ResponseEntity<Void> deleteEventData(@PathVariable Long id) {
        log.debug("REST request to delete EventData : {}", id);
        eventDataRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
