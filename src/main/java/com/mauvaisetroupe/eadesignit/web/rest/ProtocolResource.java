package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.repository.ProtocolRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.Protocol}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProtocolResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolResource.class);

    private static final String ENTITY_NAME = "protocol";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProtocolRepository protocolRepository;

    public ProtocolResource(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    /**
     * {@code POST  /protocols} : Create a new protocol.
     *
     * @param protocol the protocol to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protocol, or with status {@code 400 (Bad Request)} if the protocol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/protocols")
    public ResponseEntity<Protocol> createProtocol(@Valid @RequestBody Protocol protocol) throws URISyntaxException {
        log.debug("REST request to save Protocol : {}", protocol);
        if (protocol.getId() != null) {
            throw new BadRequestAlertException("A new protocol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Protocol result = protocolRepository.save(protocol);
        return ResponseEntity
            .created(new URI("/api/protocols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /protocols/:id} : Updates an existing protocol.
     *
     * @param id the id of the protocol to save.
     * @param protocol the protocol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocol,
     * or with status {@code 400 (Bad Request)} if the protocol is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protocol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/protocols/{id}")
    public ResponseEntity<Protocol> updateProtocol(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Protocol protocol
    ) throws URISyntaxException {
        log.debug("REST request to update Protocol : {}, {}", id, protocol);
        if (protocol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, protocol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!protocolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Protocol result = protocolRepository.save(protocol);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocol.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /protocols/:id} : Partial updates given fields of an existing protocol, field will ignore if it is null
     *
     * @param id the id of the protocol to save.
     * @param protocol the protocol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocol,
     * or with status {@code 400 (Bad Request)} if the protocol is not valid,
     * or with status {@code 404 (Not Found)} if the protocol is not found,
     * or with status {@code 500 (Internal Server Error)} if the protocol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/protocols/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Protocol> partialUpdateProtocol(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Protocol protocol
    ) throws URISyntaxException {
        log.debug("REST request to partial update Protocol partially : {}, {}", id, protocol);
        if (protocol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, protocol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!protocolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Protocol> result = protocolRepository
            .findById(protocol.getId())
            .map(existingProtocol -> {
                if (protocol.getName() != null) {
                    existingProtocol.setName(protocol.getName());
                }
                if (protocol.getType() != null) {
                    existingProtocol.setType(protocol.getType());
                }
                if (protocol.getDescription() != null) {
                    existingProtocol.setDescription(protocol.getDescription());
                }
                if (protocol.getScope() != null) {
                    existingProtocol.setScope(protocol.getScope());
                }

                return existingProtocol;
            })
            .map(protocolRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, protocol.getId().toString())
        );
    }

    /**
     * {@code GET  /protocols} : get all the protocols.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protocols in body.
     */
    @GetMapping("/protocols")
    public List<Protocol> getAllProtocols() {
        log.debug("REST request to get all Protocols");
        return protocolRepository.findAll();
    }

    /**
     * {@code GET  /protocols/:id} : get the "id" protocol.
     *
     * @param id the id of the protocol to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocol, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/protocols/{id}")
    public ResponseEntity<Protocol> getProtocol(@PathVariable Long id) {
        log.debug("REST request to get Protocol : {}", id);
        Optional<Protocol> protocol = protocolRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(protocol);
    }

    /**
     * {@code DELETE  /protocols/:id} : delete the "id" protocol.
     *
     * @param id the id of the protocol to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/protocols/{id}")
    public ResponseEntity<Void> deleteProtocol(@PathVariable Long id) {
        log.debug("REST request to delete Protocol : {}", id);
        protocolRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
