package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Format;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Frequency;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
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
 * Integration tests for the {@link DataFlowResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DataFlowResourceIT {

    private static final String DEFAULT_RESOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Frequency DEFAULT_FREQUENCY = Frequency.DAILY;
    private static final Frequency UPDATED_FREQUENCY = Frequency.WEEKLY;

    private static final Format DEFAULT_FORMAT = Format.XML;
    private static final Format UPDATED_FORMAT = Format.JSON;

    private static final String DEFAULT_CONTRACT_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION_URL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/data-flows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataFlowRepository dataFlowRepository;

    @Mock
    private DataFlowRepository dataFlowRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataFlowMockMvc;

    private DataFlow dataFlow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFlow createEntity(EntityManager em) {
        DataFlow dataFlow = new DataFlow()
            .resourceName(DEFAULT_RESOURCE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .frequency(DEFAULT_FREQUENCY)
            .format(DEFAULT_FORMAT)
            .contractURL(DEFAULT_CONTRACT_URL)
            .documentationURL(DEFAULT_DOCUMENTATION_URL)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        // Add required entity
        FunctionalFlow functionalFlow;
        if (TestUtil.findAll(em, FunctionalFlow.class).isEmpty()) {
            functionalFlow = FunctionalFlowResourceIT.createEntity(em);
            em.persist(functionalFlow);
            em.flush();
        } else {
            functionalFlow = TestUtil.findAll(em, FunctionalFlow.class).get(0);
        }
        dataFlow.getFunctionalFlows().add(functionalFlow);
        // Add required entity
        FlowInterface flowInterface;
        if (TestUtil.findAll(em, FlowInterface.class).isEmpty()) {
            flowInterface = FlowInterfaceResourceIT.createEntity(em);
            em.persist(flowInterface);
            em.flush();
        } else {
            flowInterface = TestUtil.findAll(em, FlowInterface.class).get(0);
        }
        dataFlow.setFlowInterface(flowInterface);
        return dataFlow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFlow createUpdatedEntity(EntityManager em) {
        DataFlow dataFlow = new DataFlow()
            .resourceName(UPDATED_RESOURCE_NAME)
            .description(UPDATED_DESCRIPTION)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        // Add required entity
        FunctionalFlow functionalFlow;
        if (TestUtil.findAll(em, FunctionalFlow.class).isEmpty()) {
            functionalFlow = FunctionalFlowResourceIT.createUpdatedEntity(em);
            em.persist(functionalFlow);
            em.flush();
        } else {
            functionalFlow = TestUtil.findAll(em, FunctionalFlow.class).get(0);
        }
        dataFlow.getFunctionalFlows().add(functionalFlow);
        // Add required entity
        FlowInterface flowInterface;
        if (TestUtil.findAll(em, FlowInterface.class).isEmpty()) {
            flowInterface = FlowInterfaceResourceIT.createUpdatedEntity(em);
            em.persist(flowInterface);
            em.flush();
        } else {
            flowInterface = TestUtil.findAll(em, FlowInterface.class).get(0);
        }
        dataFlow.setFlowInterface(flowInterface);
        return dataFlow;
    }

    @BeforeEach
    public void initTest() {
        dataFlow = createEntity(em);
    }

    @Test
    @Transactional
    void createDataFlow() throws Exception {
        int databaseSizeBeforeCreate = dataFlowRepository.findAll().size();
        // Create the DataFlow
        restDataFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlow)))
            .andExpect(status().isCreated());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeCreate + 1);
        DataFlow testDataFlow = dataFlowList.get(dataFlowList.size() - 1);
        assertThat(testDataFlow.getResourceName()).isEqualTo(DEFAULT_RESOURCE_NAME);
        assertThat(testDataFlow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDataFlow.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testDataFlow.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testDataFlow.getContractURL()).isEqualTo(DEFAULT_CONTRACT_URL);
        assertThat(testDataFlow.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
        assertThat(testDataFlow.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDataFlow.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createDataFlowWithExistingId() throws Exception {
        // Create the DataFlow with an existing ID
        dataFlow.setId(1L);

        int databaseSizeBeforeCreate = dataFlowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlow)))
            .andExpect(status().isBadRequest());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDataFlows() throws Exception {
        // Initialize the database
        dataFlowRepository.saveAndFlush(dataFlow);

        // Get all the dataFlowList
        restDataFlowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataFlow.getId().intValue())))
            .andExpect(jsonPath("$.[*].resourceName").value(hasItem(DEFAULT_RESOURCE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].contractURL").value(hasItem(DEFAULT_CONTRACT_URL)))
            .andExpect(jsonPath("$.[*].documentationURL").value(hasItem(DEFAULT_DOCUMENTATION_URL)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDataFlowsWithEagerRelationshipsIsEnabled() throws Exception {
        when(dataFlowRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDataFlowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dataFlowRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDataFlowsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dataFlowRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDataFlowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dataFlowRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDataFlow() throws Exception {
        // Initialize the database
        dataFlowRepository.saveAndFlush(dataFlow);

        // Get the dataFlow
        restDataFlowMockMvc
            .perform(get(ENTITY_API_URL_ID, dataFlow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataFlow.getId().intValue()))
            .andExpect(jsonPath("$.resourceName").value(DEFAULT_RESOURCE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.contractURL").value(DEFAULT_CONTRACT_URL))
            .andExpect(jsonPath("$.documentationURL").value(DEFAULT_DOCUMENTATION_URL))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDataFlow() throws Exception {
        // Get the dataFlow
        restDataFlowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataFlow() throws Exception {
        // Initialize the database
        dataFlowRepository.saveAndFlush(dataFlow);

        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();

        // Update the dataFlow
        DataFlow updatedDataFlow = dataFlowRepository.findById(dataFlow.getId()).get();
        // Disconnect from session so that the updates on updatedDataFlow are not directly saved in db
        em.detach(updatedDataFlow);
        updatedDataFlow
            .resourceName(UPDATED_RESOURCE_NAME)
            .description(UPDATED_DESCRIPTION)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restDataFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataFlow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDataFlow))
            )
            .andExpect(status().isOk());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
        DataFlow testDataFlow = dataFlowList.get(dataFlowList.size() - 1);
        assertThat(testDataFlow.getResourceName()).isEqualTo(UPDATED_RESOURCE_NAME);
        assertThat(testDataFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataFlow.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDataFlow.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDataFlow.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlow.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testDataFlow.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDataFlow.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDataFlow() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();
        dataFlow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataFlow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataFlow() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();
        dataFlow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataFlow() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();
        dataFlow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataFlowWithPatch() throws Exception {
        // Initialize the database
        dataFlowRepository.saveAndFlush(dataFlow);

        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();

        // Update the dataFlow using partial update
        DataFlow partialUpdatedDataFlow = new DataFlow();
        partialUpdatedDataFlow.setId(dataFlow.getId());

        partialUpdatedDataFlow
            .description(UPDATED_DESCRIPTION)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .contractURL(UPDATED_CONTRACT_URL)
            .endDate(UPDATED_END_DATE);

        restDataFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFlow))
            )
            .andExpect(status().isOk());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
        DataFlow testDataFlow = dataFlowList.get(dataFlowList.size() - 1);
        assertThat(testDataFlow.getResourceName()).isEqualTo(DEFAULT_RESOURCE_NAME);
        assertThat(testDataFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataFlow.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDataFlow.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDataFlow.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlow.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
        assertThat(testDataFlow.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDataFlow.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDataFlowWithPatch() throws Exception {
        // Initialize the database
        dataFlowRepository.saveAndFlush(dataFlow);

        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();

        // Update the dataFlow using partial update
        DataFlow partialUpdatedDataFlow = new DataFlow();
        partialUpdatedDataFlow.setId(dataFlow.getId());

        partialUpdatedDataFlow
            .resourceName(UPDATED_RESOURCE_NAME)
            .description(UPDATED_DESCRIPTION)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restDataFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFlow))
            )
            .andExpect(status().isOk());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
        DataFlow testDataFlow = dataFlowList.get(dataFlowList.size() - 1);
        assertThat(testDataFlow.getResourceName()).isEqualTo(UPDATED_RESOURCE_NAME);
        assertThat(testDataFlow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataFlow.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDataFlow.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDataFlow.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlow.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testDataFlow.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDataFlow.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDataFlow() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();
        dataFlow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataFlow() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();
        dataFlow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataFlow))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataFlow() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowRepository.findAll().size();
        dataFlow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dataFlow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataFlow in the database
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataFlow() throws Exception {
        // Initialize the database
        dataFlowRepository.saveAndFlush(dataFlow);

        int databaseSizeBeforeDelete = dataFlowRepository.findAll().size();

        // Delete the dataFlow
        restDataFlowMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataFlow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataFlow> dataFlowList = dataFlowRepository.findAll();
        assertThat(dataFlowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
