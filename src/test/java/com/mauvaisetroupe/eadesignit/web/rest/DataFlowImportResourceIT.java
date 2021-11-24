package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.DataFlowImportRepository;
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
 * Integration tests for the {@link DataFlowImportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataFlowImportResourceIT {

    private static final String DEFAULT_DATA_RESOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATA_RESOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_RESOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_RESOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DATA_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_DOCUMENTATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_DATA_DOCUMENTATION_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_ITEM_RESOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATA_ITEM_RESOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_ITEM_RESOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_ITEM_RESOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_ITEM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DATA_ITEM_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_ITEM_DOCUMENTATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_DATA_ITEM_DOCUMENTATION_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_URL = "BBBBBBBBBB";

    private static final ImportStatus DEFAULT_IMPORT_DATA_FLOW_STATUS = ImportStatus.NEW;
    private static final ImportStatus UPDATED_IMPORT_DATA_FLOW_STATUS = ImportStatus.EXISTING;

    private static final ImportStatus DEFAULT_IMPORT_DATA_FLOW_ITEM_STATUS = ImportStatus.NEW;
    private static final ImportStatus UPDATED_IMPORT_DATA_FLOW_ITEM_STATUS = ImportStatus.EXISTING;

    private static final String DEFAULT_IMPORT_STATUS_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMPORT_STATUS_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/data-flow-imports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataFlowImportRepository dataFlowImportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataFlowImportMockMvc;

    private DataFlowImport dataFlowImport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFlowImport createEntity(EntityManager em) {
        DataFlowImport dataFlowImport = new DataFlowImport()
            .dataResourceName(DEFAULT_DATA_RESOURCE_NAME)
            .dataResourceType(DEFAULT_DATA_RESOURCE_TYPE)
            .dataDescription(DEFAULT_DATA_DESCRIPTION)
            .dataDocumentationURL(DEFAULT_DATA_DOCUMENTATION_URL)
            .dataItemResourceName(DEFAULT_DATA_ITEM_RESOURCE_NAME)
            .dataItemResourceType(DEFAULT_DATA_ITEM_RESOURCE_TYPE)
            .dataItemDescription(DEFAULT_DATA_ITEM_DESCRIPTION)
            .dataItemDocumentationURL(DEFAULT_DATA_ITEM_DOCUMENTATION_URL)
            .frequency(DEFAULT_FREQUENCY)
            .format(DEFAULT_FORMAT)
            .contractURL(DEFAULT_CONTRACT_URL)
            .importDataFlowStatus(DEFAULT_IMPORT_DATA_FLOW_STATUS)
            .importDataFlowItemStatus(DEFAULT_IMPORT_DATA_FLOW_ITEM_STATUS)
            .importStatusMessage(DEFAULT_IMPORT_STATUS_MESSAGE);
        return dataFlowImport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFlowImport createUpdatedEntity(EntityManager em) {
        DataFlowImport dataFlowImport = new DataFlowImport()
            .dataResourceName(UPDATED_DATA_RESOURCE_NAME)
            .dataResourceType(UPDATED_DATA_RESOURCE_TYPE)
            .dataDescription(UPDATED_DATA_DESCRIPTION)
            .dataDocumentationURL(UPDATED_DATA_DOCUMENTATION_URL)
            .dataItemResourceName(UPDATED_DATA_ITEM_RESOURCE_NAME)
            .dataItemResourceType(UPDATED_DATA_ITEM_RESOURCE_TYPE)
            .dataItemDescription(UPDATED_DATA_ITEM_DESCRIPTION)
            .dataItemDocumentationURL(UPDATED_DATA_ITEM_DOCUMENTATION_URL)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .contractURL(UPDATED_CONTRACT_URL)
            .importDataFlowStatus(UPDATED_IMPORT_DATA_FLOW_STATUS)
            .importDataFlowItemStatus(UPDATED_IMPORT_DATA_FLOW_ITEM_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE);
        return dataFlowImport;
    }

    @BeforeEach
    public void initTest() {
        dataFlowImport = createEntity(em);
    }

    @Test
    @Transactional
    void createDataFlowImport() throws Exception {
        int databaseSizeBeforeCreate = dataFlowImportRepository.findAll().size();
        // Create the DataFlowImport
        restDataFlowImportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlowImport))
            )
            .andExpect(status().isCreated());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeCreate + 1);
        DataFlowImport testDataFlowImport = dataFlowImportList.get(dataFlowImportList.size() - 1);
        assertThat(testDataFlowImport.getDataResourceName()).isEqualTo(DEFAULT_DATA_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataResourceType()).isEqualTo(DEFAULT_DATA_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataDescription()).isEqualTo(DEFAULT_DATA_DESCRIPTION);
        assertThat(testDataFlowImport.getDataDocumentationURL()).isEqualTo(DEFAULT_DATA_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getDataItemResourceName()).isEqualTo(DEFAULT_DATA_ITEM_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataItemResourceType()).isEqualTo(DEFAULT_DATA_ITEM_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataItemDescription()).isEqualTo(DEFAULT_DATA_ITEM_DESCRIPTION);
        assertThat(testDataFlowImport.getDataItemDocumentationURL()).isEqualTo(DEFAULT_DATA_ITEM_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testDataFlowImport.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testDataFlowImport.getContractURL()).isEqualTo(DEFAULT_CONTRACT_URL);
        assertThat(testDataFlowImport.getImportDataFlowStatus()).isEqualTo(DEFAULT_IMPORT_DATA_FLOW_STATUS);
        assertThat(testDataFlowImport.getImportDataFlowItemStatus()).isEqualTo(DEFAULT_IMPORT_DATA_FLOW_ITEM_STATUS);
        assertThat(testDataFlowImport.getImportStatusMessage()).isEqualTo(DEFAULT_IMPORT_STATUS_MESSAGE);
    }

    @Test
    @Transactional
    void createDataFlowImportWithExistingId() throws Exception {
        // Create the DataFlowImport with an existing ID
        dataFlowImport.setId(1L);

        int databaseSizeBeforeCreate = dataFlowImportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataFlowImportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDataFlowImports() throws Exception {
        // Initialize the database
        dataFlowImportRepository.saveAndFlush(dataFlowImport);

        // Get all the dataFlowImportList
        restDataFlowImportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataFlowImport.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataResourceName").value(hasItem(DEFAULT_DATA_RESOURCE_NAME)))
            .andExpect(jsonPath("$.[*].dataResourceType").value(hasItem(DEFAULT_DATA_RESOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].dataDescription").value(hasItem(DEFAULT_DATA_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataDocumentationURL").value(hasItem(DEFAULT_DATA_DOCUMENTATION_URL)))
            .andExpect(jsonPath("$.[*].dataItemResourceName").value(hasItem(DEFAULT_DATA_ITEM_RESOURCE_NAME)))
            .andExpect(jsonPath("$.[*].dataItemResourceType").value(hasItem(DEFAULT_DATA_ITEM_RESOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].dataItemDescription").value(hasItem(DEFAULT_DATA_ITEM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataItemDocumentationURL").value(hasItem(DEFAULT_DATA_ITEM_DOCUMENTATION_URL)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].contractURL").value(hasItem(DEFAULT_CONTRACT_URL)))
            .andExpect(jsonPath("$.[*].importDataFlowStatus").value(hasItem(DEFAULT_IMPORT_DATA_FLOW_STATUS.toString())))
            .andExpect(jsonPath("$.[*].importDataFlowItemStatus").value(hasItem(DEFAULT_IMPORT_DATA_FLOW_ITEM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].importStatusMessage").value(hasItem(DEFAULT_IMPORT_STATUS_MESSAGE)));
    }

    @Test
    @Transactional
    void getDataFlowImport() throws Exception {
        // Initialize the database
        dataFlowImportRepository.saveAndFlush(dataFlowImport);

        // Get the dataFlowImport
        restDataFlowImportMockMvc
            .perform(get(ENTITY_API_URL_ID, dataFlowImport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataFlowImport.getId().intValue()))
            .andExpect(jsonPath("$.dataResourceName").value(DEFAULT_DATA_RESOURCE_NAME))
            .andExpect(jsonPath("$.dataResourceType").value(DEFAULT_DATA_RESOURCE_TYPE))
            .andExpect(jsonPath("$.dataDescription").value(DEFAULT_DATA_DESCRIPTION))
            .andExpect(jsonPath("$.dataDocumentationURL").value(DEFAULT_DATA_DOCUMENTATION_URL))
            .andExpect(jsonPath("$.dataItemResourceName").value(DEFAULT_DATA_ITEM_RESOURCE_NAME))
            .andExpect(jsonPath("$.dataItemResourceType").value(DEFAULT_DATA_ITEM_RESOURCE_TYPE))
            .andExpect(jsonPath("$.dataItemDescription").value(DEFAULT_DATA_ITEM_DESCRIPTION))
            .andExpect(jsonPath("$.dataItemDocumentationURL").value(DEFAULT_DATA_ITEM_DOCUMENTATION_URL))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT))
            .andExpect(jsonPath("$.contractURL").value(DEFAULT_CONTRACT_URL))
            .andExpect(jsonPath("$.importDataFlowStatus").value(DEFAULT_IMPORT_DATA_FLOW_STATUS.toString()))
            .andExpect(jsonPath("$.importDataFlowItemStatus").value(DEFAULT_IMPORT_DATA_FLOW_ITEM_STATUS.toString()))
            .andExpect(jsonPath("$.importStatusMessage").value(DEFAULT_IMPORT_STATUS_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingDataFlowImport() throws Exception {
        // Get the dataFlowImport
        restDataFlowImportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataFlowImport() throws Exception {
        // Initialize the database
        dataFlowImportRepository.saveAndFlush(dataFlowImport);

        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();

        // Update the dataFlowImport
        DataFlowImport updatedDataFlowImport = dataFlowImportRepository.findById(dataFlowImport.getId()).get();
        // Disconnect from session so that the updates on updatedDataFlowImport are not directly saved in db
        em.detach(updatedDataFlowImport);
        updatedDataFlowImport
            .dataResourceName(UPDATED_DATA_RESOURCE_NAME)
            .dataResourceType(UPDATED_DATA_RESOURCE_TYPE)
            .dataDescription(UPDATED_DATA_DESCRIPTION)
            .dataDocumentationURL(UPDATED_DATA_DOCUMENTATION_URL)
            .dataItemResourceName(UPDATED_DATA_ITEM_RESOURCE_NAME)
            .dataItemResourceType(UPDATED_DATA_ITEM_RESOURCE_TYPE)
            .dataItemDescription(UPDATED_DATA_ITEM_DESCRIPTION)
            .dataItemDocumentationURL(UPDATED_DATA_ITEM_DOCUMENTATION_URL)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .contractURL(UPDATED_CONTRACT_URL)
            .importDataFlowStatus(UPDATED_IMPORT_DATA_FLOW_STATUS)
            .importDataFlowItemStatus(UPDATED_IMPORT_DATA_FLOW_ITEM_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE);

        restDataFlowImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataFlowImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDataFlowImport))
            )
            .andExpect(status().isOk());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
        DataFlowImport testDataFlowImport = dataFlowImportList.get(dataFlowImportList.size() - 1);
        assertThat(testDataFlowImport.getDataResourceName()).isEqualTo(UPDATED_DATA_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataResourceType()).isEqualTo(UPDATED_DATA_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataDescription()).isEqualTo(UPDATED_DATA_DESCRIPTION);
        assertThat(testDataFlowImport.getDataDocumentationURL()).isEqualTo(UPDATED_DATA_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getDataItemResourceName()).isEqualTo(UPDATED_DATA_ITEM_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataItemResourceType()).isEqualTo(UPDATED_DATA_ITEM_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataItemDescription()).isEqualTo(UPDATED_DATA_ITEM_DESCRIPTION);
        assertThat(testDataFlowImport.getDataItemDocumentationURL()).isEqualTo(UPDATED_DATA_ITEM_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDataFlowImport.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDataFlowImport.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlowImport.getImportDataFlowStatus()).isEqualTo(UPDATED_IMPORT_DATA_FLOW_STATUS);
        assertThat(testDataFlowImport.getImportDataFlowItemStatus()).isEqualTo(UPDATED_IMPORT_DATA_FLOW_ITEM_STATUS);
        assertThat(testDataFlowImport.getImportStatusMessage()).isEqualTo(UPDATED_IMPORT_STATUS_MESSAGE);
    }

    @Test
    @Transactional
    void putNonExistingDataFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();
        dataFlowImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFlowImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataFlowImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataFlowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();
        dataFlowImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataFlowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();
        dataFlowImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowImportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlowImport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataFlowImportWithPatch() throws Exception {
        // Initialize the database
        dataFlowImportRepository.saveAndFlush(dataFlowImport);

        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();

        // Update the dataFlowImport using partial update
        DataFlowImport partialUpdatedDataFlowImport = new DataFlowImport();
        partialUpdatedDataFlowImport.setId(dataFlowImport.getId());

        partialUpdatedDataFlowImport
            .dataResourceName(UPDATED_DATA_RESOURCE_NAME)
            .dataResourceType(UPDATED_DATA_RESOURCE_TYPE)
            .dataDocumentationURL(UPDATED_DATA_DOCUMENTATION_URL)
            .dataItemResourceName(UPDATED_DATA_ITEM_RESOURCE_NAME)
            .dataItemResourceType(UPDATED_DATA_ITEM_RESOURCE_TYPE)
            .dataItemDescription(UPDATED_DATA_ITEM_DESCRIPTION)
            .dataItemDocumentationURL(UPDATED_DATA_ITEM_DOCUMENTATION_URL)
            .frequency(UPDATED_FREQUENCY)
            .contractURL(UPDATED_CONTRACT_URL)
            .importDataFlowItemStatus(UPDATED_IMPORT_DATA_FLOW_ITEM_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE);

        restDataFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataFlowImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFlowImport))
            )
            .andExpect(status().isOk());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
        DataFlowImport testDataFlowImport = dataFlowImportList.get(dataFlowImportList.size() - 1);
        assertThat(testDataFlowImport.getDataResourceName()).isEqualTo(UPDATED_DATA_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataResourceType()).isEqualTo(UPDATED_DATA_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataDescription()).isEqualTo(DEFAULT_DATA_DESCRIPTION);
        assertThat(testDataFlowImport.getDataDocumentationURL()).isEqualTo(UPDATED_DATA_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getDataItemResourceName()).isEqualTo(UPDATED_DATA_ITEM_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataItemResourceType()).isEqualTo(UPDATED_DATA_ITEM_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataItemDescription()).isEqualTo(UPDATED_DATA_ITEM_DESCRIPTION);
        assertThat(testDataFlowImport.getDataItemDocumentationURL()).isEqualTo(UPDATED_DATA_ITEM_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDataFlowImport.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testDataFlowImport.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlowImport.getImportDataFlowStatus()).isEqualTo(DEFAULT_IMPORT_DATA_FLOW_STATUS);
        assertThat(testDataFlowImport.getImportDataFlowItemStatus()).isEqualTo(UPDATED_IMPORT_DATA_FLOW_ITEM_STATUS);
        assertThat(testDataFlowImport.getImportStatusMessage()).isEqualTo(UPDATED_IMPORT_STATUS_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateDataFlowImportWithPatch() throws Exception {
        // Initialize the database
        dataFlowImportRepository.saveAndFlush(dataFlowImport);

        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();

        // Update the dataFlowImport using partial update
        DataFlowImport partialUpdatedDataFlowImport = new DataFlowImport();
        partialUpdatedDataFlowImport.setId(dataFlowImport.getId());

        partialUpdatedDataFlowImport
            .dataResourceName(UPDATED_DATA_RESOURCE_NAME)
            .dataResourceType(UPDATED_DATA_RESOURCE_TYPE)
            .dataDescription(UPDATED_DATA_DESCRIPTION)
            .dataDocumentationURL(UPDATED_DATA_DOCUMENTATION_URL)
            .dataItemResourceName(UPDATED_DATA_ITEM_RESOURCE_NAME)
            .dataItemResourceType(UPDATED_DATA_ITEM_RESOURCE_TYPE)
            .dataItemDescription(UPDATED_DATA_ITEM_DESCRIPTION)
            .dataItemDocumentationURL(UPDATED_DATA_ITEM_DOCUMENTATION_URL)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .contractURL(UPDATED_CONTRACT_URL)
            .importDataFlowStatus(UPDATED_IMPORT_DATA_FLOW_STATUS)
            .importDataFlowItemStatus(UPDATED_IMPORT_DATA_FLOW_ITEM_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE);

        restDataFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataFlowImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFlowImport))
            )
            .andExpect(status().isOk());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
        DataFlowImport testDataFlowImport = dataFlowImportList.get(dataFlowImportList.size() - 1);
        assertThat(testDataFlowImport.getDataResourceName()).isEqualTo(UPDATED_DATA_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataResourceType()).isEqualTo(UPDATED_DATA_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataDescription()).isEqualTo(UPDATED_DATA_DESCRIPTION);
        assertThat(testDataFlowImport.getDataDocumentationURL()).isEqualTo(UPDATED_DATA_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getDataItemResourceName()).isEqualTo(UPDATED_DATA_ITEM_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataItemResourceType()).isEqualTo(UPDATED_DATA_ITEM_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataItemDescription()).isEqualTo(UPDATED_DATA_ITEM_DESCRIPTION);
        assertThat(testDataFlowImport.getDataItemDocumentationURL()).isEqualTo(UPDATED_DATA_ITEM_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDataFlowImport.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDataFlowImport.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlowImport.getImportDataFlowStatus()).isEqualTo(UPDATED_IMPORT_DATA_FLOW_STATUS);
        assertThat(testDataFlowImport.getImportDataFlowItemStatus()).isEqualTo(UPDATED_IMPORT_DATA_FLOW_ITEM_STATUS);
        assertThat(testDataFlowImport.getImportStatusMessage()).isEqualTo(UPDATED_IMPORT_STATUS_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingDataFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();
        dataFlowImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataFlowImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataFlowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();
        dataFlowImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataFlowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowImportRepository.findAll().size();
        dataFlowImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dataFlowImport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataFlowImport in the database
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataFlowImport() throws Exception {
        // Initialize the database
        dataFlowImportRepository.saveAndFlush(dataFlowImport);

        int databaseSizeBeforeDelete = dataFlowImportRepository.findAll().size();

        // Delete the dataFlowImport
        restDataFlowImportMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataFlowImport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataFlowImport> dataFlowImportList = dataFlowImportRepository.findAll();
        assertThat(dataFlowImportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
