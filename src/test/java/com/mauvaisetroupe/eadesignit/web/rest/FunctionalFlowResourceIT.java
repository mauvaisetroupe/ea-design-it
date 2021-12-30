package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FunctionalFlowResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = { "ROLE_USER", "ROLE_WRITE" })
class FunctionalFlowResourceIT {

    private static final String DEFAULT_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION_URL_2 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION_URL_2 = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/functional-flows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FunctionalFlowRepository functionalFlowRepository;

    @Mock
    private FunctionalFlowRepository functionalFlowRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFunctionalFlowMockMvc;

    private FunctionalFlow functionalFlow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FunctionalFlow createEntity(EntityManager em) {
        FunctionalFlow functionalFlow = new FunctionalFlow()
            .alias(DEFAULT_ALIAS)
            .description(DEFAULT_DESCRIPTION)
            .comment(DEFAULT_COMMENT)
            .status(DEFAULT_STATUS)
            .documentationURL(DEFAULT_DOCUMENTATION_URL)
            .documentationURL2(DEFAULT_DOCUMENTATION_URL_2)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return functionalFlow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FunctionalFlow createUpdatedEntity(EntityManager em) {
        FunctionalFlow functionalFlow = new FunctionalFlow()
            .alias(UPDATED_ALIAS)
            .description(UPDATED_DESCRIPTION)
            .comment(UPDATED_COMMENT)
            .status(UPDATED_STATUS)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .documentationURL2(UPDATED_DOCUMENTATION_URL_2)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return functionalFlow;
    }

    @BeforeEach
    public void initTest() {
        functionalFlow = createEntity(em);
    }

    @Test
    @Transactional
    void createFunctionalFlow() throws Exception {
        int databaseSizeBeforeCreate = functionalFlowRepository.findAll().size();
        // Create the FunctionalFlow
        restFunctionalFlowMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalFlow))
            )
            .andExpect(status().isCreated());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeCreate + 1);
        FunctionalFlow testFunctionalFlow = functionalFlowList.get(functionalFlowList.size() - 1);
        assertThat(testFunctionalFlow.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testFunctionalFlow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFunctionalFlow.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testFunctionalFlow.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFunctionalFlow.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
        assertThat(testFunctionalFlow.getDocumentationURL2()).isEqualTo(DEFAULT_DOCUMENTATION_URL_2);
        assertThat(testFunctionalFlow.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFunctionalFlow.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createFunctionalFlowWithExistingId() throws Exception {
        // Create the FunctionalFlow with an existing ID
        functionalFlow.setId(1L);

        int databaseSizeBeforeCreate = functionalFlowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFunctionalFlowMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = functionalFlowRepository.findAll().size();
        // set the field null
        functionalFlow.setAlias(null);

        // Create the FunctionalFlow, which fails.

        restFunctionalFlowMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalFlow))
            )
            .andExpect(status().isBadRequest());

        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFunctionalFlows() throws Exception {
        // Initialize the database
        functionalFlowRepository.saveAndFlush(functionalFlow);

        // Get all the functionalFlowList
        restFunctionalFlowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(functionalFlow.getId().intValue())))
            .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].documentationURL").value(hasItem(DEFAULT_DOCUMENTATION_URL)))
            .andExpect(jsonPath("$.[*].documentationURL2").value(hasItem(DEFAULT_DOCUMENTATION_URL_2)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFunctionalFlowsWithEagerRelationshipsIsEnabled() throws Exception {
        when(functionalFlowRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFunctionalFlowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(functionalFlowRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFunctionalFlowsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(functionalFlowRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFunctionalFlowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(functionalFlowRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFunctionalFlow() throws Exception {
        // Initialize the database
        functionalFlowRepository.saveAndFlush(functionalFlow);

        // Get the functionalFlow
        restFunctionalFlowMockMvc
            .perform(get(ENTITY_API_URL_ID, functionalFlow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(functionalFlow.getId().intValue()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.documentationURL").value(DEFAULT_DOCUMENTATION_URL))
            .andExpect(jsonPath("$.documentationURL2").value(DEFAULT_DOCUMENTATION_URL_2))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFunctionalFlow() throws Exception {
        // Get the functionalFlow
        restFunctionalFlowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFunctionalFlow() throws Exception {
        // Initialize the database
        functionalFlowRepository.saveAndFlush(functionalFlow);

        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();

        // Update the functionalFlow
        FunctionalFlow updatedFunctionalFlow = functionalFlowRepository.findById(functionalFlow.getId()).get();
        // Disconnect from session so that the updates on updatedFunctionalFlow are not directly saved in db
        em.detach(updatedFunctionalFlow);
        updatedFunctionalFlow
            .alias(UPDATED_ALIAS)
            .description(UPDATED_DESCRIPTION)
            .comment(UPDATED_COMMENT)
            .status(UPDATED_STATUS)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .documentationURL2(UPDATED_DOCUMENTATION_URL_2)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restFunctionalFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFunctionalFlow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFunctionalFlow))
            )
            .andExpect(status().isOk());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
        FunctionalFlow testFunctionalFlow = functionalFlowList.get(functionalFlowList.size() - 1);
        assertThat(testFunctionalFlow.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testFunctionalFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFunctionalFlow.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testFunctionalFlow.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFunctionalFlow.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testFunctionalFlow.getDocumentationURL2()).isEqualTo(UPDATED_DOCUMENTATION_URL_2);
        assertThat(testFunctionalFlow.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFunctionalFlow.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFunctionalFlow() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();
        functionalFlow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunctionalFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, functionalFlow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFunctionalFlow() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();
        functionalFlow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFunctionalFlow() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();
        functionalFlow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalFlowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalFlow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFunctionalFlowWithPatch() throws Exception {
        // Initialize the database
        functionalFlowRepository.saveAndFlush(functionalFlow);

        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();

        // Update the functionalFlow using partial update
        FunctionalFlow partialUpdatedFunctionalFlow = new FunctionalFlow();
        partialUpdatedFunctionalFlow.setId(functionalFlow.getId());

        partialUpdatedFunctionalFlow
            .alias(UPDATED_ALIAS)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .documentationURL2(UPDATED_DOCUMENTATION_URL_2)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restFunctionalFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunctionalFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunctionalFlow))
            )
            .andExpect(status().isOk());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
        FunctionalFlow testFunctionalFlow = functionalFlowList.get(functionalFlowList.size() - 1);
        assertThat(testFunctionalFlow.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testFunctionalFlow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFunctionalFlow.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testFunctionalFlow.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFunctionalFlow.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testFunctionalFlow.getDocumentationURL2()).isEqualTo(UPDATED_DOCUMENTATION_URL_2);
        assertThat(testFunctionalFlow.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFunctionalFlow.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFunctionalFlowWithPatch() throws Exception {
        // Initialize the database
        functionalFlowRepository.saveAndFlush(functionalFlow);

        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();

        // Update the functionalFlow using partial update
        FunctionalFlow partialUpdatedFunctionalFlow = new FunctionalFlow();
        partialUpdatedFunctionalFlow.setId(functionalFlow.getId());

        partialUpdatedFunctionalFlow
            .alias(UPDATED_ALIAS)
            .description(UPDATED_DESCRIPTION)
            .comment(UPDATED_COMMENT)
            .status(UPDATED_STATUS)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .documentationURL2(UPDATED_DOCUMENTATION_URL_2)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restFunctionalFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunctionalFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunctionalFlow))
            )
            .andExpect(status().isOk());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
        FunctionalFlow testFunctionalFlow = functionalFlowList.get(functionalFlowList.size() - 1);
        assertThat(testFunctionalFlow.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testFunctionalFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFunctionalFlow.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testFunctionalFlow.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFunctionalFlow.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testFunctionalFlow.getDocumentationURL2()).isEqualTo(UPDATED_DOCUMENTATION_URL_2);
        assertThat(testFunctionalFlow.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFunctionalFlow.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFunctionalFlow() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();
        functionalFlow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunctionalFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, functionalFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFunctionalFlow() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();
        functionalFlow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionalFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFunctionalFlow() throws Exception {
        int databaseSizeBeforeUpdate = functionalFlowRepository.findAll().size();
        functionalFlow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalFlowMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(functionalFlow))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FunctionalFlow in the database
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = { "ROLE_HARD_DELETE" })
    void deleteFunctionalFlow() throws Exception {
        // Initialize the database
        functionalFlowRepository.saveAndFlush(functionalFlow);

        int databaseSizeBeforeDelete = functionalFlowRepository.findAll().size();

        // Delete the functionalFlow
        restFunctionalFlowMockMvc
            .perform(delete(ENTITY_API_URL_ID, functionalFlow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FunctionalFlow> functionalFlowList = functionalFlowRepository.findAll();
        assertThat(functionalFlowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
