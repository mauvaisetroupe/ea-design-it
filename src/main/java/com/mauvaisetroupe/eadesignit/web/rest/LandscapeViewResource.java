package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
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
 * REST controller for managing {@link com.mauvaisetroupe.eadesignit.domain.LandscapeView}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LandscapeViewResource {

    private final Logger log = LoggerFactory.getLogger(LandscapeViewResource.class);

    private static final String ENTITY_NAME = "landscapeView";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LandscapeViewRepository landscapeViewRepository;

    public LandscapeViewResource(LandscapeViewRepository landscapeViewRepository) {
        this.landscapeViewRepository = landscapeViewRepository;
    }

    /**
     * {@code POST  /landscape-views} : Create a new landscapeView.
     *
     * @param landscapeView the landscapeView to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new landscapeView, or with status {@code 400 (Bad Request)} if the landscapeView has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/landscape-views")
    public ResponseEntity<LandscapeView> createLandscapeView(@RequestBody LandscapeView landscapeView) throws URISyntaxException {
        log.debug("REST request to save LandscapeView : {}", landscapeView);
        if (landscapeView.getId() != null) {
            throw new BadRequestAlertException("A new landscapeView cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LandscapeView result = landscapeViewRepository.save(landscapeView);
        return ResponseEntity
            .created(new URI("/api/landscape-views/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /landscape-views/:id} : Updates an existing landscapeView.
     *
     * @param id the id of the landscapeView to save.
     * @param landscapeView the landscapeView to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated landscapeView,
     * or with status {@code 400 (Bad Request)} if the landscapeView is not valid,
     * or with status {@code 500 (Internal Server Error)} if the landscapeView couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/landscape-views/{id}")
    public ResponseEntity<LandscapeView> updateLandscapeView(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LandscapeView landscapeView
    ) throws URISyntaxException {
        log.debug("REST request to update LandscapeView : {}, {}", id, landscapeView);
        if (landscapeView.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, landscapeView.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!landscapeViewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LandscapeView result = landscapeViewRepository.save(landscapeView);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, landscapeView.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /landscape-views/:id} : Partial updates given fields of an existing landscapeView, field will ignore if it is null
     *
     * @param id the id of the landscapeView to save.
     * @param landscapeView the landscapeView to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated landscapeView,
     * or with status {@code 400 (Bad Request)} if the landscapeView is not valid,
     * or with status {@code 404 (Not Found)} if the landscapeView is not found,
     * or with status {@code 500 (Internal Server Error)} if the landscapeView couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/landscape-views/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LandscapeView> partialUpdateLandscapeView(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LandscapeView landscapeView
    ) throws URISyntaxException {
        log.debug("REST request to partial update LandscapeView partially : {}, {}", id, landscapeView);
        if (landscapeView.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, landscapeView.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!landscapeViewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LandscapeView> result = landscapeViewRepository
            .findById(landscapeView.getId())
            .map(existingLandscapeView -> {
                if (landscapeView.getViewpoint() != null) {
                    existingLandscapeView.setViewpoint(landscapeView.getViewpoint());
                }
                if (landscapeView.getDiagramName() != null) {
                    existingLandscapeView.setDiagramName(landscapeView.getDiagramName());
                }
                if (landscapeView.getCompressedDrawXML() != null) {
                    existingLandscapeView.setCompressedDrawXML(landscapeView.getCompressedDrawXML());
                }
                if (landscapeView.getCompressedDrawSVG() != null) {
                    existingLandscapeView.setCompressedDrawSVG(landscapeView.getCompressedDrawSVG());
                }

                return existingLandscapeView;
            })
            .map(landscapeViewRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, landscapeView.getId().toString())
        );
    }

    /**
     * {@code GET  /landscape-views} : get all the landscapeViews.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of landscapeViews in body.
     */
    @GetMapping("/landscape-views")
    public List<LandscapeView> getAllLandscapeViews(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all LandscapeViews");
        if (eagerload) {
            return landscapeViewRepository.findAllWithEagerRelationships();
        } else {
            return landscapeViewRepository.findAll();
        }
    }

    /**
     * {@code GET  /landscape-views/:id} : get the "id" landscapeView.
     *
     * @param id the id of the landscapeView to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the landscapeView, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/landscape-views/{id}")
    public ResponseEntity<LandscapeView> getLandscapeView(@PathVariable Long id) {
        log.debug("REST request to get LandscapeView : {}", id);
        Optional<LandscapeView> landscapeView = landscapeViewRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(landscapeView);
    }

    /**
     * {@code DELETE  /landscape-views/:id} : delete the "id" landscapeView.
     *
     * @param id the id of the landscapeView to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/landscape-views/{id}")
    public ResponseEntity<Void> deleteLandscapeView(@PathVariable Long id) {
        log.debug("REST request to delete LandscapeView : {}", id);
        landscapeViewRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
