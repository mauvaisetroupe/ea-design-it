package com.mauvaisetroupe.eadesignit.service;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Application}.
 */
@Service
@Transactional
public class ApplicationService {

    private final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    /**
     * Save a application.
     *
     * @param application the entity to save.
     * @return the persisted entity.
     */
    public Application save(Application application) {
        log.debug("Request to save Application : {}", application);
        return applicationRepository.save(application);
    }

    /**
     * Partially update a application.
     *
     * @param application the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Application> partialUpdate(Application application) {
        log.debug("Request to partially update Application : {}", application);

        return applicationRepository
            .findById(application.getId())
            .map(existingApplication -> {
                if (application.getAlias() != null) {
                    existingApplication.setAlias(application.getAlias());
                }
                if (application.getName() != null) {
                    existingApplication.setName(application.getName());
                }
                if (application.getDescription() != null) {
                    existingApplication.setDescription(application.getDescription());
                }
                if (application.getType() != null) {
                    existingApplication.setType(application.getType());
                }
                if (application.getTechnology() != null) {
                    existingApplication.setTechnology(application.getTechnology());
                }
                if (application.getComment() != null) {
                    existingApplication.setComment(application.getComment());
                }

                return existingApplication;
            })
            .map(applicationRepository::save);
    }

    /**
     * Get all the applications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Application> findAll(Pageable pageable) {
        log.debug("Request to get all Applications");
        return applicationRepository.findAll(pageable);
    }

    /**
     * Get one application by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Application> findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        return applicationRepository.findById(id);
    }

    /**
     * Delete the application by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Application : {}", id);
        applicationRepository.deleteById(id);
    }
}
