package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ProtocolType;
import com.mauvaisetroupe.eadesignit.repository.ProtocolRepository;
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
 * Integration tests for the {@link ProtocolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = { "ROLE_USER", "ROLE_WRITE" })
class ProtocolResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ProtocolType DEFAULT_TYPE = ProtocolType.API;
    private static final ProtocolType UPDATED_TYPE = ProtocolType.ESB;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SCOPE = "AAAAAAAAAA";
    private static final String UPDATED_SCOPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/protocols";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProtocolRepository protocolRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProtocolMockMvc;

    private Protocol protocol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Protocol createEntity(EntityManager em) {
        Protocol protocol = new Protocol().name(DEFAULT_NAME).type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION).scope(DEFAULT_SCOPE);
        return protocol;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Protocol createUpdatedEntity(EntityManager em) {
        Protocol protocol = new Protocol().name(UPDATED_NAME).type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).scope(UPDATED_SCOPE);
        return protocol;
    }

    @BeforeEach
    public void initTest() {
        protocol = createEntity(em);
    }

    @Test
    @Transactional
    void createProtocol() throws Exception {
        int databaseSizeBeforeCreate = protocolRepository.findAll().size();
        // Create the Protocol
        restProtocolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(protocol)))
            .andExpect(status().isCreated());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeCreate + 1);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProtocol.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProtocol.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProtocol.getScope()).isEqualTo(DEFAULT_SCOPE);
    }

    @Test
    @Transactional
    void createProtocolWithExistingId() throws Exception {
        // Create the Protocol with an existing ID
        protocol.setId(1L);

        int databaseSizeBeforeCreate = protocolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProtocolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(protocol)))
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = protocolRepository.findAll().size();
        // set the field null
        protocol.setName(null);

        // Create the Protocol, which fails.

        restProtocolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(protocol)))
            .andExpect(status().isBadRequest());

        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = protocolRepository.findAll().size();
        // set the field null
        protocol.setType(null);

        // Create the Protocol, which fails.

        restProtocolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(protocol)))
            .andExpect(status().isBadRequest());

        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProtocols() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList
        restProtocolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protocol.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE)));
    }

    @Test
    @Transactional
    void getProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get the protocol
        restProtocolMockMvc
            .perform(get(ENTITY_API_URL_ID, protocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(protocol.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE));
    }

    @Test
    @Transactional
    void getNonExistingProtocol() throws Exception {
        // Get the protocol
        restProtocolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Update the protocol
        Protocol updatedProtocol = protocolRepository.findById(protocol.getId()).get();
        // Disconnect from session so that the updates on updatedProtocol are not directly saved in db
        em.detach(updatedProtocol);
        updatedProtocol.name(UPDATED_NAME).type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).scope(UPDATED_SCOPE);

        restProtocolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProtocol.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProtocol))
            )
            .andExpect(status().isOk());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProtocol.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProtocol.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProtocol.getScope()).isEqualTo(UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void putNonExistingProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();
        protocol.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProtocolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, protocol.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(protocol))
            )
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();
        protocol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtocolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(protocol))
            )
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();
        protocol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtocolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(protocol)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProtocolWithPatch() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Update the protocol using partial update
        Protocol partialUpdatedProtocol = new Protocol();
        partialUpdatedProtocol.setId(protocol.getId());

        partialUpdatedProtocol.name(UPDATED_NAME).type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restProtocolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProtocol.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProtocol))
            )
            .andExpect(status().isOk());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProtocol.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProtocol.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProtocol.getScope()).isEqualTo(DEFAULT_SCOPE);
    }

    @Test
    @Transactional
    void fullUpdateProtocolWithPatch() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Update the protocol using partial update
        Protocol partialUpdatedProtocol = new Protocol();
        partialUpdatedProtocol.setId(protocol.getId());

        partialUpdatedProtocol.name(UPDATED_NAME).type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).scope(UPDATED_SCOPE);

        restProtocolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProtocol.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProtocol))
            )
            .andExpect(status().isOk());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProtocol.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProtocol.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProtocol.getScope()).isEqualTo(UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void patchNonExistingProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();
        protocol.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProtocolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, protocol.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(protocol))
            )
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();
        protocol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtocolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(protocol))
            )
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();
        protocol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtocolMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(protocol)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = { "ROLE_HARD_DELETE" })
    void deleteProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        int databaseSizeBeforeDelete = protocolRepository.findAll().size();

        // Delete the protocol
        restProtocolMockMvc
            .perform(delete(ENTITY_API_URL_ID, protocol.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
