package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowStepRepository;
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
 * Integration tests for the {@link FunctionalFlowStepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FunctionalFlowStepResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STEP_ORDER = 1;
    private static final Integer UPDATED_STEP_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/functional-flow-steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FunctionalFlowStepRepository functionalFlowStepRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFunctionalFlowStepMockMvc;

    private FunctionalFlowStep functionalFlowStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FunctionalFlowStep createEntity(EntityManager em) {
        FunctionalFlowStep functionalFlowStep = new FunctionalFlowStep().description(DEFAULT_DESCRIPTION).stepOrder(DEFAULT_STEP_ORDER);
        // Add required entity
        FlowInterface flowInterface;
        if (TestUtil.findAll(em, FlowInterface.class).isEmpty()) {
            flowInterface = FlowInterfaceResourceIT.createEntity(em);
            em.persist(flowInterface);
            em.flush();
        } else {
            flowInterface = TestUtil.findAll(em, FlowInterface.class).get(0);
        }
        functionalFlowStep.setFlowInterface(flowInterface);
        // Add required entity
        FunctionalFlow functionalFlow;
        if (TestUtil.findAll(em, FunctionalFlow.class).isEmpty()) {
            functionalFlow = FunctionalFlowResourceIT.createEntity(em);
            em.persist(functionalFlow);
            em.flush();
        } else {
            functionalFlow = TestUtil.findAll(em, FunctionalFlow.class).get(0);
        }
        functionalFlowStep.setFlow(functionalFlow);
        return functionalFlowStep;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FunctionalFlowStep createUpdatedEntity(EntityManager em) {
        FunctionalFlowStep functionalFlowStep = new FunctionalFlowStep().description(UPDATED_DESCRIPTION).stepOrder(UPDATED_STEP_ORDER);
        // Add required entity
        FlowInterface flowInterface;
        if (TestUtil.findAll(em, FlowInterface.class).isEmpty()) {
            flowInterface = FlowInterfaceResourceIT.createUpdatedEntity(em);
            em.persist(flowInterface);
            em.flush();
        } else {
            flowInterface = TestUtil.findAll(em, FlowInterface.class).get(0);
        }
        functionalFlowStep.setFlowInterface(flowInterface);
        // Add required entity
        FunctionalFlow functionalFlow;
        if (TestUtil.findAll(em, FunctionalFlow.class).isEmpty()) {
            functionalFlow = FunctionalFlowResourceIT.createUpdatedEntity(em);
            em.persist(functionalFlow);
            em.flush();
        } else {
            functionalFlow = TestUtil.findAll(em, FunctionalFlow.class).get(0);
        }
        functionalFlowStep.setFlow(functionalFlow);
        return functionalFlowStep;
    }

    @BeforeEach
    public void initTest() {
        functionalFlowStep = createEntity(em);
    }

    @Test
    @Transactional
    void createFunctionalFlowStep() throws Exception {
        int databaseSizeBeforeCreate = functionalFlowStepRepository.findAll().size();
        // Create the FunctionalFlowStep
        restFunctionalFlowStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalFlowStep))
            )
            .andExpect(status().isCreated());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeCreate + 1);
        FunctionalFlowStep testFunctionalFlowStep = functionalFlowStepList.get(functionalFlowStepList.size() - 1);
        assertThat(testFunctionalFlowStep.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFunctionalFlowStep.getStepOrder()).isEqualTo(DEFAULT_STEP_ORDER);
    }

    @Test
    @Transactional
    void createFunctionalFlowStepWithExistingId() throws Exception {
        // Create the FunctionalFlowStep with an existing ID
        functionalFlowStep.setId(1L);

        int databaseSizeBeforeCreate = functionalFlowStepRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFunctionalFlowStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalFlowStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFunctionalFlowSteps() throws Exception {
        // Initialize the database
        functionalFlowStepRepository.saveAndFlush(functionalFlowStep);

        // Get all the functionalFlowStepList
        restFunctionalFlowStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(functionalFlowStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stepOrder").value(hasItem(DEFAULT_STEP_ORDER)));
    }

    @Test
    @Transactional
    void getFunctionalFlowStep() throws Exception {
        // Initialize the database
        functionalFlowStepRepository.saveAndFlush(functionalFlowStep);

        // Get the functionalFlowStep
        restFunctionalFlowStepMockMvc
            .perform(get(ENTITY_API_URL_ID, functionalFlowStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(functionalFlowStep.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.stepOrder").value(DEFAULT_STEP_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingFunctionalFlowStep() throws Exception {
        // Get the functionalFlowStep
        restFunctionalFlowStepMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFunctionalFlowStep() throws Exception {
        // Initialize the database
        functionalFlowStepRepository.saveAndFlush(functionalFlowStep);

        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();

        // Update the functionalFlowStep
        FunctionalFlowStep updatedFunctionalFlowStep = functionalFlowStepRepository.findById(functionalFlowStep.getId()).get();
        // Disconnect from session so that the updates on updatedFunctionalFlowStep are not directly saved in db
        em.detach(updatedFunctionalFlowStep);
        updatedFunctionalFlowStep.description(UPDATED_DESCRIPTION).stepOrder(UPDATED_STEP_ORDER);

        restFunctionalFlowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFunctionalFlowStep.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFunctionalFlowStep))
            )
            .andExpect(status().isOk());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
        FunctionalFlowStep testFunctionalFlowStep = functionalFlowStepList.get(functionalFlowStepList.size() - 1);
        assertThat(testFunctionalFlowStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFunctionalFlowStep.getStepOrder()).isEqualTo(UPDATED_STEP_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingFunctionalFlowStep() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();
        functionalFlowStep.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunctionalFlowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, functionalFlowStep.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlowStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFunctionalFlowStep() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();
        functionalFlowStep.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalFlowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlowStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFunctionalFlowStep() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();
        functionalFlowStep.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalFlowStepMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalFlowStep))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFunctionalFlowStepWithPatch() throws Exception {
        // Initialize the database
        functionalFlowStepRepository.saveAndFlush(functionalFlowStep);

        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();

        // Update the functionalFlowStep using partial update
        FunctionalFlowStep partialUpdatedFunctionalFlowStep = new FunctionalFlowStep();
        partialUpdatedFunctionalFlowStep.setId(functionalFlowStep.getId());

        restFunctionalFlowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunctionalFlowStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunctionalFlowStep))
            )
            .andExpect(status().isOk());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
        FunctionalFlowStep testFunctionalFlowStep = functionalFlowStepList.get(functionalFlowStepList.size() - 1);
        assertThat(testFunctionalFlowStep.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFunctionalFlowStep.getStepOrder()).isEqualTo(DEFAULT_STEP_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateFunctionalFlowStepWithPatch() throws Exception {
        // Initialize the database
        functionalFlowStepRepository.saveAndFlush(functionalFlowStep);

        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();

        // Update the functionalFlowStep using partial update
        FunctionalFlowStep partialUpdatedFunctionalFlowStep = new FunctionalFlowStep();
        partialUpdatedFunctionalFlowStep.setId(functionalFlowStep.getId());

        partialUpdatedFunctionalFlowStep.description(UPDATED_DESCRIPTION).stepOrder(UPDATED_STEP_ORDER);

        restFunctionalFlowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunctionalFlowStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunctionalFlowStep))
            )
            .andExpect(status().isOk());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
        FunctionalFlowStep testFunctionalFlowStep = functionalFlowStepList.get(functionalFlowStepList.size() - 1);
        assertThat(testFunctionalFlowStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFunctionalFlowStep.getStepOrder()).isEqualTo(UPDATED_STEP_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingFunctionalFlowStep() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();
        functionalFlowStep.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunctionalFlowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, functionalFlowStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlowStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFunctionalFlowStep() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();
        functionalFlowStep.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalFlowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlowStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFunctionalFlowStep() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowStepRepository.findAll().size();
        functionalFlowStep.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalFlowStepMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlowStep))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FunctionalFlowStep in the database
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFunctionalFlowStep() throws Exception {
        // Initialize the database
        functionalFlowStepRepository.saveAndFlush(functionalFlowStep);

        int databaseSizeBeforeDelete = functionalFlowStepRepository.findAll().size();

        // Delete the functionalFlowStep
        restFunctionalFlowStepMockMvc
            .perform(delete(ENTITY_API_URL_ID, functionalFlowStep.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FunctionalFlowStep> functionalFlowStepList = functionalFlowStepRepository.findAll();
        assertThat(functionalFlowStepList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
