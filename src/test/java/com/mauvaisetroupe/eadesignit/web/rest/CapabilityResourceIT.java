package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
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
 * Integration tests for the {@link CapabilityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = { "ROLE_USER", "ROLE_WRITE" })
class CapabilityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String ENTITY_API_URL = "/api/capabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CapabilityRepository capabilityRepository;

    @Mock
    private CapabilityRepository capabilityRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCapabilityMockMvc;

    private Capability capability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capability createEntity(EntityManager em) {
        Capability capability = new Capability()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .comment(DEFAULT_COMMENT)
            .level(DEFAULT_LEVEL);
        return capability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capability createUpdatedEntity(EntityManager em) {
        Capability capability = new Capability()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .comment(UPDATED_COMMENT)
            .level(UPDATED_LEVEL);
        return capability;
    }

    @BeforeEach
    public void initTest() {
        capability = createEntity(em);
    }

    @Test
    @Transactional
    void createCapability() throws Exception {
        int databaseSizeBeforeCreate = capabilityRepository.findAll().size();
        // Create the Capability
        restCapabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isCreated());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeCreate + 1);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCapability.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCapability.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testCapability.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void createCapabilityWithExistingId() throws Exception {
        // Create the Capability with an existing ID
        capability.setId(1L);

        int databaseSizeBeforeCreate = capabilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = capabilityRepository.findAll().size();
        // set the field null
        capability.setName(null);

        // Create the Capability, which fails.

        restCapabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isBadRequest());

        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCapabilities() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        // Get all the capabilityList
        restCapabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capability.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    void getCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        // Get the capability
        restCapabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, capability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capability.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    void getNonExistingCapability() throws Exception {
        // Get the capability
        restCapabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();

        // Update the capability
        Capability updatedCapability = capabilityRepository.findById(capability.getId()).get();
        // Disconnect from session so that the updates on updatedCapability are not directly saved in db
        em.detach(updatedCapability);
        updatedCapability.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).comment(UPDATED_COMMENT).level(UPDATED_LEVEL);

        restCapabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCapability.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCapability))
            )
            .andExpect(status().isOk());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCapability.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCapability.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testCapability.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void putNonExistingCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, capability.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capability)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCapabilityWithPatch() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();

        // Update the capability using partial update
        Capability partialUpdatedCapability = new Capability();
        partialUpdatedCapability.setId(capability.getId());

        partialUpdatedCapability.name(UPDATED_NAME);

        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapability))
            )
            .andExpect(status().isOk());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCapability.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCapability.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testCapability.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdateCapabilityWithPatch() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();

        // Update the capability using partial update
        Capability partialUpdatedCapability = new Capability();
        partialUpdatedCapability.setId(capability.getId());

        partialUpdatedCapability.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).comment(UPDATED_COMMENT).level(UPDATED_LEVEL);

        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapability))
            )
            .andExpect(status().isOk());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
        Capability testCapability = capabilityList.get(capabilityList.size() - 1);
        assertThat(testCapability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCapability.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCapability.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testCapability.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, capability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCapability() throws Exception {
        int databaseSizeBeforeUpdate = capabilityRepository.findAll().size();
        capability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapabilityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(capability))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Capability in the database
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = { "ROLE_HARD_DELETE" })
    void deleteCapability() throws Exception {
        // Initialize the database
        capabilityRepository.saveAndFlush(capability);

        int databaseSizeBeforeDelete = capabilityRepository.findAll().size();

        // Delete the capability
        restCapabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, capability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Capability> capabilityList = capabilityRepository.findAll();
        assertThat(capabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
