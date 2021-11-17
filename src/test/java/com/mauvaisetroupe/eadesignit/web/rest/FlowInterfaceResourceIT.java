package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Protocol;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
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
 * Integration tests for the {@link FlowInterfaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlowInterfaceResourceIT {

    private static final String DEFAULT_ALIAS = "SWR-574";
    private static final String UPDATED_ALIAS = "ITI-98";

    private static final Protocol DEFAULT_PROTOCOL = Protocol.API;
    private static final Protocol UPDATED_PROTOCOL = Protocol.ESB;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flow-interfaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlowInterfaceMockMvc;

    private FlowInterface flowInterface;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlowInterface createEntity(EntityManager em) {
        FlowInterface flowInterface = new FlowInterface()
            .alias(DEFAULT_ALIAS)
            .protocol(DEFAULT_PROTOCOL)
            .status(DEFAULT_STATUS)
            .documentationURL(DEFAULT_DOCUMENTATION_URL);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        flowInterface.setSource(application);
        // Add required entity
        flowInterface.setTarget(application);
        return flowInterface;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlowInterface createUpdatedEntity(EntityManager em) {
        FlowInterface flowInterface = new FlowInterface()
            .alias(UPDATED_ALIAS)
            .protocol(UPDATED_PROTOCOL)
            .status(UPDATED_STATUS)
            .documentationURL(UPDATED_DOCUMENTATION_URL);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createUpdatedEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        flowInterface.setSource(application);
        // Add required entity
        flowInterface.setTarget(application);
        return flowInterface;
    }

    @BeforeEach
    public void initTest() {
        flowInterface = createEntity(em);
    }

    @Test
    @Transactional
    void createFlowInterface() throws Exception {
        int databaseSizeBeforeCreate = flowInterfaceRepository.findAll().size();
        // Create the FlowInterface
        restFlowInterfaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowInterface)))
            .andExpect(status().isCreated());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeCreate + 1);
        FlowInterface testFlowInterface = flowInterfaceList.get(flowInterfaceList.size() - 1);
        assertThat(testFlowInterface.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testFlowInterface.getProtocol()).isEqualTo(DEFAULT_PROTOCOL);
        assertThat(testFlowInterface.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFlowInterface.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
    }

    @Test
    @Transactional
    void createFlowInterfaceWithExistingId() throws Exception {
        // Create the FlowInterface with an existing ID
        flowInterface.setId(1L);

        int databaseSizeBeforeCreate = flowInterfaceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowInterfaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowInterface)))
            .andExpect(status().isBadRequest());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlowInterfaces() throws Exception {
        // Initialize the database
        flowInterfaceRepository.saveAndFlush(flowInterface);

        // Get all the flowInterfaceList
        restFlowInterfaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flowInterface.getId().intValue())))
            .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS)))
            .andExpect(jsonPath("$.[*].protocol").value(hasItem(DEFAULT_PROTOCOL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].documentationURL").value(hasItem(DEFAULT_DOCUMENTATION_URL)));
    }

    @Test
    @Transactional
    void getFlowInterface() throws Exception {
        // Initialize the database
        flowInterfaceRepository.saveAndFlush(flowInterface);

        // Get the flowInterface
        restFlowInterfaceMockMvc
            .perform(get(ENTITY_API_URL_ID, flowInterface.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flowInterface.getId().intValue()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS))
            .andExpect(jsonPath("$.protocol").value(DEFAULT_PROTOCOL.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.documentationURL").value(DEFAULT_DOCUMENTATION_URL));
    }

    @Test
    @Transactional
    void getNonExistingFlowInterface() throws Exception {
        // Get the flowInterface
        restFlowInterfaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlowInterface() throws Exception {
        // Initialize the database
        flowInterfaceRepository.saveAndFlush(flowInterface);

        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();

        // Update the flowInterface
        FlowInterface updatedFlowInterface = flowInterfaceRepository.findById(flowInterface.getId()).get();
        // Disconnect from session so that the updates on updatedFlowInterface are not directly saved in db
        em.detach(updatedFlowInterface);
        updatedFlowInterface
            .alias(UPDATED_ALIAS)
            .protocol(UPDATED_PROTOCOL)
            .status(UPDATED_STATUS)
            .documentationURL(UPDATED_DOCUMENTATION_URL);

        restFlowInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlowInterface.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlowInterface))
            )
            .andExpect(status().isOk());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
        FlowInterface testFlowInterface = flowInterfaceList.get(flowInterfaceList.size() - 1);
        assertThat(testFlowInterface.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testFlowInterface.getProtocol()).isEqualTo(UPDATED_PROTOCOL);
        assertThat(testFlowInterface.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFlowInterface.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
    }

    @Test
    @Transactional
    void putNonExistingFlowInterface() throws Exception {
        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();
        flowInterface.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flowInterface.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowInterface))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlowInterface() throws Exception {
        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();
        flowInterface.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowInterfaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowInterface))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlowInterface() throws Exception {
        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();
        flowInterface.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowInterfaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowInterface)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlowInterfaceWithPatch() throws Exception {
        // Initialize the database
        flowInterfaceRepository.saveAndFlush(flowInterface);

        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();

        // Update the flowInterface using partial update
        FlowInterface partialUpdatedFlowInterface = new FlowInterface();
        partialUpdatedFlowInterface.setId(flowInterface.getId());

        partialUpdatedFlowInterface.protocol(UPDATED_PROTOCOL).status(UPDATED_STATUS);

        restFlowInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlowInterface.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlowInterface))
            )
            .andExpect(status().isOk());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
        FlowInterface testFlowInterface = flowInterfaceList.get(flowInterfaceList.size() - 1);
        assertThat(testFlowInterface.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testFlowInterface.getProtocol()).isEqualTo(UPDATED_PROTOCOL);
        assertThat(testFlowInterface.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFlowInterface.getDocumentationURL()).isEqualTo(DEFAULT_DOCUMENTATION_URL);
    }

    @Test
    @Transactional
    void fullUpdateFlowInterfaceWithPatch() throws Exception {
        // Initialize the database
        flowInterfaceRepository.saveAndFlush(flowInterface);

        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();

        // Update the flowInterface using partial update
        FlowInterface partialUpdatedFlowInterface = new FlowInterface();
        partialUpdatedFlowInterface.setId(flowInterface.getId());

        partialUpdatedFlowInterface
            .alias(UPDATED_ALIAS)
            .protocol(UPDATED_PROTOCOL)
            .status(UPDATED_STATUS)
            .documentationURL(UPDATED_DOCUMENTATION_URL);

        restFlowInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlowInterface.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlowInterface))
            )
            .andExpect(status().isOk());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
        FlowInterface testFlowInterface = flowInterfaceList.get(flowInterfaceList.size() - 1);
        assertThat(testFlowInterface.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testFlowInterface.getProtocol()).isEqualTo(UPDATED_PROTOCOL);
        assertThat(testFlowInterface.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFlowInterface.getDocumentationURL()).isEqualTo(UPDATED_DOCUMENTATION_URL);
    }

    @Test
    @Transactional
    void patchNonExistingFlowInterface() throws Exception {
        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();
        flowInterface.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flowInterface.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowInterface))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlowInterface() throws Exception {
        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();
        flowInterface.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowInterface))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlowInterface() throws Exception {
        int databaseSizeBeforeUpdate = flowInterfaceRepository.findAll().size();
        flowInterface.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowInterfaceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flowInterface))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlowInterface in the database
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlowInterface() throws Exception {
        // Initialize the database
        flowInterfaceRepository.saveAndFlush(flowInterface);

        int databaseSizeBeforeDelete = flowInterfaceRepository.findAll().size();

        // Delete the flowInterface
        restFlowInterfaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, flowInterface.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlowInterface> flowInterfaceList = flowInterfaceRepository.findAll();
        assertThat(flowInterfaceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
