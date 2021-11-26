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

    private static final String DEFAULT_DATA_ID = "AAAAAAAAAA";
    private static final String UPDATED_DATA_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_PARENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_DATA_PARENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_PARENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATA_PARENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FUNCTIONAL_FLOW_ID = "AAAAAAAAAA";
    private static final String UPDATED_FUNCTIONAL_FLOW_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FLOW_INTERFACE_ID = "AAAAAAAAAA";
    private static final String UPDATED_FLOW_INTERFACE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_RESOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATA_RESOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_RESOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_RESOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DATA_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_DATA_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_DATA_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_CONTRACT_URL = "AAAAAAAAAA";
    private static final String UPDATED_DATA_CONTRACT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_DOCUMENTATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_DATA_DOCUMENTATION_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET = "AAAAAAAAAA";
    private static final String UPDATED_TARGET = "BBBBBBBBBB";

    private static final ImportStatus DEFAULT_IMPORT_DATA_STATUS = ImportStatus.NEW;
    private static final ImportStatus UPDATED_IMPORT_DATA_STATUS = ImportStatus.EXISTING;

    private static final ImportStatus DEFAULT_IMPORT_DATA_ITEM_STATUS = ImportStatus.NEW;
    private static final ImportStatus UPDATED_IMPORT_DATA_ITEM_STATUS = ImportStatus.EXISTING;

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
            .dataId(DEFAULT_DATA_ID)
            .dataParentId(DEFAULT_DATA_PARENT_ID)
            .dataParentName(DEFAULT_DATA_PARENT_NAME)
            .functionalFlowId(DEFAULT_FUNCTIONAL_FLOW_ID)
            .flowInterfaceId(DEFAULT_FLOW_INTERFACE_ID)
            .dataType(DEFAULT_DATA_TYPE)
            .dataResourceName(DEFAULT_DATA_RESOURCE_NAME)
            .dataResourceType(DEFAULT_DATA_RESOURCE_TYPE)
            .dataDescription(DEFAULT_DATA_DESCRIPTION)
            .dataFrequency(DEFAULT_DATA_FREQUENCY)
            .dataFormat(DEFAULT_DATA_FORMAT)
            .dataContractURL(DEFAULT_DATA_CONTRACT_URL)
            .dataDocumentationURL(DEFAULT_DATA_DOCUMENTATION_URL)
            .source(DEFAULT_SOURCE)
            .target(DEFAULT_TARGET)
            .importDataStatus(DEFAULT_IMPORT_DATA_STATUS)
            .importDataItemStatus(DEFAULT_IMPORT_DATA_ITEM_STATUS)
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
            .dataId(UPDATED_DATA_ID)
            .dataParentId(UPDATED_DATA_PARENT_ID)
            .dataParentName(UPDATED_DATA_PARENT_NAME)
            .functionalFlowId(UPDATED_FUNCTIONAL_FLOW_ID)
            .flowInterfaceId(UPDATED_FLOW_INTERFACE_ID)
            .dataType(UPDATED_DATA_TYPE)
            .dataResourceName(UPDATED_DATA_RESOURCE_NAME)
            .dataResourceType(UPDATED_DATA_RESOURCE_TYPE)
            .dataDescription(UPDATED_DATA_DESCRIPTION)
            .dataFrequency(UPDATED_DATA_FREQUENCY)
            .dataFormat(UPDATED_DATA_FORMAT)
            .dataContractURL(UPDATED_DATA_CONTRACT_URL)
            .dataDocumentationURL(UPDATED_DATA_DOCUMENTATION_URL)
            .source(UPDATED_SOURCE)
            .target(UPDATED_TARGET)
            .importDataStatus(UPDATED_IMPORT_DATA_STATUS)
            .importDataItemStatus(UPDATED_IMPORT_DATA_ITEM_STATUS)
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
        assertThat(testDataFlowImport.getDataId()).isEqualTo(DEFAULT_DATA_ID);
        assertThat(testDataFlowImport.getDataParentId()).isEqualTo(DEFAULT_DATA_PARENT_ID);
        assertThat(testDataFlowImport.getDataParentName()).isEqualTo(DEFAULT_DATA_PARENT_NAME);
        assertThat(testDataFlowImport.getFunctionalFlowId()).isEqualTo(DEFAULT_FUNCTIONAL_FLOW_ID);
        assertThat(testDataFlowImport.getFlowInterfaceId()).isEqualTo(DEFAULT_FLOW_INTERFACE_ID);
        assertThat(testDataFlowImport.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testDataFlowImport.getDataResourceName()).isEqualTo(DEFAULT_DATA_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataResourceType()).isEqualTo(DEFAULT_DATA_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataDescription()).isEqualTo(DEFAULT_DATA_DESCRIPTION);
        assertThat(testDataFlowImport.getDataFrequency()).isEqualTo(DEFAULT_DATA_FREQUENCY);
        assertThat(testDataFlowImport.getDataFormat()).isEqualTo(DEFAULT_DATA_FORMAT);
        assertThat(testDataFlowImport.getDataContractURL()).isEqualTo(DEFAULT_DATA_CONTRACT_URL);
        assertThat(testDataFlowImport.getDataDocumentationURL()).isEqualTo(DEFAULT_DATA_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testDataFlowImport.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testDataFlowImport.getImportDataStatus()).isEqualTo(DEFAULT_IMPORT_DATA_STATUS);
        assertThat(testDataFlowImport.getImportDataItemStatus()).isEqualTo(DEFAULT_IMPORT_DATA_ITEM_STATUS);
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
            .andExpect(jsonPath("$.[*].dataId").value(hasItem(DEFAULT_DATA_ID)))
            .andExpect(jsonPath("$.[*].dataParentId").value(hasItem(DEFAULT_DATA_PARENT_ID)))
            .andExpect(jsonPath("$.[*].dataParentName").value(hasItem(DEFAULT_DATA_PARENT_NAME)))
            .andExpect(jsonPath("$.[*].functionalFlowId").value(hasItem(DEFAULT_FUNCTIONAL_FLOW_ID)))
            .andExpect(jsonPath("$.[*].flowInterfaceId").value(hasItem(DEFAULT_FLOW_INTERFACE_ID)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].dataResourceName").value(hasItem(DEFAULT_DATA_RESOURCE_NAME)))
            .andExpect(jsonPath("$.[*].dataResourceType").value(hasItem(DEFAULT_DATA_RESOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].dataDescription").value(hasItem(DEFAULT_DATA_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dataFrequency").value(hasItem(DEFAULT_DATA_FREQUENCY)))
            .andExpect(jsonPath("$.[*].dataFormat").value(hasItem(DEFAULT_DATA_FORMAT)))
            .andExpect(jsonPath("$.[*].dataContractURL").value(hasItem(DEFAULT_DATA_CONTRACT_URL)))
            .andExpect(jsonPath("$.[*].dataDocumentationURL").value(hasItem(DEFAULT_DATA_DOCUMENTATION_URL)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].importDataStatus").value(hasItem(DEFAULT_IMPORT_DATA_STATUS.toString())))
            .andExpect(jsonPath("$.[*].importDataItemStatus").value(hasItem(DEFAULT_IMPORT_DATA_ITEM_STATUS.toString())))
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
            .andExpect(jsonPath("$.dataId").value(DEFAULT_DATA_ID))
            .andExpect(jsonPath("$.dataParentId").value(DEFAULT_DATA_PARENT_ID))
            .andExpect(jsonPath("$.dataParentName").value(DEFAULT_DATA_PARENT_NAME))
            .andExpect(jsonPath("$.functionalFlowId").value(DEFAULT_FUNCTIONAL_FLOW_ID))
            .andExpect(jsonPath("$.flowInterfaceId").value(DEFAULT_FLOW_INTERFACE_ID))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE))
            .andExpect(jsonPath("$.dataResourceName").value(DEFAULT_DATA_RESOURCE_NAME))
            .andExpect(jsonPath("$.dataResourceType").value(DEFAULT_DATA_RESOURCE_TYPE))
            .andExpect(jsonPath("$.dataDescription").value(DEFAULT_DATA_DESCRIPTION))
            .andExpect(jsonPath("$.dataFrequency").value(DEFAULT_DATA_FREQUENCY))
            .andExpect(jsonPath("$.dataFormat").value(DEFAULT_DATA_FORMAT))
            .andExpect(jsonPath("$.dataContractURL").value(DEFAULT_DATA_CONTRACT_URL))
            .andExpect(jsonPath("$.dataDocumentationURL").value(DEFAULT_DATA_DOCUMENTATION_URL))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET))
            .andExpect(jsonPath("$.importDataStatus").value(DEFAULT_IMPORT_DATA_STATUS.toString()))
            .andExpect(jsonPath("$.importDataItemStatus").value(DEFAULT_IMPORT_DATA_ITEM_STATUS.toString()))
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
            .dataId(UPDATED_DATA_ID)
            .dataParentId(UPDATED_DATA_PARENT_ID)
            .dataParentName(UPDATED_DATA_PARENT_NAME)
            .functionalFlowId(UPDATED_FUNCTIONAL_FLOW_ID)
            .flowInterfaceId(UPDATED_FLOW_INTERFACE_ID)
            .dataType(UPDATED_DATA_TYPE)
            .dataResourceName(UPDATED_DATA_RESOURCE_NAME)
            .dataResourceType(UPDATED_DATA_RESOURCE_TYPE)
            .dataDescription(UPDATED_DATA_DESCRIPTION)
            .dataFrequency(UPDATED_DATA_FREQUENCY)
            .dataFormat(UPDATED_DATA_FORMAT)
            .dataContractURL(UPDATED_DATA_CONTRACT_URL)
            .dataDocumentationURL(UPDATED_DATA_DOCUMENTATION_URL)
            .source(UPDATED_SOURCE)
            .target(UPDATED_TARGET)
            .importDataStatus(UPDATED_IMPORT_DATA_STATUS)
            .importDataItemStatus(UPDATED_IMPORT_DATA_ITEM_STATUS)
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
        assertThat(testDataFlowImport.getDataId()).isEqualTo(UPDATED_DATA_ID);
        assertThat(testDataFlowImport.getDataParentId()).isEqualTo(UPDATED_DATA_PARENT_ID);
        assertThat(testDataFlowImport.getDataParentName()).isEqualTo(UPDATED_DATA_PARENT_NAME);
        assertThat(testDataFlowImport.getFunctionalFlowId()).isEqualTo(UPDATED_FUNCTIONAL_FLOW_ID);
        assertThat(testDataFlowImport.getFlowInterfaceId()).isEqualTo(UPDATED_FLOW_INTERFACE_ID);
        assertThat(testDataFlowImport.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testDataFlowImport.getDataResourceName()).isEqualTo(UPDATED_DATA_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataResourceType()).isEqualTo(UPDATED_DATA_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataDescription()).isEqualTo(UPDATED_DATA_DESCRIPTION);
        assertThat(testDataFlowImport.getDataFrequency()).isEqualTo(UPDATED_DATA_FREQUENCY);
        assertThat(testDataFlowImport.getDataFormat()).isEqualTo(UPDATED_DATA_FORMAT);
        assertThat(testDataFlowImport.getDataContractURL()).isEqualTo(UPDATED_DATA_CONTRACT_URL);
        assertThat(testDataFlowImport.getDataDocumentationURL()).isEqualTo(UPDATED_DATA_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testDataFlowImport.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testDataFlowImport.getImportDataStatus()).isEqualTo(UPDATED_IMPORT_DATA_STATUS);
        assertThat(testDataFlowImport.getImportDataItemStatus()).isEqualTo(UPDATED_IMPORT_DATA_ITEM_STATUS);
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
            .dataId(UPDATED_DATA_ID)
            .dataParentId(UPDATED_DATA_PARENT_ID)
            .functionalFlowId(UPDATED_FUNCTIONAL_FLOW_ID)
            .flowInterfaceId(UPDATED_FLOW_INTERFACE_ID)
            .dataType(UPDATED_DATA_TYPE)
            .dataResourceName(UPDATED_DATA_RESOURCE_NAME)
            .dataResourceType(UPDATED_DATA_RESOURCE_TYPE)
            .dataDescription(UPDATED_DATA_DESCRIPTION)
            .dataFormat(UPDATED_DATA_FORMAT)
            .dataDocumentationURL(UPDATED_DATA_DOCUMENTATION_URL)
            .source(UPDATED_SOURCE)
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
        assertThat(testDataFlowImport.getDataId()).isEqualTo(UPDATED_DATA_ID);
        assertThat(testDataFlowImport.getDataParentId()).isEqualTo(UPDATED_DATA_PARENT_ID);
        assertThat(testDataFlowImport.getDataParentName()).isEqualTo(DEFAULT_DATA_PARENT_NAME);
        assertThat(testDataFlowImport.getFunctionalFlowId()).isEqualTo(UPDATED_FUNCTIONAL_FLOW_ID);
        assertThat(testDataFlowImport.getFlowInterfaceId()).isEqualTo(UPDATED_FLOW_INTERFACE_ID);
        assertThat(testDataFlowImport.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testDataFlowImport.getDataResourceName()).isEqualTo(UPDATED_DATA_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataResourceType()).isEqualTo(UPDATED_DATA_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataDescription()).isEqualTo(UPDATED_DATA_DESCRIPTION);
        assertThat(testDataFlowImport.getDataFrequency()).isEqualTo(DEFAULT_DATA_FREQUENCY);
        assertThat(testDataFlowImport.getDataFormat()).isEqualTo(UPDATED_DATA_FORMAT);
        assertThat(testDataFlowImport.getDataContractURL()).isEqualTo(DEFAULT_DATA_CONTRACT_URL);
        assertThat(testDataFlowImport.getDataDocumentationURL()).isEqualTo(UPDATED_DATA_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testDataFlowImport.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testDataFlowImport.getImportDataStatus()).isEqualTo(DEFAULT_IMPORT_DATA_STATUS);
        assertThat(testDataFlowImport.getImportDataItemStatus()).isEqualTo(DEFAULT_IMPORT_DATA_ITEM_STATUS);
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
            .dataId(UPDATED_DATA_ID)
            .dataParentId(UPDATED_DATA_PARENT_ID)
            .dataParentName(UPDATED_DATA_PARENT_NAME)
            .functionalFlowId(UPDATED_FUNCTIONAL_FLOW_ID)
            .flowInterfaceId(UPDATED_FLOW_INTERFACE_ID)
            .dataType(UPDATED_DATA_TYPE)
            .dataResourceName(UPDATED_DATA_RESOURCE_NAME)
            .dataResourceType(UPDATED_DATA_RESOURCE_TYPE)
            .dataDescription(UPDATED_DATA_DESCRIPTION)
            .dataFrequency(UPDATED_DATA_FREQUENCY)
            .dataFormat(UPDATED_DATA_FORMAT)
            .dataContractURL(UPDATED_DATA_CONTRACT_URL)
            .dataDocumentationURL(UPDATED_DATA_DOCUMENTATION_URL)
            .source(UPDATED_SOURCE)
            .target(UPDATED_TARGET)
            .importDataStatus(UPDATED_IMPORT_DATA_STATUS)
            .importDataItemStatus(UPDATED_IMPORT_DATA_ITEM_STATUS)
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
        assertThat(testDataFlowImport.getDataId()).isEqualTo(UPDATED_DATA_ID);
        assertThat(testDataFlowImport.getDataParentId()).isEqualTo(UPDATED_DATA_PARENT_ID);
        assertThat(testDataFlowImport.getDataParentName()).isEqualTo(UPDATED_DATA_PARENT_NAME);
        assertThat(testDataFlowImport.getFunctionalFlowId()).isEqualTo(UPDATED_FUNCTIONAL_FLOW_ID);
        assertThat(testDataFlowImport.getFlowInterfaceId()).isEqualTo(UPDATED_FLOW_INTERFACE_ID);
        assertThat(testDataFlowImport.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testDataFlowImport.getDataResourceName()).isEqualTo(UPDATED_DATA_RESOURCE_NAME);
        assertThat(testDataFlowImport.getDataResourceType()).isEqualTo(UPDATED_DATA_RESOURCE_TYPE);
        assertThat(testDataFlowImport.getDataDescription()).isEqualTo(UPDATED_DATA_DESCRIPTION);
        assertThat(testDataFlowImport.getDataFrequency()).isEqualTo(UPDATED_DATA_FREQUENCY);
        assertThat(testDataFlowImport.getDataFormat()).isEqualTo(UPDATED_DATA_FORMAT);
        assertThat(testDataFlowImport.getDataContractURL()).isEqualTo(UPDATED_DATA_CONTRACT_URL);
        assertThat(testDataFlowImport.getDataDocumentationURL()).isEqualTo(UPDATED_DATA_DOCUMENTATION_URL);
        assertThat(testDataFlowImport.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testDataFlowImport.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testDataFlowImport.getImportDataStatus()).isEqualTo(UPDATED_IMPORT_DATA_STATUS);
        assertThat(testDataFlowImport.getImportDataItemStatus()).isEqualTo(UPDATED_IMPORT_DATA_ITEM_STATUS);
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
