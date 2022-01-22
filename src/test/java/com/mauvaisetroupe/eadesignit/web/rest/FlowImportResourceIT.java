package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.FlowImportRepository;
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
 * Integration tests for the {@link FlowImportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlowImportResourceIT {

    private static final String DEFAULT_ID_FLOW_FROM_EXCEL = "AAAAAAAAAA";
    private static final String UPDATED_ID_FLOW_FROM_EXCEL = "BBBBBBBBBB";

    private static final String DEFAULT_FLOW_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_FLOW_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_ELEMENT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ELEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_ELEMENT = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_ELEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STEP_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_STEP_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INTEGRATION_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_INTEGRATION_PATTERN = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_SWAGGER = "AAAAAAAAAA";
    private static final String UPDATED_SWAGGER = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_URL_DOCUMENTATION = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_URL_DOCUMENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_URL_DOCUMENTATION = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_URL_DOCUMENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_DOCUMENTATION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_DOCUMENTATION_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_DOCUMENTATION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_DOCUMENTATION_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_FLOW_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_FLOW_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final ImportStatus DEFAULT_IMPORT_INTERFACE_STATUS = ImportStatus.NEW;
    private static final ImportStatus UPDATED_IMPORT_INTERFACE_STATUS = ImportStatus.EXISTING;

    private static final ImportStatus DEFAULT_IMPORT_FUNCTIONAL_FLOW_STATUS = ImportStatus.NEW;
    private static final ImportStatus UPDATED_IMPORT_FUNCTIONAL_FLOW_STATUS = ImportStatus.EXISTING;

    private static final ImportStatus DEFAULT_IMPORT_DATA_FLOW_STATUS = ImportStatus.NEW;
    private static final ImportStatus UPDATED_IMPORT_DATA_FLOW_STATUS = ImportStatus.EXISTING;

    private static final String DEFAULT_IMPORT_STATUS_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMPORT_STATUS_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flow-imports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlowImportRepository flowImportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlowImportMockMvc;

    private FlowImport flowImport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlowImport createEntity(EntityManager em) {
        FlowImport flowImport = new FlowImport()
            .idFlowFromExcel(DEFAULT_ID_FLOW_FROM_EXCEL)
            .flowAlias(DEFAULT_FLOW_ALIAS)
            .sourceElement(DEFAULT_SOURCE_ELEMENT)
            .targetElement(DEFAULT_TARGET_ELEMENT)
            .description(DEFAULT_DESCRIPTION)
            .stepDescription(DEFAULT_STEP_DESCRIPTION)
            .integrationPattern(DEFAULT_INTEGRATION_PATTERN)
            .frequency(DEFAULT_FREQUENCY)
            .format(DEFAULT_FORMAT)
            .swagger(DEFAULT_SWAGGER)
            .sourceURLDocumentation(DEFAULT_SOURCE_URL_DOCUMENTATION)
            .targetURLDocumentation(DEFAULT_TARGET_URL_DOCUMENTATION)
            .sourceDocumentationStatus(DEFAULT_SOURCE_DOCUMENTATION_STATUS)
            .targetDocumentationStatus(DEFAULT_TARGET_DOCUMENTATION_STATUS)
            .flowStatus(DEFAULT_FLOW_STATUS)
            .comment(DEFAULT_COMMENT)
            .documentName(DEFAULT_DOCUMENT_NAME)
            .importInterfaceStatus(DEFAULT_IMPORT_INTERFACE_STATUS)
            .importFunctionalFlowStatus(DEFAULT_IMPORT_FUNCTIONAL_FLOW_STATUS)
            .importDataFlowStatus(DEFAULT_IMPORT_DATA_FLOW_STATUS)
            .importStatusMessage(DEFAULT_IMPORT_STATUS_MESSAGE);
        return flowImport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlowImport createUpdatedEntity(EntityManager em) {
        FlowImport flowImport = new FlowImport()
            .idFlowFromExcel(UPDATED_ID_FLOW_FROM_EXCEL)
            .flowAlias(UPDATED_FLOW_ALIAS)
            .sourceElement(UPDATED_SOURCE_ELEMENT)
            .targetElement(UPDATED_TARGET_ELEMENT)
            .description(UPDATED_DESCRIPTION)
            .stepDescription(UPDATED_STEP_DESCRIPTION)
            .integrationPattern(UPDATED_INTEGRATION_PATTERN)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .swagger(UPDATED_SWAGGER)
            .sourceURLDocumentation(UPDATED_SOURCE_URL_DOCUMENTATION)
            .targetURLDocumentation(UPDATED_TARGET_URL_DOCUMENTATION)
            .sourceDocumentationStatus(UPDATED_SOURCE_DOCUMENTATION_STATUS)
            .targetDocumentationStatus(UPDATED_TARGET_DOCUMENTATION_STATUS)
            .flowStatus(UPDATED_FLOW_STATUS)
            .comment(UPDATED_COMMENT)
            .documentName(UPDATED_DOCUMENT_NAME)
            .importInterfaceStatus(UPDATED_IMPORT_INTERFACE_STATUS)
            .importFunctionalFlowStatus(UPDATED_IMPORT_FUNCTIONAL_FLOW_STATUS)
            .importDataFlowStatus(UPDATED_IMPORT_DATA_FLOW_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE);
        return flowImport;
    }

    @BeforeEach
    public void initTest() {
        flowImport = createEntity(em);
    }

    @Test
    @Transactional
    void createFlowImport() throws Exception {
        int databaseSizeBeforeCreate = flowImportRepository.findAll().size();
        // Create the FlowImport
        restFlowImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowImport)))
            .andExpect(status().isCreated());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeCreate + 1);
        FlowImport testFlowImport = flowImportList.get(flowImportList.size() - 1);
        assertThat(testFlowImport.getIdFlowFromExcel()).isEqualTo(DEFAULT_ID_FLOW_FROM_EXCEL);
        assertThat(testFlowImport.getFlowAlias()).isEqualTo(DEFAULT_FLOW_ALIAS);
        assertThat(testFlowImport.getSourceElement()).isEqualTo(DEFAULT_SOURCE_ELEMENT);
        assertThat(testFlowImport.getTargetElement()).isEqualTo(DEFAULT_TARGET_ELEMENT);
        assertThat(testFlowImport.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlowImport.getStepDescription()).isEqualTo(DEFAULT_STEP_DESCRIPTION);
        assertThat(testFlowImport.getIntegrationPattern()).isEqualTo(DEFAULT_INTEGRATION_PATTERN);
        assertThat(testFlowImport.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testFlowImport.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testFlowImport.getSwagger()).isEqualTo(DEFAULT_SWAGGER);
        assertThat(testFlowImport.getSourceURLDocumentation()).isEqualTo(DEFAULT_SOURCE_URL_DOCUMENTATION);
        assertThat(testFlowImport.getTargetURLDocumentation()).isEqualTo(DEFAULT_TARGET_URL_DOCUMENTATION);
        assertThat(testFlowImport.getSourceDocumentationStatus()).isEqualTo(DEFAULT_SOURCE_DOCUMENTATION_STATUS);
        assertThat(testFlowImport.getTargetDocumentationStatus()).isEqualTo(DEFAULT_TARGET_DOCUMENTATION_STATUS);
        assertThat(testFlowImport.getFlowStatus()).isEqualTo(DEFAULT_FLOW_STATUS);
        assertThat(testFlowImport.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testFlowImport.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testFlowImport.getImportInterfaceStatus()).isEqualTo(DEFAULT_IMPORT_INTERFACE_STATUS);
        assertThat(testFlowImport.getImportFunctionalFlowStatus()).isEqualTo(DEFAULT_IMPORT_FUNCTIONAL_FLOW_STATUS);
        assertThat(testFlowImport.getImportDataFlowStatus()).isEqualTo(DEFAULT_IMPORT_DATA_FLOW_STATUS);
        assertThat(testFlowImport.getImportStatusMessage()).isEqualTo(DEFAULT_IMPORT_STATUS_MESSAGE);
    }

    @Test
    @Transactional
    void createFlowImportWithExistingId() throws Exception {
        // Create the FlowImport with an existing ID
        flowImport.setId(1L);

        int databaseSizeBeforeCreate = flowImportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowImport)))
            .andExpect(status().isBadRequest());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlowImports() throws Exception {
        // Initialize the database
        flowImportRepository.saveAndFlush(flowImport);

        // Get all the flowImportList
        restFlowImportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flowImport.getId().intValue())))
            .andExpect(jsonPath("$.[*].idFlowFromExcel").value(hasItem(DEFAULT_ID_FLOW_FROM_EXCEL)))
            .andExpect(jsonPath("$.[*].flowAlias").value(hasItem(DEFAULT_FLOW_ALIAS)))
            .andExpect(jsonPath("$.[*].sourceElement").value(hasItem(DEFAULT_SOURCE_ELEMENT)))
            .andExpect(jsonPath("$.[*].targetElement").value(hasItem(DEFAULT_TARGET_ELEMENT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stepDescription").value(hasItem(DEFAULT_STEP_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].integrationPattern").value(hasItem(DEFAULT_INTEGRATION_PATTERN)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].swagger").value(hasItem(DEFAULT_SWAGGER)))
            .andExpect(jsonPath("$.[*].sourceURLDocumentation").value(hasItem(DEFAULT_SOURCE_URL_DOCUMENTATION)))
            .andExpect(jsonPath("$.[*].targetURLDocumentation").value(hasItem(DEFAULT_TARGET_URL_DOCUMENTATION)))
            .andExpect(jsonPath("$.[*].sourceDocumentationStatus").value(hasItem(DEFAULT_SOURCE_DOCUMENTATION_STATUS)))
            .andExpect(jsonPath("$.[*].targetDocumentationStatus").value(hasItem(DEFAULT_TARGET_DOCUMENTATION_STATUS)))
            .andExpect(jsonPath("$.[*].flowStatus").value(hasItem(DEFAULT_FLOW_STATUS)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].importInterfaceStatus").value(hasItem(DEFAULT_IMPORT_INTERFACE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].importFunctionalFlowStatus").value(hasItem(DEFAULT_IMPORT_FUNCTIONAL_FLOW_STATUS.toString())))
            .andExpect(jsonPath("$.[*].importDataFlowStatus").value(hasItem(DEFAULT_IMPORT_DATA_FLOW_STATUS.toString())))
            .andExpect(jsonPath("$.[*].importStatusMessage").value(hasItem(DEFAULT_IMPORT_STATUS_MESSAGE)));
    }

    @Test
    @Transactional
    void getFlowImport() throws Exception {
        // Initialize the database
        flowImportRepository.saveAndFlush(flowImport);

        // Get the flowImport
        restFlowImportMockMvc
            .perform(get(ENTITY_API_URL_ID, flowImport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flowImport.getId().intValue()))
            .andExpect(jsonPath("$.idFlowFromExcel").value(DEFAULT_ID_FLOW_FROM_EXCEL))
            .andExpect(jsonPath("$.flowAlias").value(DEFAULT_FLOW_ALIAS))
            .andExpect(jsonPath("$.sourceElement").value(DEFAULT_SOURCE_ELEMENT))
            .andExpect(jsonPath("$.targetElement").value(DEFAULT_TARGET_ELEMENT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.stepDescription").value(DEFAULT_STEP_DESCRIPTION))
            .andExpect(jsonPath("$.integrationPattern").value(DEFAULT_INTEGRATION_PATTERN))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT))
            .andExpect(jsonPath("$.swagger").value(DEFAULT_SWAGGER))
            .andExpect(jsonPath("$.sourceURLDocumentation").value(DEFAULT_SOURCE_URL_DOCUMENTATION))
            .andExpect(jsonPath("$.targetURLDocumentation").value(DEFAULT_TARGET_URL_DOCUMENTATION))
            .andExpect(jsonPath("$.sourceDocumentationStatus").value(DEFAULT_SOURCE_DOCUMENTATION_STATUS))
            .andExpect(jsonPath("$.targetDocumentationStatus").value(DEFAULT_TARGET_DOCUMENTATION_STATUS))
            .andExpect(jsonPath("$.flowStatus").value(DEFAULT_FLOW_STATUS))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.importInterfaceStatus").value(DEFAULT_IMPORT_INTERFACE_STATUS.toString()))
            .andExpect(jsonPath("$.importFunctionalFlowStatus").value(DEFAULT_IMPORT_FUNCTIONAL_FLOW_STATUS.toString()))
            .andExpect(jsonPath("$.importDataFlowStatus").value(DEFAULT_IMPORT_DATA_FLOW_STATUS.toString()))
            .andExpect(jsonPath("$.importStatusMessage").value(DEFAULT_IMPORT_STATUS_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingFlowImport() throws Exception {
        // Get the flowImport
        restFlowImportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlowImport() throws Exception {
        // Initialize the database
        flowImportRepository.saveAndFlush(flowImport);

        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();

        // Update the flowImport
        FlowImport updatedFlowImport = flowImportRepository.findById(flowImport.getId()).get();
        // Disconnect from session so that the updates on updatedFlowImport are not directly saved in db
        em.detach(updatedFlowImport);
        updatedFlowImport
            .idFlowFromExcel(UPDATED_ID_FLOW_FROM_EXCEL)
            .flowAlias(UPDATED_FLOW_ALIAS)
            .sourceElement(UPDATED_SOURCE_ELEMENT)
            .targetElement(UPDATED_TARGET_ELEMENT)
            .description(UPDATED_DESCRIPTION)
            .stepDescription(UPDATED_STEP_DESCRIPTION)
            .integrationPattern(UPDATED_INTEGRATION_PATTERN)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .swagger(UPDATED_SWAGGER)
            .sourceURLDocumentation(UPDATED_SOURCE_URL_DOCUMENTATION)
            .targetURLDocumentation(UPDATED_TARGET_URL_DOCUMENTATION)
            .sourceDocumentationStatus(UPDATED_SOURCE_DOCUMENTATION_STATUS)
            .targetDocumentationStatus(UPDATED_TARGET_DOCUMENTATION_STATUS)
            .flowStatus(UPDATED_FLOW_STATUS)
            .comment(UPDATED_COMMENT)
            .documentName(UPDATED_DOCUMENT_NAME)
            .importInterfaceStatus(UPDATED_IMPORT_INTERFACE_STATUS)
            .importFunctionalFlowStatus(UPDATED_IMPORT_FUNCTIONAL_FLOW_STATUS)
            .importDataFlowStatus(UPDATED_IMPORT_DATA_FLOW_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE);

        restFlowImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlowImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlowImport))
            )
            .andExpect(status().isOk());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
        FlowImport testFlowImport = flowImportList.get(flowImportList.size() - 1);
        assertThat(testFlowImport.getIdFlowFromExcel()).isEqualTo(UPDATED_ID_FLOW_FROM_EXCEL);
        assertThat(testFlowImport.getFlowAlias()).isEqualTo(UPDATED_FLOW_ALIAS);
        assertThat(testFlowImport.getSourceElement()).isEqualTo(UPDATED_SOURCE_ELEMENT);
        assertThat(testFlowImport.getTargetElement()).isEqualTo(UPDATED_TARGET_ELEMENT);
        assertThat(testFlowImport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlowImport.getStepDescription()).isEqualTo(UPDATED_STEP_DESCRIPTION);
        assertThat(testFlowImport.getIntegrationPattern()).isEqualTo(UPDATED_INTEGRATION_PATTERN);
        assertThat(testFlowImport.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testFlowImport.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testFlowImport.getSwagger()).isEqualTo(UPDATED_SWAGGER);
        assertThat(testFlowImport.getSourceURLDocumentation()).isEqualTo(UPDATED_SOURCE_URL_DOCUMENTATION);
        assertThat(testFlowImport.getTargetURLDocumentation()).isEqualTo(UPDATED_TARGET_URL_DOCUMENTATION);
        assertThat(testFlowImport.getSourceDocumentationStatus()).isEqualTo(UPDATED_SOURCE_DOCUMENTATION_STATUS);
        assertThat(testFlowImport.getTargetDocumentationStatus()).isEqualTo(UPDATED_TARGET_DOCUMENTATION_STATUS);
        assertThat(testFlowImport.getFlowStatus()).isEqualTo(UPDATED_FLOW_STATUS);
        assertThat(testFlowImport.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testFlowImport.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testFlowImport.getImportInterfaceStatus()).isEqualTo(UPDATED_IMPORT_INTERFACE_STATUS);
        assertThat(testFlowImport.getImportFunctionalFlowStatus()).isEqualTo(UPDATED_IMPORT_FUNCTIONAL_FLOW_STATUS);
        assertThat(testFlowImport.getImportDataFlowStatus()).isEqualTo(UPDATED_IMPORT_DATA_FLOW_STATUS);
        assertThat(testFlowImport.getImportStatusMessage()).isEqualTo(UPDATED_IMPORT_STATUS_MESSAGE);
    }

    @Test
    @Transactional
    void putNonExistingFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();
        flowImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flowImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();
        flowImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();
        flowImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowImportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowImport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlowImportWithPatch() throws Exception {
        // Initialize the database
        flowImportRepository.saveAndFlush(flowImport);

        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();

        // Update the flowImport using partial update
        FlowImport partialUpdatedFlowImport = new FlowImport();
        partialUpdatedFlowImport.setId(flowImport.getId());

        partialUpdatedFlowImport
            .idFlowFromExcel(UPDATED_ID_FLOW_FROM_EXCEL)
            .sourceElement(UPDATED_SOURCE_ELEMENT)
            .format(UPDATED_FORMAT)
            .targetURLDocumentation(UPDATED_TARGET_URL_DOCUMENTATION)
            .flowStatus(UPDATED_FLOW_STATUS)
            .documentName(UPDATED_DOCUMENT_NAME)
            .importInterfaceStatus(UPDATED_IMPORT_INTERFACE_STATUS)
            .importFunctionalFlowStatus(UPDATED_IMPORT_FUNCTIONAL_FLOW_STATUS)
            .importDataFlowStatus(UPDATED_IMPORT_DATA_FLOW_STATUS);

        restFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlowImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlowImport))
            )
            .andExpect(status().isOk());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
        FlowImport testFlowImport = flowImportList.get(flowImportList.size() - 1);
        assertThat(testFlowImport.getIdFlowFromExcel()).isEqualTo(UPDATED_ID_FLOW_FROM_EXCEL);
        assertThat(testFlowImport.getFlowAlias()).isEqualTo(DEFAULT_FLOW_ALIAS);
        assertThat(testFlowImport.getSourceElement()).isEqualTo(UPDATED_SOURCE_ELEMENT);
        assertThat(testFlowImport.getTargetElement()).isEqualTo(DEFAULT_TARGET_ELEMENT);
        assertThat(testFlowImport.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlowImport.getStepDescription()).isEqualTo(DEFAULT_STEP_DESCRIPTION);
        assertThat(testFlowImport.getIntegrationPattern()).isEqualTo(DEFAULT_INTEGRATION_PATTERN);
        assertThat(testFlowImport.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testFlowImport.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testFlowImport.getSwagger()).isEqualTo(DEFAULT_SWAGGER);
        assertThat(testFlowImport.getSourceURLDocumentation()).isEqualTo(DEFAULT_SOURCE_URL_DOCUMENTATION);
        assertThat(testFlowImport.getTargetURLDocumentation()).isEqualTo(UPDATED_TARGET_URL_DOCUMENTATION);
        assertThat(testFlowImport.getSourceDocumentationStatus()).isEqualTo(DEFAULT_SOURCE_DOCUMENTATION_STATUS);
        assertThat(testFlowImport.getTargetDocumentationStatus()).isEqualTo(DEFAULT_TARGET_DOCUMENTATION_STATUS);
        assertThat(testFlowImport.getFlowStatus()).isEqualTo(UPDATED_FLOW_STATUS);
        assertThat(testFlowImport.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testFlowImport.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testFlowImport.getImportInterfaceStatus()).isEqualTo(UPDATED_IMPORT_INTERFACE_STATUS);
        assertThat(testFlowImport.getImportFunctionalFlowStatus()).isEqualTo(UPDATED_IMPORT_FUNCTIONAL_FLOW_STATUS);
        assertThat(testFlowImport.getImportDataFlowStatus()).isEqualTo(UPDATED_IMPORT_DATA_FLOW_STATUS);
        assertThat(testFlowImport.getImportStatusMessage()).isEqualTo(DEFAULT_IMPORT_STATUS_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateFlowImportWithPatch() throws Exception {
        // Initialize the database
        flowImportRepository.saveAndFlush(flowImport);

        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();

        // Update the flowImport using partial update
        FlowImport partialUpdatedFlowImport = new FlowImport();
        partialUpdatedFlowImport.setId(flowImport.getId());

        partialUpdatedFlowImport
            .idFlowFromExcel(UPDATED_ID_FLOW_FROM_EXCEL)
            .flowAlias(UPDATED_FLOW_ALIAS)
            .sourceElement(UPDATED_SOURCE_ELEMENT)
            .targetElement(UPDATED_TARGET_ELEMENT)
            .description(UPDATED_DESCRIPTION)
            .stepDescription(UPDATED_STEP_DESCRIPTION)
            .integrationPattern(UPDATED_INTEGRATION_PATTERN)
            .frequency(UPDATED_FREQUENCY)
            .format(UPDATED_FORMAT)
            .swagger(UPDATED_SWAGGER)
            .sourceURLDocumentation(UPDATED_SOURCE_URL_DOCUMENTATION)
            .targetURLDocumentation(UPDATED_TARGET_URL_DOCUMENTATION)
            .sourceDocumentationStatus(UPDATED_SOURCE_DOCUMENTATION_STATUS)
            .targetDocumentationStatus(UPDATED_TARGET_DOCUMENTATION_STATUS)
            .flowStatus(UPDATED_FLOW_STATUS)
            .comment(UPDATED_COMMENT)
            .documentName(UPDATED_DOCUMENT_NAME)
            .importInterfaceStatus(UPDATED_IMPORT_INTERFACE_STATUS)
            .importFunctionalFlowStatus(UPDATED_IMPORT_FUNCTIONAL_FLOW_STATUS)
            .importDataFlowStatus(UPDATED_IMPORT_DATA_FLOW_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE);

        restFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlowImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlowImport))
            )
            .andExpect(status().isOk());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
        FlowImport testFlowImport = flowImportList.get(flowImportList.size() - 1);
        assertThat(testFlowImport.getIdFlowFromExcel()).isEqualTo(UPDATED_ID_FLOW_FROM_EXCEL);
        assertThat(testFlowImport.getFlowAlias()).isEqualTo(UPDATED_FLOW_ALIAS);
        assertThat(testFlowImport.getSourceElement()).isEqualTo(UPDATED_SOURCE_ELEMENT);
        assertThat(testFlowImport.getTargetElement()).isEqualTo(UPDATED_TARGET_ELEMENT);
        assertThat(testFlowImport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlowImport.getStepDescription()).isEqualTo(UPDATED_STEP_DESCRIPTION);
        assertThat(testFlowImport.getIntegrationPattern()).isEqualTo(UPDATED_INTEGRATION_PATTERN);
        assertThat(testFlowImport.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testFlowImport.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testFlowImport.getSwagger()).isEqualTo(UPDATED_SWAGGER);
        assertThat(testFlowImport.getSourceURLDocumentation()).isEqualTo(UPDATED_SOURCE_URL_DOCUMENTATION);
        assertThat(testFlowImport.getTargetURLDocumentation()).isEqualTo(UPDATED_TARGET_URL_DOCUMENTATION);
        assertThat(testFlowImport.getSourceDocumentationStatus()).isEqualTo(UPDATED_SOURCE_DOCUMENTATION_STATUS);
        assertThat(testFlowImport.getTargetDocumentationStatus()).isEqualTo(UPDATED_TARGET_DOCUMENTATION_STATUS);
        assertThat(testFlowImport.getFlowStatus()).isEqualTo(UPDATED_FLOW_STATUS);
        assertThat(testFlowImport.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testFlowImport.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testFlowImport.getImportInterfaceStatus()).isEqualTo(UPDATED_IMPORT_INTERFACE_STATUS);
        assertThat(testFlowImport.getImportFunctionalFlowStatus()).isEqualTo(UPDATED_IMPORT_FUNCTIONAL_FLOW_STATUS);
        assertThat(testFlowImport.getImportDataFlowStatus()).isEqualTo(UPDATED_IMPORT_DATA_FLOW_STATUS);
        assertThat(testFlowImport.getImportStatusMessage()).isEqualTo(UPDATED_IMPORT_STATUS_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();
        flowImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flowImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();
        flowImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlowImport() throws Exception {
        int databaseSizeBeforeUpdate = flowImportRepository.findAll().size();
        flowImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowImportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flowImport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlowImport in the database
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlowImport() throws Exception {
        // Initialize the database
        flowImportRepository.saveAndFlush(flowImport);

        int databaseSizeBeforeDelete = flowImportRepository.findAll().size();

        // Delete the flowImport
        restFlowImportMockMvc
            .perform(delete(ENTITY_API_URL_ID, flowImport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlowImport> flowImportList = flowImportRepository.findAll();
        assertThat(flowImportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
