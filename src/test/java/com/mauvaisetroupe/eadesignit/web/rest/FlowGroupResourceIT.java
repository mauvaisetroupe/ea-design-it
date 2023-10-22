package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.repository.FlowGroupRepository;
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
 * Integration tests for the {@link FlowGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = { "ROLE_USER", "ROLE_WRITE" })
class FlowGroupResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flow-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlowGroupRepository flowGroupRepository;

    @Mock
    private FlowGroupRepository flowGroupRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlowGroupMockMvc;

    private FlowGroup flowGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlowGroup createEntity(EntityManager em) {
        FlowGroup flowGroup = new FlowGroup().title(DEFAULT_TITLE).url(DEFAULT_URL).description(DEFAULT_DESCRIPTION);
        // Add required entity
        FunctionalFlowStep functionalFlowStep;
        if (TestUtil.findAll(em, FunctionalFlowStep.class).isEmpty()) {
            functionalFlowStep = FunctionalFlowStepResourceIT.createEntity(em);
            em.persist(functionalFlowStep);
            em.flush();
        } else {
            functionalFlowStep = TestUtil.findAll(em, FunctionalFlowStep.class).get(0);
        }
        flowGroup.getSteps().add(functionalFlowStep);
        return flowGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlowGroup createUpdatedEntity(EntityManager em) {
        FlowGroup flowGroup = new FlowGroup().title(UPDATED_TITLE).url(UPDATED_URL).description(UPDATED_DESCRIPTION);
        // Add required entity
        FunctionalFlowStep functionalFlowStep;
        if (TestUtil.findAll(em, FunctionalFlowStep.class).isEmpty()) {
            functionalFlowStep = FunctionalFlowStepResourceIT.createUpdatedEntity(em);
            em.persist(functionalFlowStep);
            em.flush();
        } else {
            functionalFlowStep = TestUtil.findAll(em, FunctionalFlowStep.class).get(0);
        }
        flowGroup.getSteps().add(functionalFlowStep);
        return flowGroup;
    }

    @BeforeEach
    public void initTest() {
        flowGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createFlowGroup() throws Exception {
        int databaseSizeBeforeCreate = flowGroupRepository.findAll().size();
        // Create the FlowGroup
        restFlowGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowGroup)))
            .andExpect(status().isCreated());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeCreate + 1);
        FlowGroup testFlowGroup = flowGroupList.get(flowGroupList.size() - 1);
        assertThat(testFlowGroup.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFlowGroup.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testFlowGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFlowGroupWithExistingId() throws Exception {
        // Create the FlowGroup with an existing ID
        flowGroup.setId(1L);

        int databaseSizeBeforeCreate = flowGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowGroup)))
            .andExpect(status().isBadRequest());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlowGroups() throws Exception {
        // Initialize the database
        flowGroupRepository.saveAndFlush(flowGroup);

        // Get all the flowGroupList
        restFlowGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flowGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFlowGroup() throws Exception {
        // Initialize the database
        flowGroupRepository.saveAndFlush(flowGroup);

        // Get the flowGroup
        restFlowGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, flowGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flowGroup.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingFlowGroup() throws Exception {
        // Get the flowGroup
        restFlowGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFlowGroup() throws Exception {
        // Initialize the database
        flowGroupRepository.saveAndFlush(flowGroup);

        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();

        // Update the flowGroup
        FlowGroup updatedFlowGroup = flowGroupRepository.findById(flowGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFlowGroup are not directly saved in db
        em.detach(updatedFlowGroup);
        updatedFlowGroup.title(UPDATED_TITLE).url(UPDATED_URL).description(UPDATED_DESCRIPTION);

        restFlowGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlowGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlowGroup))
            )
            .andExpect(status().isOk());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
        FlowGroup testFlowGroup = flowGroupList.get(flowGroupList.size() - 1);
        assertThat(testFlowGroup.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFlowGroup.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFlowGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFlowGroup() throws Exception {
        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();
        flowGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flowGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlowGroup() throws Exception {
        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();
        flowGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlowGroup() throws Exception {
        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();
        flowGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlowGroupWithPatch() throws Exception {
        // Initialize the database
        flowGroupRepository.saveAndFlush(flowGroup);

        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();

        // Update the flowGroup using partial update
        FlowGroup partialUpdatedFlowGroup = new FlowGroup();
        partialUpdatedFlowGroup.setId(flowGroup.getId());

        partialUpdatedFlowGroup.url(UPDATED_URL);

        restFlowGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlowGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlowGroup))
            )
            .andExpect(status().isOk());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
        FlowGroup testFlowGroup = flowGroupList.get(flowGroupList.size() - 1);
        assertThat(testFlowGroup.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFlowGroup.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFlowGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFlowGroupWithPatch() throws Exception {
        // Initialize the database
        flowGroupRepository.saveAndFlush(flowGroup);

        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();

        // Update the flowGroup using partial update
        FlowGroup partialUpdatedFlowGroup = new FlowGroup();
        partialUpdatedFlowGroup.setId(flowGroup.getId());

        partialUpdatedFlowGroup.title(UPDATED_TITLE).url(UPDATED_URL).description(UPDATED_DESCRIPTION);

        restFlowGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlowGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlowGroup))
            )
            .andExpect(status().isOk());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
        FlowGroup testFlowGroup = flowGroupList.get(flowGroupList.size() - 1);
        assertThat(testFlowGroup.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFlowGroup.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFlowGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFlowGroup() throws Exception {
        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();
        flowGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flowGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlowGroup() throws Exception {
        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();
        flowGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlowGroup() throws Exception {
        int databaseSizeBeforeUpdate = flowGroupRepository.findAll().size();
        flowGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flowGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlowGroup in the database
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = { "ROLE_HARD_DELETE" })
    void deleteFlowGroup() throws Exception {
        // Initialize the database
        flowGroupRepository.saveAndFlush(flowGroup);

        int databaseSizeBeforeDelete = flowGroupRepository.findAll().size();

        // Delete the flowGroup
        restFlowGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, flowGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlowGroup> flowGroupList = flowGroupRepository.findAll();
        assertThat(flowGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
