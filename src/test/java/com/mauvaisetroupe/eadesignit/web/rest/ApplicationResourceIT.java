package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.Owner;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ApplicationType;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.service.criteria.ApplicationCriteria;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ApplicationType DEFAULT_TYPE = ApplicationType.MICROSERVICE;
    private static final ApplicationType UPDATED_TYPE = ApplicationType.EXTERNAL;

    private static final String DEFAULT_TECHNOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_TECHNOLOGY = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationMockMvc;

    private Application application;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .technology(DEFAULT_TECHNOLOGY)
            .comment(DEFAULT_COMMENT);
        return application;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createUpdatedEntity(EntityManager em) {
        Application application = new Application()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .technology(UPDATED_TECHNOLOGY)
            .comment(UPDATED_COMMENT);
        return application;
    }

    @BeforeEach
    public void initTest() {
        application = createEntity(em);
    }

    @Test
    @Transactional
    void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();
        // Create the Application
        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplication.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApplication.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testApplication.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createApplicationWithExistingId() throws Exception {
        // Create the Application with an existing ID
        application.setId("existing_id");

        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].technology").value(hasItem(DEFAULT_TECHNOLOGY)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.technology").value(DEFAULT_TECHNOLOGY))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getApplicationsByIdFiltering() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        String id = application.getId();

        defaultApplicationShouldBeFound("id.equals=" + id);
        defaultApplicationShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllApplicationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where name equals to DEFAULT_NAME
        defaultApplicationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the applicationList where name equals to UPDATED_NAME
        defaultApplicationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where name not equals to DEFAULT_NAME
        defaultApplicationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the applicationList where name not equals to UPDATED_NAME
        defaultApplicationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultApplicationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the applicationList where name equals to UPDATED_NAME
        defaultApplicationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where name is not null
        defaultApplicationShouldBeFound("name.specified=true");

        // Get all the applicationList where name is null
        defaultApplicationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationsByNameContainsSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where name contains DEFAULT_NAME
        defaultApplicationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the applicationList where name contains UPDATED_NAME
        defaultApplicationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where name does not contain DEFAULT_NAME
        defaultApplicationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the applicationList where name does not contain UPDATED_NAME
        defaultApplicationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApplicationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where description equals to DEFAULT_DESCRIPTION
        defaultApplicationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the applicationList where description equals to UPDATED_DESCRIPTION
        defaultApplicationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApplicationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where description not equals to DEFAULT_DESCRIPTION
        defaultApplicationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the applicationList where description not equals to UPDATED_DESCRIPTION
        defaultApplicationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApplicationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultApplicationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the applicationList where description equals to UPDATED_DESCRIPTION
        defaultApplicationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApplicationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where description is not null
        defaultApplicationShouldBeFound("description.specified=true");

        // Get all the applicationList where description is null
        defaultApplicationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where description contains DEFAULT_DESCRIPTION
        defaultApplicationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the applicationList where description contains UPDATED_DESCRIPTION
        defaultApplicationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApplicationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where description does not contain DEFAULT_DESCRIPTION
        defaultApplicationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the applicationList where description does not contain UPDATED_DESCRIPTION
        defaultApplicationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApplicationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where type equals to DEFAULT_TYPE
        defaultApplicationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the applicationList where type equals to UPDATED_TYPE
        defaultApplicationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllApplicationsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where type not equals to DEFAULT_TYPE
        defaultApplicationShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the applicationList where type not equals to UPDATED_TYPE
        defaultApplicationShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllApplicationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultApplicationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the applicationList where type equals to UPDATED_TYPE
        defaultApplicationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllApplicationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where type is not null
        defaultApplicationShouldBeFound("type.specified=true");

        // Get all the applicationList where type is null
        defaultApplicationShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationsByTechnologyIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where technology equals to DEFAULT_TECHNOLOGY
        defaultApplicationShouldBeFound("technology.equals=" + DEFAULT_TECHNOLOGY);

        // Get all the applicationList where technology equals to UPDATED_TECHNOLOGY
        defaultApplicationShouldNotBeFound("technology.equals=" + UPDATED_TECHNOLOGY);
    }

    @Test
    @Transactional
    void getAllApplicationsByTechnologyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where technology not equals to DEFAULT_TECHNOLOGY
        defaultApplicationShouldNotBeFound("technology.notEquals=" + DEFAULT_TECHNOLOGY);

        // Get all the applicationList where technology not equals to UPDATED_TECHNOLOGY
        defaultApplicationShouldBeFound("technology.notEquals=" + UPDATED_TECHNOLOGY);
    }

    @Test
    @Transactional
    void getAllApplicationsByTechnologyIsInShouldWork() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where technology in DEFAULT_TECHNOLOGY or UPDATED_TECHNOLOGY
        defaultApplicationShouldBeFound("technology.in=" + DEFAULT_TECHNOLOGY + "," + UPDATED_TECHNOLOGY);

        // Get all the applicationList where technology equals to UPDATED_TECHNOLOGY
        defaultApplicationShouldNotBeFound("technology.in=" + UPDATED_TECHNOLOGY);
    }

    @Test
    @Transactional
    void getAllApplicationsByTechnologyIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where technology is not null
        defaultApplicationShouldBeFound("technology.specified=true");

        // Get all the applicationList where technology is null
        defaultApplicationShouldNotBeFound("technology.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationsByTechnologyContainsSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where technology contains DEFAULT_TECHNOLOGY
        defaultApplicationShouldBeFound("technology.contains=" + DEFAULT_TECHNOLOGY);

        // Get all the applicationList where technology contains UPDATED_TECHNOLOGY
        defaultApplicationShouldNotBeFound("technology.contains=" + UPDATED_TECHNOLOGY);
    }

    @Test
    @Transactional
    void getAllApplicationsByTechnologyNotContainsSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where technology does not contain DEFAULT_TECHNOLOGY
        defaultApplicationShouldNotBeFound("technology.doesNotContain=" + DEFAULT_TECHNOLOGY);

        // Get all the applicationList where technology does not contain UPDATED_TECHNOLOGY
        defaultApplicationShouldBeFound("technology.doesNotContain=" + UPDATED_TECHNOLOGY);
    }

    @Test
    @Transactional
    void getAllApplicationsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where comment equals to DEFAULT_COMMENT
        defaultApplicationShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the applicationList where comment equals to UPDATED_COMMENT
        defaultApplicationShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllApplicationsByCommentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where comment not equals to DEFAULT_COMMENT
        defaultApplicationShouldNotBeFound("comment.notEquals=" + DEFAULT_COMMENT);

        // Get all the applicationList where comment not equals to UPDATED_COMMENT
        defaultApplicationShouldBeFound("comment.notEquals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllApplicationsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultApplicationShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the applicationList where comment equals to UPDATED_COMMENT
        defaultApplicationShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllApplicationsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where comment is not null
        defaultApplicationShouldBeFound("comment.specified=true");

        // Get all the applicationList where comment is null
        defaultApplicationShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationsByCommentContainsSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where comment contains DEFAULT_COMMENT
        defaultApplicationShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the applicationList where comment contains UPDATED_COMMENT
        defaultApplicationShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllApplicationsByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList where comment does not contain DEFAULT_COMMENT
        defaultApplicationShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the applicationList where comment does not contain UPDATED_COMMENT
        defaultApplicationShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllApplicationsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        Owner owner;
        if (TestUtil.findAll(em, Owner.class).isEmpty()) {
            owner = OwnerResourceIT.createEntity(em);
            em.persist(owner);
            em.flush();
        } else {
            owner = TestUtil.findAll(em, Owner.class).get(0);
        }
        em.persist(owner);
        em.flush();
        application.setOwner(owner);
        applicationRepository.saveAndFlush(application);
        Long ownerId = owner.getId();

        // Get all the applicationList where owner equals to ownerId
        defaultApplicationShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the applicationList where owner equals to (ownerId + 1)
        defaultApplicationShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationsByApplicationsListIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        ApplicationComponent applicationsList;
        if (TestUtil.findAll(em, ApplicationComponent.class).isEmpty()) {
            applicationsList = ApplicationComponentResourceIT.createEntity(em);
            em.persist(applicationsList);
            em.flush();
        } else {
            applicationsList = TestUtil.findAll(em, ApplicationComponent.class).get(0);
        }
        em.persist(applicationsList);
        em.flush();
        application.addApplicationsList(applicationsList);
        applicationRepository.saveAndFlush(application);
        Long applicationsListId = applicationsList.getId();

        // Get all the applicationList where applicationsList equals to applicationsListId
        defaultApplicationShouldBeFound("applicationsListId.equals=" + applicationsListId);

        // Get all the applicationList where applicationsList equals to (applicationsListId + 1)
        defaultApplicationShouldNotBeFound("applicationsListId.equals=" + (applicationsListId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationShouldBeFound(String filter) throws Exception {
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].technology").value(hasItem(DEFAULT_TECHNOLOGY)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationShouldNotBeFound(String filter) throws Exception {
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findById(application.getId()).get();
        // Disconnect from session so that the updates on updatedApplication are not directly saved in db
        em.detach(updatedApplication);
        updatedApplication
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .technology(UPDATED_TECHNOLOGY)
            .comment(UPDATED_COMMENT);

        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplication.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplication.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testApplication.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, application.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication.description(UPDATED_DESCRIPTION);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplication.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApplication.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testApplication.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .technology(UPDATED_TECHNOLOGY)
            .comment(UPDATED_COMMENT);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplication.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplication.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testApplication.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, application.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Delete the application
        restApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, application.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
