package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.repository.OwnerRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.Owner}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OwnerResource {

    private final Logger log = LoggerFactory.getLogger(OwnerResource.class);

    private static final String ENTITY_NAME = "owner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OwnerRepository ownerRepository;

    public OwnerResource(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * {@code POST  /owners} : Create a new owner.
     *
     * @param owner the owner to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new owner, or with status {@code 400 (Bad Request)} if the owner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/owners")
    public ResponseEntity<Owner> createOwner(@Valid @RequestBody Owner owner) throws URISyntaxException {
        log.debug("REST request to save Owner : {}", owner);
        if (owner.getId() != null) {
            throw new BadRequestAlertException("A new owner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Owner result = ownerRepository.save(owner);
        return ResponseEntity
            .created(new URI("/api/owners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /owners/:id} : Updates an existing owner.
     *
     * @param id the id of the owner to save.
     * @param owner the owner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated owner,
     * or with status {@code 400 (Bad Request)} if the owner is not valid,
     * or with status {@code 500 (Internal Server Error)} if the owner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/owners/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Owner owner)
        throws URISyntaxException {
        log.debug("REST request to update Owner : {}, {}", id, owner);
        if (owner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, owner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Owner result = ownerRepository.save(owner);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, owner.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /owners/:id} : Partial updates given fields of an existing owner, field will ignore if it is null
     *
     * @param id the id of the owner to save.
     * @param owner the owner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated owner,
     * or with status {@code 400 (Bad Request)} if the owner is not valid,
     * or with status {@code 404 (Not Found)} if the owner is not found,
     * or with status {@code 500 (Internal Server Error)} if the owner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/owners/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Owner> partialUpdateOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Owner owner
    ) throws URISyntaxException {
        log.debug("REST request to partial update Owner partially : {}, {}", id, owner);
        if (owner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, owner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Owner> result = ownerRepository
            .findById(owner.getId())
            .map(existingOwner -> {
                if (owner.getName() != null) {
                    existingOwner.setName(owner.getName());
                }
                if (owner.getFirstname() != null) {
                    existingOwner.setFirstname(owner.getFirstname());
                }
                if (owner.getLastname() != null) {
                    existingOwner.setLastname(owner.getLastname());
                }
                if (owner.getEmail() != null) {
                    existingOwner.setEmail(owner.getEmail());
                }

                return existingOwner;
            })
            .map(ownerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, owner.getId().toString())
        );
    }

    /**
     * {@code GET  /owners} : get all the owners.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of owners in body.
     */
    @GetMapping("/owners")
    public List<Owner> getAllOwners(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Owners");
        return ownerRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /owners/:id} : get the "id" owner.
     *
     * @param id the id of the owner to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the owner, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/owners/{id}")
    public ResponseEntity<Owner> getOwner(@PathVariable Long id) {
        log.debug("REST request to get Owner : {}", id);
        Optional<Owner> owner = ownerRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(owner);
    }

    /**
     * {@code DELETE  /owners/:id} : delete the "id" owner.
     *
     * @param id the id of the owner to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/owners/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        log.debug("REST request to delete Owner : {}", id);
        ownerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
