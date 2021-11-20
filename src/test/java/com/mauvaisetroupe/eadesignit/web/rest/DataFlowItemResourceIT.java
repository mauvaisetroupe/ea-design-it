package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.DataFlowItem;
import com.mauvaisetroupe.eadesignit.repository.DataFlowItemRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link DataFlowItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataFlowItemResourceIT {

    private static final String DEFAULT_RESOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION_URL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/data-flow-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataFlowItemRepository dataFlowItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataFlowItemMockMvc;

    private DataFlowItem dataFlowItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFlowItem createEntity(EntityManager em) {
        DataFlowItem dataFlowItem = new DataFlowItem()
            .resourceName(DEFAULT_RESOURCE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .contractURL(DEFAULT_CONTRACT_URL)
            .documentationURL(DEFAULT_DOCUMENTATION_URL)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return dataFlowItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFlowItem createUpdatedEntity(EntityManager em) {
        DataFlowItem dataFlowItem = new DataFlowItem()
            .resourceName(UPDATED_RESOURCE_NAME)
            .description(UPDATED_DESCRIPTION)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return dataFlowItem;
    }

    @BeforeEach
    public void initTest() {
        dataFlowItem = createEntity(em);
    }

    @Test
    @Transactional
    void createDataFlowItem() throws Exception {
        int databaseSizeBeforeCreate = dataFlowItemRepository.findAll().size();
        // Create the DataFlowItem
        restDataFlowItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlowItem)))
            .andExpect(status().isCreated());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeCreate + 1);
        DataFlowItem testDataFlowItem = dataFlowItemList.get(dataFlowItemList.size() - 1);
        assertThat(testDataFlowItem.getResourceName()).isEqualTo(DEFAULT_RESOURCE_NAME);
        assertThat(testDataFlowItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDataFlowItem.getContractURL()).isEqualTo(DEFAULT_CONTRACT_URL);
        assertThat(testDataFlowItem.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
        assertThat(testDataFlowItem.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDataFlowItem.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createDataFlowItemWithExistingId() throws Exception {
        // Create the DataFlowItem with an existing ID
        dataFlowItem.setId(1L);

        int databaseSizeBeforeCreate = dataFlowItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataFlowItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlowItem)))
            .andExpect(status().isBadRequest());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDataFlowItems() throws Exception {
        // Initialize the database
        dataFlowItemRepository.saveAndFlush(dataFlowItem);

        // Get all the dataFlowItemList
        restDataFlowItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataFlowItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].resourceName").value(hasItem(DEFAULT_RESOURCE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contractURL").value(hasItem(DEFAULT_CONTRACT_URL)))
            .andExpect(jsonPath("$.[*].documentationURL").value(hasItem(DEFAULT_DOCUMENTATION_URL)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getDataFlowItem() throws Exception {
        // Initialize the database
        dataFlowItemRepository.saveAndFlush(dataFlowItem);

        // Get the dataFlowItem
        restDataFlowItemMockMvc
            .perform(get(ENTITY_API_URL_ID, dataFlowItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataFlowItem.getId().intValue()))
            .andExpect(jsonPath("$.resourceName").value(DEFAULT_RESOURCE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.contractURL").value(DEFAULT_CONTRACT_URL))
            .andExpect(jsonPath("$.documentationURL").value(DEFAULT_DOCUMENTATION_URL))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDataFlowItem() throws Exception {
        // Get the dataFlowItem
        restDataFlowItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataFlowItem() throws Exception {
        // Initialize the database
        dataFlowItemRepository.saveAndFlush(dataFlowItem);

        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();

        // Update the dataFlowItem
        DataFlowItem updatedDataFlowItem = dataFlowItemRepository.findById(dataFlowItem.getId()).get();
        // Disconnect from session so that the updates on updatedDataFlowItem are not directly saved in db
        em.detach(updatedDataFlowItem);
        updatedDataFlowItem
            .resourceName(UPDATED_RESOURCE_NAME)
            .description(UPDATED_DESCRIPTION)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restDataFlowItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataFlowItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDataFlowItem))
            )
            .andExpect(status().isOk());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
        DataFlowItem testDataFlowItem = dataFlowItemList.get(dataFlowItemList.size() - 1);
        assertThat(testDataFlowItem.getResourceName()).isEqualTo(UPDATED_RESOURCE_NAME);
        assertThat(testDataFlowItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataFlowItem.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlowItem.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testDataFlowItem.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDataFlowItem.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDataFlowItem() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();
        dataFlowItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFlowItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataFlowItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataFlowItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataFlowItem() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();
        dataFlowItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataFlowItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataFlowItem() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();
        dataFlowItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFlowItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataFlowItemWithPatch() throws Exception {
        // Initialize the database
        dataFlowItemRepository.saveAndFlush(dataFlowItem);

        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();

        // Update the dataFlowItem using partial update
        DataFlowItem partialUpdatedDataFlowItem = new DataFlowItem();
        partialUpdatedDataFlowItem.setId(dataFlowItem.getId());

        partialUpdatedDataFlowItem
            .resourceName(UPDATED_RESOURCE_NAME)
            .description(UPDATED_DESCRIPTION)
            .contractURL(UPDATED_CONTRACT_URL)
            .endDate(UPDATED_END_DATE);

        restDataFlowItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataFlowItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFlowItem))
            )
            .andExpect(status().isOk());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
        DataFlowItem testDataFlowItem = dataFlowItemList.get(dataFlowItemList.size() - 1);
        assertThat(testDataFlowItem.getResourceName()).isEqualTo(UPDATED_RESOURCE_NAME);
        assertThat(testDataFlowItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataFlowItem.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlowItem.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
        assertThat(testDataFlowItem.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDataFlowItem.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDataFlowItemWithPatch() throws Exception {
        // Initialize the database
        dataFlowItemRepository.saveAndFlush(dataFlowItem);

        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();

        // Update the dataFlowItem using partial update
        DataFlowItem partialUpdatedDataFlowItem = new DataFlowItem();
        partialUpdatedDataFlowItem.setId(dataFlowItem.getId());

        partialUpdatedDataFlowItem
            .resourceName(UPDATED_RESOURCE_NAME)
            .description(UPDATED_DESCRIPTION)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restDataFlowItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataFlowItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFlowItem))
            )
            .andExpect(status().isOk());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
        DataFlowItem testDataFlowItem = dataFlowItemList.get(dataFlowItemList.size() - 1);
        assertThat(testDataFlowItem.getResourceName()).isEqualTo(UPDATED_RESOURCE_NAME);
        assertThat(testDataFlowItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataFlowItem.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testDataFlowItem.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testDataFlowItem.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDataFlowItem.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDataFlowItem() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();
        dataFlowItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFlowItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataFlowItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataFlowItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataFlowItem() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();
        dataFlowItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataFlowItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataFlowItem() throws Exception {
        int databaseSizeBeforeUpdate = dataFlowItemRepository.findAll().size();
        dataFlowItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFlowItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dataFlowItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataFlowItem in the database
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataFlowItem() throws Exception {
        // Initialize the database
        dataFlowItemRepository.saveAndFlush(dataFlowItem);

        int databaseSizeBeforeDelete = dataFlowItemRepository.findAll().size();

        // Delete the dataFlowItem
        restDataFlowItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataFlowItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataFlowItem> dataFlowItemList = dataFlowItemRepository.findAll();
        assertThat(dataFlowItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
