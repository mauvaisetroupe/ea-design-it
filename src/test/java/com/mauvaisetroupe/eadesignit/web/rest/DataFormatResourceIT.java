package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.DataFormat;
import com.mauvaisetroupe.eadesignit.repository.DataFormatRepository;
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
 * Integration tests for the {@link DataFormatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = { "ROLE_USER", "ROLE_WRITE" })
class DataFormatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/data-formats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataFormatRepository dataFormatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataFormatMockMvc;

    private DataFormat dataFormat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFormat createEntity(EntityManager em) {
        DataFormat dataFormat = new DataFormat().name(DEFAULT_NAME);
        return dataFormat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFormat createUpdatedEntity(EntityManager em) {
        DataFormat dataFormat = new DataFormat().name(UPDATED_NAME);
        return dataFormat;
    }

    @BeforeEach
    public void initTest() {
        dataFormat = createEntity(em);
    }

    @Test
    @Transactional
    void createDataFormat() throws Exception {
        int databaseSizeBeforeCreate = dataFormatRepository.findAll().size();
        // Create the DataFormat
        restDataFormatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFormat)))
            .andExpect(status().isCreated());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeCreate + 1);
        DataFormat testDataFormat = dataFormatList.get(dataFormatList.size() - 1);
        assertThat(testDataFormat.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDataFormatWithExistingId() throws Exception {
        // Create the DataFormat with an existing ID
        dataFormat.setId(1L);

        int databaseSizeBeforeCreate = dataFormatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataFormatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFormat)))
            .andExpect(status().isBadRequest());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataFormatRepository.findAll().size();
        // set the field null
        dataFormat.setName(null);

        // Create the DataFormat, which fails.

        restDataFormatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFormat)))
            .andExpect(status().isBadRequest());

        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDataFormats() throws Exception {
        // Initialize the database
        dataFormatRepository.saveAndFlush(dataFormat);

        // Get all the dataFormatList
        restDataFormatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataFormat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDataFormat() throws Exception {
        // Initialize the database
        dataFormatRepository.saveAndFlush(dataFormat);

        // Get the dataFormat
        restDataFormatMockMvc
            .perform(get(ENTITY_API_URL_ID, dataFormat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataFormat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDataFormat() throws Exception {
        // Get the dataFormat
        restDataFormatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataFormat() throws Exception {
        // Initialize the database
        dataFormatRepository.saveAndFlush(dataFormat);

        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();

        // Update the dataFormat
        DataFormat updatedDataFormat = dataFormatRepository.findById(dataFormat.getId()).get();
        // Disconnect from session so that the updates on updatedDataFormat are not directly saved in db
        em.detach(updatedDataFormat);
        updatedDataFormat.name(UPDATED_NAME);

        restDataFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataFormat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDataFormat))
            )
            .andExpect(status().isOk());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
        DataFormat testDataFormat = dataFormatList.get(dataFormatList.size() - 1);
        assertThat(testDataFormat.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDataFormat() throws Exception {
        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();
        dataFormat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataFormat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataFormat))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataFormat() throws Exception {
        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();
        dataFormat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataFormat))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataFormat() throws Exception {
        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();
        dataFormat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFormatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFormat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataFormatWithPatch() throws Exception {
        // Initialize the database
        dataFormatRepository.saveAndFlush(dataFormat);

        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();

        // Update the dataFormat using partial update
        DataFormat partialUpdatedDataFormat = new DataFormat();
        partialUpdatedDataFormat.setId(dataFormat.getId());

        partialUpdatedDataFormat.name(UPDATED_NAME);

        restDataFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataFormat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFormat))
            )
            .andExpect(status().isOk());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
        DataFormat testDataFormat = dataFormatList.get(dataFormatList.size() - 1);
        assertThat(testDataFormat.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDataFormatWithPatch() throws Exception {
        // Initialize the database
        dataFormatRepository.saveAndFlush(dataFormat);

        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();

        // Update the dataFormat using partial update
        DataFormat partialUpdatedDataFormat = new DataFormat();
        partialUpdatedDataFormat.setId(dataFormat.getId());

        partialUpdatedDataFormat.name(UPDATED_NAME);

        restDataFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataFormat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFormat))
            )
            .andExpect(status().isOk());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
        DataFormat testDataFormat = dataFormatList.get(dataFormatList.size() - 1);
        assertThat(testDataFormat.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDataFormat() throws Exception {
        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();
        dataFormat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataFormat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataFormat))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataFormat() throws Exception {
        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();
        dataFormat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataFormat))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataFormat() throws Exception {
        int databaseSizeBeforeUpdate = dataFormatRepository.findAll().size();
        dataFormat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataFormatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dataFormat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataFormat in the database
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = { "ROLE_HARD_DELETE" })
    void deleteDataFormat() throws Exception {
        // Initialize the database
        dataFormatRepository.saveAndFlush(dataFormat);

        int databaseSizeBeforeDelete = dataFormatRepository.findAll().size();

        // Delete the dataFormat
        restDataFormatMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataFormat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataFormat> dataFormatList = dataFormatRepository.findAll();
        assertThat(dataFormatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
