package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.BusinessObject;
import com.mauvaisetroupe.eadesignit.repository.BusinessObjectRepository;
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
 * Integration tests for the {@link BusinessObjectResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BusinessObjectResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IMPLEMENTABLE = false;
    private static final Boolean UPDATED_IMPLEMENTABLE = true;

    private static final String ENTITY_API_URL = "/api/business-objects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessObjectRepository businessObjectRepository;

    @Mock
    private BusinessObjectRepository businessObjectRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessObjectMockMvc;

    private BusinessObject businessObject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessObject createEntity(EntityManager em) {
        BusinessObject businessObject = new BusinessObject().name(DEFAULT_NAME).implementable(DEFAULT_IMPLEMENTABLE);
        return businessObject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessObject createUpdatedEntity(EntityManager em) {
        BusinessObject businessObject = new BusinessObject().name(UPDATED_NAME).implementable(UPDATED_IMPLEMENTABLE);
        return businessObject;
    }

    @BeforeEach
    public void initTest() {
        businessObject = createEntity(em);
    }

    @Test
    @Transactional
    void createBusinessObject() throws Exception {
        int databaseSizeBeforeCreate = businessObjectRepository.findAll().size();
        // Create the BusinessObject
        restBusinessObjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessObject))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessObject testBusinessObject = businessObjectList.get(businessObjectList.size() - 1);
        assertThat(testBusinessObject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusinessObject.getImplementable()).isEqualTo(DEFAULT_IMPLEMENTABLE);
    }

    @Test
    @Transactional
    void createBusinessObjectWithExistingId() throws Exception {
        // Create the BusinessObject with an existing ID
        businessObject.setId(1L);

        int databaseSizeBeforeCreate = businessObjectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessObjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessObjectRepository.findAll().size();
        // set the field null
        businessObject.setName(null);

        // Create the BusinessObject, which fails.

        restBusinessObjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessObject))
            )
            .andExpect(status().isBadRequest());

        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessObjects() throws Exception {
        // Initialize the database
        businessObjectRepository.saveAndFlush(businessObject);

        // Get all the businessObjectList
        restBusinessObjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessObject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].implementable").value(hasItem(DEFAULT_IMPLEMENTABLE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBusinessObjectsWithEagerRelationshipsIsEnabled() throws Exception {
        when(businessObjectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBusinessObjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(businessObjectRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBusinessObjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(businessObjectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBusinessObjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(businessObjectRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBusinessObject() throws Exception {
        // Initialize the database
        businessObjectRepository.saveAndFlush(businessObject);

        // Get the businessObject
        restBusinessObjectMockMvc
            .perform(get(ENTITY_API_URL_ID, businessObject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessObject.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.implementable").value(DEFAULT_IMPLEMENTABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingBusinessObject() throws Exception {
        // Get the businessObject
        restBusinessObjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBusinessObject() throws Exception {
        // Initialize the database
        businessObjectRepository.saveAndFlush(businessObject);

        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();

        // Update the businessObject
        BusinessObject updatedBusinessObject = businessObjectRepository.findById(businessObject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBusinessObject are not directly saved in db
        em.detach(updatedBusinessObject);
        updatedBusinessObject.name(UPDATED_NAME).implementable(UPDATED_IMPLEMENTABLE);

        restBusinessObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBusinessObject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBusinessObject))
            )
            .andExpect(status().isOk());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
        BusinessObject testBusinessObject = businessObjectList.get(businessObjectList.size() - 1);
        assertThat(testBusinessObject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessObject.getImplementable()).isEqualTo(UPDATED_IMPLEMENTABLE);
    }

    @Test
    @Transactional
    void putNonExistingBusinessObject() throws Exception {
        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();
        businessObject.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessObject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessObject() throws Exception {
        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();
        businessObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessObject() throws Exception {
        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();
        businessObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessObjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessObject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessObjectWithPatch() throws Exception {
        // Initialize the database
        businessObjectRepository.saveAndFlush(businessObject);

        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();

        // Update the businessObject using partial update
        BusinessObject partialUpdatedBusinessObject = new BusinessObject();
        partialUpdatedBusinessObject.setId(businessObject.getId());

        partialUpdatedBusinessObject.name(UPDATED_NAME).implementable(UPDATED_IMPLEMENTABLE);

        restBusinessObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessObject))
            )
            .andExpect(status().isOk());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
        BusinessObject testBusinessObject = businessObjectList.get(businessObjectList.size() - 1);
        assertThat(testBusinessObject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessObject.getImplementable()).isEqualTo(UPDATED_IMPLEMENTABLE);
    }

    @Test
    @Transactional
    void fullUpdateBusinessObjectWithPatch() throws Exception {
        // Initialize the database
        businessObjectRepository.saveAndFlush(businessObject);

        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();

        // Update the businessObject using partial update
        BusinessObject partialUpdatedBusinessObject = new BusinessObject();
        partialUpdatedBusinessObject.setId(businessObject.getId());

        partialUpdatedBusinessObject.name(UPDATED_NAME).implementable(UPDATED_IMPLEMENTABLE);

        restBusinessObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessObject))
            )
            .andExpect(status().isOk());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
        BusinessObject testBusinessObject = businessObjectList.get(businessObjectList.size() - 1);
        assertThat(testBusinessObject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessObject.getImplementable()).isEqualTo(UPDATED_IMPLEMENTABLE);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessObject() throws Exception {
        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();
        businessObject.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessObject() throws Exception {
        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();
        businessObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessObject() throws Exception {
        int databaseSizeBeforeUpdate = businessObjectRepository.findAll().size();
        businessObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessObjectMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(businessObject))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessObject in the database
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessObject() throws Exception {
        // Initialize the database
        businessObjectRepository.saveAndFlush(businessObject);

        int databaseSizeBeforeDelete = businessObjectRepository.findAll().size();

        // Delete the businessObject
        restBusinessObjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessObject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessObject> businessObjectList = businessObjectRepository.findAll();
        assertThat(businessObjectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
