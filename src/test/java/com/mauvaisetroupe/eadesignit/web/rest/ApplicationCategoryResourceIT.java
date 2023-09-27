package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.ApplicationCategory;
import com.mauvaisetroupe.eadesignit.repository.ApplicationCategoryRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link ApplicationCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = { "ROLE_USER", "ROLE_WRITE" })
class ApplicationCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationCategoryRepository applicationCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationCategoryMockMvc;

    private ApplicationCategory applicationCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationCategory createEntity(EntityManager em) {
        ApplicationCategory applicationCategory = new ApplicationCategory()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return applicationCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationCategory createUpdatedEntity(EntityManager em) {
        ApplicationCategory applicationCategory = new ApplicationCategory()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);
        return applicationCategory;
    }

    @BeforeEach
    public void initTest() {
        applicationCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationCategory() throws Exception {
        int databaseSizeBeforeCreate = applicationCategoryRepository.findAll().size();
        // Create the ApplicationCategory
        restApplicationCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationCategory testApplicationCategory = applicationCategoryList.get(applicationCategoryList.size() - 1);
        assertThat(testApplicationCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicationCategory.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApplicationCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createApplicationCategoryWithExistingId() throws Exception {
        // Create the ApplicationCategory with an existing ID
        applicationCategory.setId(1L);

        int databaseSizeBeforeCreate = applicationCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationCategoryRepository.findAll().size();
        // set the field null
        applicationCategory.setName(null);

        // Create the ApplicationCategory, which fails.

        restApplicationCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isBadRequest());

        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationCategories() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        // Get all the applicationCategoryList
        restApplicationCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getApplicationCategory() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        // Get the applicationCategory
        restApplicationCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingApplicationCategory() throws Exception {
        // Get the applicationCategory
        restApplicationCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationCategory() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();

        // Update the applicationCategory
        ApplicationCategory updatedApplicationCategory = applicationCategoryRepository.findById(applicationCategory.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationCategory are not directly saved in db
        em.detach(updatedApplicationCategory);
        updatedApplicationCategory.name(UPDATED_NAME).type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restApplicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicationCategory))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationCategory testApplicationCategory = applicationCategoryList.get(applicationCategoryList.size() - 1);
        assertThat(testApplicationCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicationCategory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplicationCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationCategoryWithPatch() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();

        // Update the applicationCategory using partial update
        ApplicationCategory partialUpdatedApplicationCategory = new ApplicationCategory();
        partialUpdatedApplicationCategory.setId(applicationCategory.getId());

        partialUpdatedApplicationCategory.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationCategory))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationCategory testApplicationCategory = applicationCategoryList.get(applicationCategoryList.size() - 1);
        assertThat(testApplicationCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicationCategory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplicationCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateApplicationCategoryWithPatch() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();

        // Update the applicationCategory using partial update
        ApplicationCategory partialUpdatedApplicationCategory = new ApplicationCategory();
        partialUpdatedApplicationCategory.setId(applicationCategory.getId());

        partialUpdatedApplicationCategory.name(UPDATED_NAME).type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationCategory))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationCategory testApplicationCategory = applicationCategoryList.get(applicationCategoryList.size() - 1);
        assertThat(testApplicationCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicationCategory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplicationCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationCategory() throws Exception {
        int databaseSizeBeforeUpdate = applicationCategoryRepository.findAll().size();
        applicationCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationCategory in the database
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = { "ROLE_HARD_DELETE" })
    void deleteApplicationCategory() throws Exception {
        // Initialize the database
        applicationCategoryRepository.saveAndFlush(applicationCategory);

        int databaseSizeBeforeDelete = applicationCategoryRepository.findAll().size();

        // Delete the applicationCategory
        restApplicationCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationCategory> applicationCategoryList = applicationCategoryRepository.findAll();
        assertThat(applicationCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
