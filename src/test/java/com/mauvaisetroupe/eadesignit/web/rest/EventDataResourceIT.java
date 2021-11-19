package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.EventData;
import com.mauvaisetroupe.eadesignit.repository.EventDataRepository;
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
 * Integration tests for the {@link EventDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventDataResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION_URL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/event-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventDataRepository eventDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventDataMockMvc;

    private EventData eventData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventData createEntity(EntityManager em) {
        EventData eventData = new EventData()
            .name(DEFAULT_NAME)
            .contractURL(DEFAULT_CONTRACT_URL)
            .documentationURL(DEFAULT_DOCUMENTATION_URL)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return eventData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventData createUpdatedEntity(EntityManager em) {
        EventData eventData = new EventData()
            .name(UPDATED_NAME)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return eventData;
    }

    @BeforeEach
    public void initTest() {
        eventData = createEntity(em);
    }

    @Test
    @Transactional
    void createEventData() throws Exception {
        int databaseSizeBeforeCreate = eventDataRepository.findAll().size();
        // Create the EventData
        restEventDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventData)))
            .andExpect(status().isCreated());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeCreate + 1);
        EventData testEventData = eventDataList.get(eventDataList.size() - 1);
        assertThat(testEventData.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventData.getContractURL()).isEqualTo(DEFAULT_CONTRACT_URL);
        assertThat(testEventData.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
        assertThat(testEventData.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEventData.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createEventDataWithExistingId() throws Exception {
        // Create the EventData with an existing ID
        eventData.setId(1L);

        int databaseSizeBeforeCreate = eventDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventData)))
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventData() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        // Get all the eventDataList
        restEventDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contractURL").value(hasItem(DEFAULT_CONTRACT_URL)))
            .andExpect(jsonPath("$.[*].documentationURL").value(hasItem(DEFAULT_DOCUMENTATION_URL)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getEventData() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        // Get the eventData
        restEventDataMockMvc
            .perform(get(ENTITY_API_URL_ID, eventData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contractURL").value(DEFAULT_CONTRACT_URL))
            .andExpect(jsonPath("$.documentationURL").value(DEFAULT_DOCUMENTATION_URL))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEventData() throws Exception {
        // Get the eventData
        restEventDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEventData() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();

        // Update the eventData
        EventData updatedEventData = eventDataRepository.findById(eventData.getId()).get();
        // Disconnect from session so that the updates on updatedEventData are not directly saved in db
        em.detach(updatedEventData);
        updatedEventData
            .name(UPDATED_NAME)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restEventDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEventData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEventData))
            )
            .andExpect(status().isOk());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
        EventData testEventData = eventDataList.get(eventDataList.size() - 1);
        assertThat(testEventData.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventData.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testEventData.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testEventData.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEventData.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventDataWithPatch() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();

        // Update the eventData using partial update
        EventData partialUpdatedEventData = new EventData();
        partialUpdatedEventData.setId(eventData.getId());

        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventData))
            )
            .andExpect(status().isOk());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
        EventData testEventData = eventDataList.get(eventDataList.size() - 1);
        assertThat(testEventData.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventData.getContractURL()).isEqualTo(DEFAULT_CONTRACT_URL);
        assertThat(testEventData.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
        assertThat(testEventData.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEventData.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateEventDataWithPatch() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();

        // Update the eventData using partial update
        EventData partialUpdatedEventData = new EventData();
        partialUpdatedEventData.setId(eventData.getId());

        partialUpdatedEventData
            .name(UPDATED_NAME)
            .contractURL(UPDATED_CONTRACT_URL)
            .documentationURL(UPDATED_DOCUMENTATION_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventData))
            )
            .andExpect(status().isOk());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
        EventData testEventData = eventDataList.get(eventDataList.size() - 1);
        assertThat(testEventData.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventData.getContractURL()).isEqualTo(UPDATED_CONTRACT_URL);
        assertThat(testEventData.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
        assertThat(testEventData.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEventData.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventData() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        int databaseSizeBeforeDelete = eventDataRepository.findAll().size();

        // Delete the eventData
        restEventDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
