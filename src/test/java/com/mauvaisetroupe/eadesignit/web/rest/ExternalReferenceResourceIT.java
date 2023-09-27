package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.ExternalReference;
import com.mauvaisetroupe.eadesignit.repository.ExternalReferenceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExternalReferenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = { "ROLE_USER", "ROLE_WRITE" })
class ExternalReferenceResourceIT {

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/external-references";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExternalReferenceRepository externalReferenceRepository;

    @Mock
    private ExternalReferenceRepository externalReferenceRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExternalReferenceMockMvc;

    private ExternalReference externalReference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalReference createEntity(EntityManager em) {
        ExternalReference externalReference = new ExternalReference().externalID(DEFAULT_EXTERNAL_ID);
        return externalReference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalReference createUpdatedEntity(EntityManager em) {
        ExternalReference externalReference = new ExternalReference().externalID(UPDATED_EXTERNAL_ID);
        return externalReference;
    }

    @BeforeEach
    public void initTest() {
        externalReference = createEntity(em);
    }

    @Test
    @Transactional
    void createExternalReference() throws Exception {
        int databaseSizeBeforeCreate = externalReferenceRepository.findAll().size();
        // Create the ExternalReference
        restExternalReferenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(externalReference))
            )
            .andExpect(status().isCreated());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeCreate + 1);
        ExternalReference testExternalReference = externalReferenceList.get(externalReferenceList.size() - 1);
        assertThat(testExternalReference.getExternalID()).isEqualTo(DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void createExternalReferenceWithExistingId() throws Exception {
        // Create the ExternalReference with an existing ID
        externalReference.setId(1L);

        int databaseSizeBeforeCreate = externalReferenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExternalReferenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(externalReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExternalReferences() throws Exception {
        // Initialize the database
        externalReferenceRepository.saveAndFlush(externalReference);

        // Get all the externalReferenceList
        restExternalReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].externalID").value(hasItem(DEFAULT_EXTERNAL_ID)));
    }

    @Test
    @Transactional
    void getExternalReference() throws Exception {
        // Initialize the database
        externalReferenceRepository.saveAndFlush(externalReference);

        // Get the externalReference
        restExternalReferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, externalReference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(externalReference.getId().intValue()))
            .andExpect(jsonPath("$.externalID").value(DEFAULT_EXTERNAL_ID));
    }

    @Test
    @Transactional
    void getNonExistingExternalReference() throws Exception {
        // Get the externalReference
        restExternalReferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExternalReference() throws Exception {
        // Initialize the database
        externalReferenceRepository.saveAndFlush(externalReference);

        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();

        // Update the externalReference
        ExternalReference updatedExternalReference = externalReferenceRepository.findById(externalReference.getId()).get();
        // Disconnect from session so that the updates on updatedExternalReference are not directly saved in db
        em.detach(updatedExternalReference);
        updatedExternalReference.externalID(UPDATED_EXTERNAL_ID);

        restExternalReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExternalReference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExternalReference))
            )
            .andExpect(status().isOk());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
        ExternalReference testExternalReference = externalReferenceList.get(externalReferenceList.size() - 1);
        assertThat(testExternalReference.getExternalID()).isEqualTo(UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void putNonExistingExternalReference() throws Exception {
        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();
        externalReference.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalReference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(externalReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExternalReference() throws Exception {
        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();
        externalReference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(externalReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExternalReference() throws Exception {
        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();
        externalReference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalReferenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(externalReference))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExternalReferenceWithPatch() throws Exception {
        // Initialize the database
        externalReferenceRepository.saveAndFlush(externalReference);

        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();

        // Update the externalReference using partial update
        ExternalReference partialUpdatedExternalReference = new ExternalReference();
        partialUpdatedExternalReference.setId(externalReference.getId());

        restExternalReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExternalReference))
            )
            .andExpect(status().isOk());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
        ExternalReference testExternalReference = externalReferenceList.get(externalReferenceList.size() - 1);
        assertThat(testExternalReference.getExternalID()).isEqualTo(DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void fullUpdateExternalReferenceWithPatch() throws Exception {
        // Initialize the database
        externalReferenceRepository.saveAndFlush(externalReference);

        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();

        // Update the externalReference using partial update
        ExternalReference partialUpdatedExternalReference = new ExternalReference();
        partialUpdatedExternalReference.setId(externalReference.getId());

        partialUpdatedExternalReference.externalID(UPDATED_EXTERNAL_ID);

        restExternalReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExternalReference))
            )
            .andExpect(status().isOk());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
        ExternalReference testExternalReference = externalReferenceList.get(externalReferenceList.size() - 1);
        assertThat(testExternalReference.getExternalID()).isEqualTo(UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void patchNonExistingExternalReference() throws Exception {
        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();
        externalReference.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, externalReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(externalReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExternalReference() throws Exception {
        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();
        externalReference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(externalReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExternalReference() throws Exception {
        int databaseSizeBeforeUpdate = externalReferenceRepository.findAll().size();
        externalReference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(externalReference))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalReference in the database
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = { "ROLE_HARD_DELETE", "ROLE_ADMIN", "ROLE_WRITE" })
    void deleteExternalReference() throws Exception {
        // Initialize the database
        externalReferenceRepository.saveAndFlush(externalReference);

        int databaseSizeBeforeDelete = externalReferenceRepository.findAll().size();

        // Delete the externalReference
        restExternalReferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, externalReference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExternalReference> externalReferenceList = externalReferenceRepository.findAll();
        assertThat(externalReferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
