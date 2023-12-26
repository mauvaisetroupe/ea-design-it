package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.OrganizationalEntity;
import com.mauvaisetroupe.eadesignit.repository.OrganizationalEntityRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrganizationalEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationalEntityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organizational-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationalEntityRepository organizationalEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationalEntityMockMvc;

    private OrganizationalEntity organizationalEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationalEntity createEntity(EntityManager em) {
        OrganizationalEntity organizationalEntity = new OrganizationalEntity().name(DEFAULT_NAME);
        return organizationalEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationalEntity createUpdatedEntity(EntityManager em) {
        OrganizationalEntity organizationalEntity = new OrganizationalEntity().name(UPDATED_NAME);
        return organizationalEntity;
    }

    @BeforeEach
    public void initTest() {
        organizationalEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganizationalEntity() throws Exception {
        int databaseSizeBeforeCreate = organizationalEntityRepository.findAll().size();
        // Create the OrganizationalEntity
        restOrganizationalEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isCreated());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationalEntity testOrganizationalEntity = organizationalEntityList.get(organizationalEntityList.size() - 1);
        assertThat(testOrganizationalEntity.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOrganizationalEntityWithExistingId() throws Exception {
        // Create the OrganizationalEntity with an existing ID
        organizationalEntity.setId(1L);

        int databaseSizeBeforeCreate = organizationalEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationalEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationalEntityRepository.findAll().size();
        // set the field null
        organizationalEntity.setName(null);

        // Create the OrganizationalEntity, which fails.

        restOrganizationalEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isBadRequest());

        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganizationalEntities() throws Exception {
        // Initialize the database
        organizationalEntityRepository.saveAndFlush(organizationalEntity);

        // Get all the organizationalEntityList
        restOrganizationalEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationalEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getOrganizationalEntity() throws Exception {
        // Initialize the database
        organizationalEntityRepository.saveAndFlush(organizationalEntity);

        // Get the organizationalEntity
        restOrganizationalEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, organizationalEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationalEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOrganizationalEntity() throws Exception {
        // Get the organizationalEntity
        restOrganizationalEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganizationalEntity() throws Exception {
        // Initialize the database
        organizationalEntityRepository.saveAndFlush(organizationalEntity);

        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();

        // Update the organizationalEntity
        OrganizationalEntity updatedOrganizationalEntity = organizationalEntityRepository
            .findById(organizationalEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedOrganizationalEntity are not directly saved in db
        em.detach(updatedOrganizationalEntity);
        updatedOrganizationalEntity.name(UPDATED_NAME);

        restOrganizationalEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganizationalEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationalEntity))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
        OrganizationalEntity testOrganizationalEntity = organizationalEntityList.get(organizationalEntityList.size() - 1);
        assertThat(testOrganizationalEntity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOrganizationalEntity() throws Exception {
        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();
        organizationalEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationalEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationalEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganizationalEntity() throws Exception {
        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();
        organizationalEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationalEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganizationalEntity() throws Exception {
        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();
        organizationalEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationalEntityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationalEntityWithPatch() throws Exception {
        // Initialize the database
        organizationalEntityRepository.saveAndFlush(organizationalEntity);

        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();

        // Update the organizationalEntity using partial update
        OrganizationalEntity partialUpdatedOrganizationalEntity = new OrganizationalEntity();
        partialUpdatedOrganizationalEntity.setId(organizationalEntity.getId());

        partialUpdatedOrganizationalEntity.name(UPDATED_NAME);

        restOrganizationalEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationalEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationalEntity))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
        OrganizationalEntity testOrganizationalEntity = organizationalEntityList.get(organizationalEntityList.size() - 1);
        assertThat(testOrganizationalEntity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationalEntityWithPatch() throws Exception {
        // Initialize the database
        organizationalEntityRepository.saveAndFlush(organizationalEntity);

        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();

        // Update the organizationalEntity using partial update
        OrganizationalEntity partialUpdatedOrganizationalEntity = new OrganizationalEntity();
        partialUpdatedOrganizationalEntity.setId(organizationalEntity.getId());

        partialUpdatedOrganizationalEntity.name(UPDATED_NAME);

        restOrganizationalEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationalEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationalEntity))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
        OrganizationalEntity testOrganizationalEntity = organizationalEntityList.get(organizationalEntityList.size() - 1);
        assertThat(testOrganizationalEntity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOrganizationalEntity() throws Exception {
        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();
        organizationalEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationalEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationalEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganizationalEntity() throws Exception {
        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();
        organizationalEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationalEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganizationalEntity() throws Exception {
        int databaseSizeBeforeUpdate = organizationalEntityRepository.findAll().size();
        organizationalEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationalEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationalEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationalEntity in the database
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganizationalEntity() throws Exception {
        // Initialize the database
        organizationalEntityRepository.saveAndFlush(organizationalEntity);

        int databaseSizeBeforeDelete = organizationalEntityRepository.findAll().size();

        // Delete the organizationalEntity
        restOrganizationalEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, organizationalEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationalEntity> organizationalEntityList = organizationalEntityRepository.findAll();
        assertThat(organizationalEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
