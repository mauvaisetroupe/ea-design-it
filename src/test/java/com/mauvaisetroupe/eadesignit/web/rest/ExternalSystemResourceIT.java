package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.ExternalSystem;
import com.mauvaisetroupe.eadesignit.repository.ExternalSystemRepository;
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
 * Integration tests for the {@link ExternalSystemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = { "ROLE_USER", "ROLE_WRITE" })
class ExternalSystemResourceIT {

    private static final String DEFAULT_EXTERNAL_SYSTEM_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_SYSTEM_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/external-systems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExternalSystemRepository externalSystemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExternalSystemMockMvc;

    private ExternalSystem externalSystem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalSystem createEntity(EntityManager em) {
        ExternalSystem externalSystem = new ExternalSystem().externalSystemID(DEFAULT_EXTERNAL_SYSTEM_ID);
        return externalSystem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalSystem createUpdatedEntity(EntityManager em) {
        ExternalSystem externalSystem = new ExternalSystem().externalSystemID(UPDATED_EXTERNAL_SYSTEM_ID);
        return externalSystem;
    }

    @BeforeEach
    public void initTest() {
        externalSystem = createEntity(em);
    }

    @Test
    @Transactional
    void createExternalSystem() throws Exception {
        int databaseSizeBeforeCreate = externalSystemRepository.findAll().size();
        // Create the ExternalSystem
        restExternalSystemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(externalSystem))
            )
            .andExpect(status().isCreated());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeCreate + 1);
        ExternalSystem testExternalSystem = externalSystemList.get(externalSystemList.size() - 1);
        assertThat(testExternalSystem.getExternalSystemID()).isEqualTo(DEFAULT_EXTERNAL_SYSTEM_ID);
    }

    @Test
    @Transactional
    void createExternalSystemWithExistingId() throws Exception {
        // Create the ExternalSystem with an existing ID
        externalSystem.setId(1L);

        int databaseSizeBeforeCreate = externalSystemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExternalSystemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(externalSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExternalSystems() throws Exception {
        // Initialize the database
        externalSystemRepository.saveAndFlush(externalSystem);

        // Get all the externalSystemList
        restExternalSystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalSystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].externalSystemID").value(hasItem(DEFAULT_EXTERNAL_SYSTEM_ID)));
    }

    @Test
    @Transactional
    void getExternalSystem() throws Exception {
        // Initialize the database
        externalSystemRepository.saveAndFlush(externalSystem);

        // Get the externalSystem
        restExternalSystemMockMvc
            .perform(get(ENTITY_API_URL_ID, externalSystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(externalSystem.getId().intValue()))
            .andExpect(jsonPath("$.externalSystemID").value(DEFAULT_EXTERNAL_SYSTEM_ID));
    }

    @Test
    @Transactional
    void getNonExistingExternalSystem() throws Exception {
        // Get the externalSystem
        restExternalSystemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExternalSystem() throws Exception {
        // Initialize the database
        externalSystemRepository.saveAndFlush(externalSystem);

        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();

        // Update the externalSystem
        ExternalSystem updatedExternalSystem = externalSystemRepository.findById(externalSystem.getId()).get();
        // Disconnect from session so that the updates on updatedExternalSystem are not directly saved in db
        em.detach(updatedExternalSystem);
        updatedExternalSystem.externalSystemID(UPDATED_EXTERNAL_SYSTEM_ID);

        restExternalSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExternalSystem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExternalSystem))
            )
            .andExpect(status().isOk());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
        ExternalSystem testExternalSystem = externalSystemList.get(externalSystemList.size() - 1);
        assertThat(testExternalSystem.getExternalSystemID()).isEqualTo(UPDATED_EXTERNAL_SYSTEM_ID);
    }

    @Test
    @Transactional
    void putNonExistingExternalSystem() throws Exception {
        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();
        externalSystem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalSystem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(externalSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExternalSystem() throws Exception {
        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();
        externalSystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(externalSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExternalSystem() throws Exception {
        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();
        externalSystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalSystemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(externalSystem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExternalSystemWithPatch() throws Exception {
        // Initialize the database
        externalSystemRepository.saveAndFlush(externalSystem);

        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();

        // Update the externalSystem using partial update
        ExternalSystem partialUpdatedExternalSystem = new ExternalSystem();
        partialUpdatedExternalSystem.setId(externalSystem.getId());

        partialUpdatedExternalSystem.externalSystemID(UPDATED_EXTERNAL_SYSTEM_ID);

        restExternalSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalSystem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExternalSystem))
            )
            .andExpect(status().isOk());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
        ExternalSystem testExternalSystem = externalSystemList.get(externalSystemList.size() - 1);
        assertThat(testExternalSystem.getExternalSystemID()).isEqualTo(UPDATED_EXTERNAL_SYSTEM_ID);
    }

    @Test
    @Transactional
    void fullUpdateExternalSystemWithPatch() throws Exception {
        // Initialize the database
        externalSystemRepository.saveAndFlush(externalSystem);

        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();

        // Update the externalSystem using partial update
        ExternalSystem partialUpdatedExternalSystem = new ExternalSystem();
        partialUpdatedExternalSystem.setId(externalSystem.getId());

        partialUpdatedExternalSystem.externalSystemID(UPDATED_EXTERNAL_SYSTEM_ID);

        restExternalSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalSystem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExternalSystem))
            )
            .andExpect(status().isOk());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
        ExternalSystem testExternalSystem = externalSystemList.get(externalSystemList.size() - 1);
        assertThat(testExternalSystem.getExternalSystemID()).isEqualTo(UPDATED_EXTERNAL_SYSTEM_ID);
    }

    @Test
    @Transactional
    void patchNonExistingExternalSystem() throws Exception {
        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();
        externalSystem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, externalSystem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(externalSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExternalSystem() throws Exception {
        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();
        externalSystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(externalSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExternalSystem() throws Exception {
        int databaseSizeBeforeUpdate = externalSystemRepository.findAll().size();
        externalSystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalSystemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(externalSystem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalSystem in the database
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = { "ROLE_HARD_DELETE", "ROLE_ADMIN", "ROLE_WRITE" })
    void deleteExternalSystem() throws Exception {
        // Initialize the database
        externalSystemRepository.saveAndFlush(externalSystem);

        int databaseSizeBeforeDelete = externalSystemRepository.findAll().size();

        // Delete the externalSystem
        restExternalSystemMockMvc
            .perform(delete(ENTITY_API_URL_ID, externalSystem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExternalSystem> externalSystemList = externalSystemRepository.findAll();
        assertThat(externalSystemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
