package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import com.mauvaisetroupe.eadesignit.domain.enumeration.DataObjectType;
import com.mauvaisetroupe.eadesignit.repository.DataObjectRepository;
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
 * Integration tests for the {@link DataObjectResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DataObjectResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final DataObjectType DEFAULT_TYPE = DataObjectType.GOLDEN_SOURCE;
    private static final DataObjectType UPDATED_TYPE = DataObjectType.READ_ONLY_REPLICA;

    private static final String ENTITY_API_URL = "/api/data-objects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataObjectRepository dataObjectRepository;

    @Mock
    private DataObjectRepository dataObjectRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataObjectMockMvc;

    private DataObject dataObject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataObject createEntity(EntityManager em) {
        DataObject dataObject = new DataObject().name(DEFAULT_NAME).type(DEFAULT_TYPE);
        return dataObject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataObject createUpdatedEntity(EntityManager em) {
        DataObject dataObject = new DataObject().name(UPDATED_NAME).type(UPDATED_TYPE);
        return dataObject;
    }

    @BeforeEach
    public void initTest() {
        dataObject = createEntity(em);
    }

    @Test
    @Transactional
    void createDataObject() throws Exception {
        int databaseSizeBeforeCreate = dataObjectRepository.findAll().size();
        // Create the DataObject
        restDataObjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataObject)))
            .andExpect(status().isCreated());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeCreate + 1);
        DataObject testDataObject = dataObjectList.get(dataObjectList.size() - 1);
        assertThat(testDataObject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataObject.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createDataObjectWithExistingId() throws Exception {
        // Create the DataObject with an existing ID
        dataObject.setId(1L);

        int databaseSizeBeforeCreate = dataObjectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataObjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataObject)))
            .andExpect(status().isBadRequest());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataObjectRepository.findAll().size();
        // set the field null
        dataObject.setName(null);

        // Create the DataObject, which fails.

        restDataObjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataObject)))
            .andExpect(status().isBadRequest());

        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDataObjects() throws Exception {
        // Initialize the database
        dataObjectRepository.saveAndFlush(dataObject);

        // Get all the dataObjectList
        restDataObjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataObject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDataObjectsWithEagerRelationshipsIsEnabled() throws Exception {
        when(dataObjectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDataObjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dataObjectRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDataObjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dataObjectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDataObjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dataObjectRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDataObject() throws Exception {
        // Initialize the database
        dataObjectRepository.saveAndFlush(dataObject);

        // Get the dataObject
        restDataObjectMockMvc
            .perform(get(ENTITY_API_URL_ID, dataObject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataObject.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDataObject() throws Exception {
        // Get the dataObject
        restDataObjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDataObject() throws Exception {
        // Initialize the database
        dataObjectRepository.saveAndFlush(dataObject);

        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();

        // Update the dataObject
        DataObject updatedDataObject = dataObjectRepository.findById(dataObject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDataObject are not directly saved in db
        em.detach(updatedDataObject);
        updatedDataObject.name(UPDATED_NAME).type(UPDATED_TYPE);

        restDataObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataObject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDataObject))
            )
            .andExpect(status().isOk());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
        DataObject testDataObject = dataObjectList.get(dataObjectList.size() - 1);
        assertThat(testDataObject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataObject.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDataObject() throws Exception {
        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();
        dataObject.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataObject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataObject() throws Exception {
        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();
        dataObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataObject() throws Exception {
        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();
        dataObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataObjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataObject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataObjectWithPatch() throws Exception {
        // Initialize the database
        dataObjectRepository.saveAndFlush(dataObject);

        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();

        // Update the dataObject using partial update
        DataObject partialUpdatedDataObject = new DataObject();
        partialUpdatedDataObject.setId(dataObject.getId());

        restDataObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataObject))
            )
            .andExpect(status().isOk());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
        DataObject testDataObject = dataObjectList.get(dataObjectList.size() - 1);
        assertThat(testDataObject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataObject.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDataObjectWithPatch() throws Exception {
        // Initialize the database
        dataObjectRepository.saveAndFlush(dataObject);

        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();

        // Update the dataObject using partial update
        DataObject partialUpdatedDataObject = new DataObject();
        partialUpdatedDataObject.setId(dataObject.getId());

        partialUpdatedDataObject.name(UPDATED_NAME).type(UPDATED_TYPE);

        restDataObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataObject))
            )
            .andExpect(status().isOk());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
        DataObject testDataObject = dataObjectList.get(dataObjectList.size() - 1);
        assertThat(testDataObject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataObject.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDataObject() throws Exception {
        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();
        dataObject.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataObject() throws Exception {
        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();
        dataObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataObject() throws Exception {
        int databaseSizeBeforeUpdate = dataObjectRepository.findAll().size();
        dataObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataObjectMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dataObject))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataObject in the database
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataObject() throws Exception {
        // Initialize the database
        dataObjectRepository.saveAndFlush(dataObject);

        int databaseSizeBeforeDelete = dataObjectRepository.findAll().size();

        // Delete the dataObject
        restDataObjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataObject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataObject> dataObjectList = dataObjectRepository.findAll();
        assertThat(dataObjectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
