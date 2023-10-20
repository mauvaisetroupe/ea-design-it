package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import com.mauvaisetroupe.eadesignit.repository.CapabilityApplicationMappingRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CapabilityApplicationMappingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CapabilityApplicationMappingResourceIT {

    private static final String ENTITY_API_URL = "/api/capability-application-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CapabilityApplicationMappingRepository capabilityApplicationMappingRepository;

    @Mock
    private CapabilityApplicationMappingRepository capabilityApplicationMappingRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCapabilityApplicationMappingMockMvc;

    private CapabilityApplicationMapping capabilityApplicationMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapabilityApplicationMapping createEntity(EntityManager em) {
        CapabilityApplicationMapping capabilityApplicationMapping = new CapabilityApplicationMapping();
        return capabilityApplicationMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapabilityApplicationMapping createUpdatedEntity(EntityManager em) {
        CapabilityApplicationMapping capabilityApplicationMapping = new CapabilityApplicationMapping();
        return capabilityApplicationMapping;
    }

    @BeforeEach
    public void initTest() {
        capabilityApplicationMapping = createEntity(em);
    }

    @Test
    @Transactional
    void createCapabilityApplicationMapping() throws Exception {
        int databaseSizeBeforeCreate = capabilityApplicationMappingRepository.findAll().size();
        // Create the CapabilityApplicationMapping
        restCapabilityApplicationMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capabilityApplicationMapping))
            )
            .andExpect(status().isCreated());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeCreate + 1);
        CapabilityApplicationMapping testCapabilityApplicationMapping = capabilityApplicationMappingList.get(
            capabilityApplicationMappingList.size() - 1
        );
    }

    @Test
    @Transactional
    void createCapabilityApplicationMappingWithExistingId() throws Exception {
        // Create the CapabilityApplicationMapping with an existing ID
        capabilityApplicationMapping.setId(1L);

        int databaseSizeBeforeCreate = capabilityApplicationMappingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapabilityApplicationMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capabilityApplicationMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCapabilityApplicationMappings() throws Exception {
        // Initialize the database
        capabilityApplicationMappingRepository.saveAndFlush(capabilityApplicationMapping);

        // Get all the capabilityApplicationMappingList
        restCapabilityApplicationMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capabilityApplicationMapping.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCapabilityApplicationMappingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(capabilityApplicationMappingRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCapabilityApplicationMappingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(capabilityApplicationMappingRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCapabilityApplicationMappingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(capabilityApplicationMappingRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCapabilityApplicationMappingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(capabilityApplicationMappingRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCapabilityApplicationMapping() throws Exception {
        // Initialize the database
        capabilityApplicationMappingRepository.saveAndFlush(capabilityApplicationMapping);

        // Get the capabilityApplicationMapping
        restCapabilityApplicationMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, capabilityApplicationMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capabilityApplicationMapping.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCapabilityApplicationMapping() throws Exception {
        // Get the capabilityApplicationMapping
        restCapabilityApplicationMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCapabilityApplicationMapping() throws Exception {
        // Initialize the database
        capabilityApplicationMappingRepository.saveAndFlush(capabilityApplicationMapping);

        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();

        // Update the capabilityApplicationMapping
        CapabilityApplicationMapping updatedCapabilityApplicationMapping = capabilityApplicationMappingRepository
            .findById(capabilityApplicationMapping.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCapabilityApplicationMapping are not directly saved in db
        em.detach(updatedCapabilityApplicationMapping);

        restCapabilityApplicationMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCapabilityApplicationMapping.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCapabilityApplicationMapping))
            )
            .andExpect(status().isOk());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
        CapabilityApplicationMapping testCapabilityApplicationMapping = capabilityApplicationMappingList.get(
            capabilityApplicationMappingList.size() - 1
        );
    }

    @Test
    @Transactional
    void putNonExistingCapabilityApplicationMapping() throws Exception {
        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();
        capabilityApplicationMapping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapabilityApplicationMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, capabilityApplicationMapping.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capabilityApplicationMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCapabilityApplicationMapping() throws Exception {
        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();
        capabilityApplicationMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityApplicationMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capabilityApplicationMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCapabilityApplicationMapping() throws Exception {
        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();
        capabilityApplicationMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityApplicationMappingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capabilityApplicationMapping))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCapabilityApplicationMappingWithPatch() throws Exception {
        // Initialize the database
        capabilityApplicationMappingRepository.saveAndFlush(capabilityApplicationMapping);

        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();

        // Update the capabilityApplicationMapping using partial update
        CapabilityApplicationMapping partialUpdatedCapabilityApplicationMapping = new CapabilityApplicationMapping();
        partialUpdatedCapabilityApplicationMapping.setId(capabilityApplicationMapping.getId());

        restCapabilityApplicationMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapabilityApplicationMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapabilityApplicationMapping))
            )
            .andExpect(status().isOk());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
        CapabilityApplicationMapping testCapabilityApplicationMapping = capabilityApplicationMappingList.get(
            capabilityApplicationMappingList.size() - 1
        );
    }

    @Test
    @Transactional
    void fullUpdateCapabilityApplicationMappingWithPatch() throws Exception {
        // Initialize the database
        capabilityApplicationMappingRepository.saveAndFlush(capabilityApplicationMapping);

        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();

        // Update the capabilityApplicationMapping using partial update
        CapabilityApplicationMapping partialUpdatedCapabilityApplicationMapping = new CapabilityApplicationMapping();
        partialUpdatedCapabilityApplicationMapping.setId(capabilityApplicationMapping.getId());

        restCapabilityApplicationMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapabilityApplicationMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapabilityApplicationMapping))
            )
            .andExpect(status().isOk());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
        CapabilityApplicationMapping testCapabilityApplicationMapping = capabilityApplicationMappingList.get(
            capabilityApplicationMappingList.size() - 1
        );
    }

    @Test
    @Transactional
    void patchNonExistingCapabilityApplicationMapping() throws Exception {
        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();
        capabilityApplicationMapping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapabilityApplicationMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, capabilityApplicationMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capabilityApplicationMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCapabilityApplicationMapping() throws Exception {
        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();
        capabilityApplicationMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityApplicationMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capabilityApplicationMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCapabilityApplicationMapping() throws Exception {
        int databaseSizeBeforeUpdate = capabilityApplicationMappingRepository.findAll().size();
        capabilityApplicationMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityApplicationMappingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capabilityApplicationMapping))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CapabilityApplicationMapping in the database
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCapabilityApplicationMapping() throws Exception {
        // Initialize the database
        capabilityApplicationMappingRepository.saveAndFlush(capabilityApplicationMapping);

        int databaseSizeBeforeDelete = capabilityApplicationMappingRepository.findAll().size();

        // Delete the capabilityApplicationMapping
        restCapabilityApplicationMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, capabilityApplicationMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CapabilityApplicationMapping> capabilityApplicationMappingList = capabilityApplicationMappingRepository.findAll();
        assertThat(capabilityApplicationMappingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
