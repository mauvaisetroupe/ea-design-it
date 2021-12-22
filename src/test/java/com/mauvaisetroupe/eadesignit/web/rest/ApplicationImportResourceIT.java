package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.ApplicationImport;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.ApplicationImportRepository;
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
 * Integration tests for the {@link ApplicationImportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationImportResourceIT {

    private static final String DEFAULT_IMPORT_ID = "AAAAAAAAAA";
    private static final String UPDATED_IMPORT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXCEL_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXCEL_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ID_FROM_EXCEL = "AAAAAAAAAA";
    private static final String UPDATED_ID_FROM_EXCEL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SOFTWARE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SOFTWARE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_1 = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_1 = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_2 = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_3 = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_3 = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_TECHNOLOGY = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ImportStatus DEFAULT_IMPORT_STATUS = ImportStatus.NEW;
    private static final ImportStatus UPDATED_IMPORT_STATUS = ImportStatus.EXISTING;

    private static final String DEFAULT_IMPORT_STATUS_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMPORT_STATUS_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_EXISTING_APPLICATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXISTING_APPLICATION_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-imports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationImportRepository applicationImportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationImportMockMvc;

    private ApplicationImport applicationImport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationImport createEntity(EntityManager em) {
        ApplicationImport applicationImport = new ApplicationImport()
            .importId(DEFAULT_IMPORT_ID)
            .excelFileName(DEFAULT_EXCEL_FILE_NAME)
            .idFromExcel(DEFAULT_ID_FROM_EXCEL)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .softwareType(DEFAULT_SOFTWARE_TYPE)
            .category1(DEFAULT_CATEGORY_1)
            .category2(DEFAULT_CATEGORY_2)
            .category3(DEFAULT_CATEGORY_3)
            .technology(DEFAULT_TECHNOLOGY)
            .documentation(DEFAULT_DOCUMENTATION)
            .comment(DEFAULT_COMMENT)
            .importStatus(DEFAULT_IMPORT_STATUS)
            .importStatusMessage(DEFAULT_IMPORT_STATUS_MESSAGE)
            .existingApplicationID(DEFAULT_EXISTING_APPLICATION_ID);
        return applicationImport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationImport createUpdatedEntity(EntityManager em) {
        ApplicationImport applicationImport = new ApplicationImport()
            .importId(UPDATED_IMPORT_ID)
            .excelFileName(UPDATED_EXCEL_FILE_NAME)
            .idFromExcel(UPDATED_ID_FROM_EXCEL)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .softwareType(UPDATED_SOFTWARE_TYPE)
            .category1(UPDATED_CATEGORY_1)
            .category2(UPDATED_CATEGORY_2)
            .category3(UPDATED_CATEGORY_3)
            .technology(UPDATED_TECHNOLOGY)
            .documentation(UPDATED_DOCUMENTATION)
            .comment(UPDATED_COMMENT)
            .importStatus(UPDATED_IMPORT_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE)
            .existingApplicationID(UPDATED_EXISTING_APPLICATION_ID);
        return applicationImport;
    }

    @BeforeEach
    public void initTest() {
        applicationImport = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationImport() throws Exception {
        int databaseSizeBeforeCreate = applicationImportRepository.findAll().size();
        // Create the ApplicationImport
        restApplicationImportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationImport))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationImport testApplicationImport = applicationImportList.get(applicationImportList.size() - 1);
        assertThat(testApplicationImport.getImportId()).isEqualTo(DEFAULT_IMPORT_ID);
        assertThat(testApplicationImport.getExcelFileName()).isEqualTo(DEFAULT_EXCEL_FILE_NAME);
        assertThat(testApplicationImport.getIdFromExcel()).isEqualTo(DEFAULT_ID_FROM_EXCEL);
        assertThat(testApplicationImport.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicationImport.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplicationImport.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApplicationImport.getSoftwareType()).isEqualTo(DEFAULT_SOFTWARE_TYPE);
        assertThat(testApplicationImport.getCategory1()).isEqualTo(DEFAULT_CATEGORY_1);
        assertThat(testApplicationImport.getCategory2()).isEqualTo(DEFAULT_CATEGORY_2);
        assertThat(testApplicationImport.getCategory3()).isEqualTo(DEFAULT_CATEGORY_3);
        assertThat(testApplicationImport.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testApplicationImport.getDocumentation()).isEqualTo(DEFAULT_DOCUMENTATION);
        assertThat(testApplicationImport.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testApplicationImport.getImportStatus()).isEqualTo(DEFAULT_IMPORT_STATUS);
        assertThat(testApplicationImport.getImportStatusMessage()).isEqualTo(DEFAULT_IMPORT_STATUS_MESSAGE);
        assertThat(testApplicationImport.getExistingApplicationID()).isEqualTo(DEFAULT_EXISTING_APPLICATION_ID);
    }

    @Test
    @Transactional
    void createApplicationImportWithExistingId() throws Exception {
        // Create the ApplicationImport with an existing ID
        applicationImport.setId(1L);

        int databaseSizeBeforeCreate = applicationImportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationImportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApplicationImports() throws Exception {
        // Initialize the database
        applicationImportRepository.saveAndFlush(applicationImport);

        // Get all the applicationImportList
        restApplicationImportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationImport.getId().intValue())))
            .andExpect(jsonPath("$.[*].importId").value(hasItem(DEFAULT_IMPORT_ID)))
            .andExpect(jsonPath("$.[*].excelFileName").value(hasItem(DEFAULT_EXCEL_FILE_NAME)))
            .andExpect(jsonPath("$.[*].idFromExcel").value(hasItem(DEFAULT_ID_FROM_EXCEL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].softwareType").value(hasItem(DEFAULT_SOFTWARE_TYPE)))
            .andExpect(jsonPath("$.[*].category1").value(hasItem(DEFAULT_CATEGORY_1)))
            .andExpect(jsonPath("$.[*].category2").value(hasItem(DEFAULT_CATEGORY_2)))
            .andExpect(jsonPath("$.[*].category3").value(hasItem(DEFAULT_CATEGORY_3)))
            .andExpect(jsonPath("$.[*].technology").value(hasItem(DEFAULT_TECHNOLOGY)))
            .andExpect(jsonPath("$.[*].documentation").value(hasItem(DEFAULT_DOCUMENTATION)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].importStatus").value(hasItem(DEFAULT_IMPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].importStatusMessage").value(hasItem(DEFAULT_IMPORT_STATUS_MESSAGE)))
            .andExpect(jsonPath("$.[*].existingApplicationID").value(hasItem(DEFAULT_EXISTING_APPLICATION_ID)));
    }

    @Test
    @Transactional
    void getApplicationImport() throws Exception {
        // Initialize the database
        applicationImportRepository.saveAndFlush(applicationImport);

        // Get the applicationImport
        restApplicationImportMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationImport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationImport.getId().intValue()))
            .andExpect(jsonPath("$.importId").value(DEFAULT_IMPORT_ID))
            .andExpect(jsonPath("$.excelFileName").value(DEFAULT_EXCEL_FILE_NAME))
            .andExpect(jsonPath("$.idFromExcel").value(DEFAULT_ID_FROM_EXCEL))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.softwareType").value(DEFAULT_SOFTWARE_TYPE))
            .andExpect(jsonPath("$.category1").value(DEFAULT_CATEGORY_1))
            .andExpect(jsonPath("$.category2").value(DEFAULT_CATEGORY_2))
            .andExpect(jsonPath("$.category3").value(DEFAULT_CATEGORY_3))
            .andExpect(jsonPath("$.technology").value(DEFAULT_TECHNOLOGY))
            .andExpect(jsonPath("$.documentation").value(DEFAULT_DOCUMENTATION))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.importStatus").value(DEFAULT_IMPORT_STATUS.toString()))
            .andExpect(jsonPath("$.importStatusMessage").value(DEFAULT_IMPORT_STATUS_MESSAGE))
            .andExpect(jsonPath("$.existingApplicationID").value(DEFAULT_EXISTING_APPLICATION_ID));
    }

    @Test
    @Transactional
    void getNonExistingApplicationImport() throws Exception {
        // Get the applicationImport
        restApplicationImportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicationImport() throws Exception {
        // Initialize the database
        applicationImportRepository.saveAndFlush(applicationImport);

        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();

        // Update the applicationImport
        ApplicationImport updatedApplicationImport = applicationImportRepository.findById(applicationImport.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationImport are not directly saved in db
        em.detach(updatedApplicationImport);
        updatedApplicationImport
            .importId(UPDATED_IMPORT_ID)
            .excelFileName(UPDATED_EXCEL_FILE_NAME)
            .idFromExcel(UPDATED_ID_FROM_EXCEL)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .softwareType(UPDATED_SOFTWARE_TYPE)
            .category1(UPDATED_CATEGORY_1)
            .category2(UPDATED_CATEGORY_2)
            .category3(UPDATED_CATEGORY_3)
            .technology(UPDATED_TECHNOLOGY)
            .documentation(UPDATED_DOCUMENTATION)
            .comment(UPDATED_COMMENT)
            .importStatus(UPDATED_IMPORT_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE)
            .existingApplicationID(UPDATED_EXISTING_APPLICATION_ID);

        restApplicationImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicationImport))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
        ApplicationImport testApplicationImport = applicationImportList.get(applicationImportList.size() - 1);
        assertThat(testApplicationImport.getImportId()).isEqualTo(UPDATED_IMPORT_ID);
        assertThat(testApplicationImport.getExcelFileName()).isEqualTo(UPDATED_EXCEL_FILE_NAME);
        assertThat(testApplicationImport.getIdFromExcel()).isEqualTo(UPDATED_ID_FROM_EXCEL);
        assertThat(testApplicationImport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicationImport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplicationImport.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplicationImport.getSoftwareType()).isEqualTo(UPDATED_SOFTWARE_TYPE);
        assertThat(testApplicationImport.getCategory1()).isEqualTo(UPDATED_CATEGORY_1);
        assertThat(testApplicationImport.getCategory2()).isEqualTo(UPDATED_CATEGORY_2);
        assertThat(testApplicationImport.getCategory3()).isEqualTo(UPDATED_CATEGORY_3);
        assertThat(testApplicationImport.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testApplicationImport.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testApplicationImport.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testApplicationImport.getImportStatus()).isEqualTo(UPDATED_IMPORT_STATUS);
        assertThat(testApplicationImport.getImportStatusMessage()).isEqualTo(UPDATED_IMPORT_STATUS_MESSAGE);
        assertThat(testApplicationImport.getExistingApplicationID()).isEqualTo(UPDATED_EXISTING_APPLICATION_ID);
    }

    @Test
    @Transactional
    void putNonExistingApplicationImport() throws Exception {
        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();
        applicationImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationImport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationImport() throws Exception {
        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();
        applicationImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationImport() throws Exception {
        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();
        applicationImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationImportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationImport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationImportWithPatch() throws Exception {
        // Initialize the database
        applicationImportRepository.saveAndFlush(applicationImport);

        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();

        // Update the applicationImport using partial update
        ApplicationImport partialUpdatedApplicationImport = new ApplicationImport();
        partialUpdatedApplicationImport.setId(applicationImport.getId());

        partialUpdatedApplicationImport
            .importId(UPDATED_IMPORT_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .category2(UPDATED_CATEGORY_2)
            .category3(UPDATED_CATEGORY_3)
            .documentation(UPDATED_DOCUMENTATION)
            .importStatus(UPDATED_IMPORT_STATUS);

        restApplicationImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationImport))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
        ApplicationImport testApplicationImport = applicationImportList.get(applicationImportList.size() - 1);
        assertThat(testApplicationImport.getImportId()).isEqualTo(UPDATED_IMPORT_ID);
        assertThat(testApplicationImport.getExcelFileName()).isEqualTo(DEFAULT_EXCEL_FILE_NAME);
        assertThat(testApplicationImport.getIdFromExcel()).isEqualTo(DEFAULT_ID_FROM_EXCEL);
        assertThat(testApplicationImport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicationImport.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplicationImport.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplicationImport.getSoftwareType()).isEqualTo(DEFAULT_SOFTWARE_TYPE);
        assertThat(testApplicationImport.getCategory1()).isEqualTo(DEFAULT_CATEGORY_1);
        assertThat(testApplicationImport.getCategory2()).isEqualTo(UPDATED_CATEGORY_2);
        assertThat(testApplicationImport.getCategory3()).isEqualTo(UPDATED_CATEGORY_3);
        assertThat(testApplicationImport.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testApplicationImport.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testApplicationImport.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testApplicationImport.getImportStatus()).isEqualTo(UPDATED_IMPORT_STATUS);
        assertThat(testApplicationImport.getImportStatusMessage()).isEqualTo(DEFAULT_IMPORT_STATUS_MESSAGE);
        assertThat(testApplicationImport.getExistingApplicationID()).isEqualTo(DEFAULT_EXISTING_APPLICATION_ID);
    }

    @Test
    @Transactional
    void fullUpdateApplicationImportWithPatch() throws Exception {
        // Initialize the database
        applicationImportRepository.saveAndFlush(applicationImport);

        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();

        // Update the applicationImport using partial update
        ApplicationImport partialUpdatedApplicationImport = new ApplicationImport();
        partialUpdatedApplicationImport.setId(applicationImport.getId());

        partialUpdatedApplicationImport
            .importId(UPDATED_IMPORT_ID)
            .excelFileName(UPDATED_EXCEL_FILE_NAME)
            .idFromExcel(UPDATED_ID_FROM_EXCEL)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .softwareType(UPDATED_SOFTWARE_TYPE)
            .category1(UPDATED_CATEGORY_1)
            .category2(UPDATED_CATEGORY_2)
            .category3(UPDATED_CATEGORY_3)
            .technology(UPDATED_TECHNOLOGY)
            .documentation(UPDATED_DOCUMENTATION)
            .comment(UPDATED_COMMENT)
            .importStatus(UPDATED_IMPORT_STATUS)
            .importStatusMessage(UPDATED_IMPORT_STATUS_MESSAGE)
            .existingApplicationID(UPDATED_EXISTING_APPLICATION_ID);

        restApplicationImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationImport))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
        ApplicationImport testApplicationImport = applicationImportList.get(applicationImportList.size() - 1);
        assertThat(testApplicationImport.getImportId()).isEqualTo(UPDATED_IMPORT_ID);
        assertThat(testApplicationImport.getExcelFileName()).isEqualTo(UPDATED_EXCEL_FILE_NAME);
        assertThat(testApplicationImport.getIdFromExcel()).isEqualTo(UPDATED_ID_FROM_EXCEL);
        assertThat(testApplicationImport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicationImport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplicationImport.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplicationImport.getSoftwareType()).isEqualTo(UPDATED_SOFTWARE_TYPE);
        assertThat(testApplicationImport.getCategory1()).isEqualTo(UPDATED_CATEGORY_1);
        assertThat(testApplicationImport.getCategory2()).isEqualTo(UPDATED_CATEGORY_2);
        assertThat(testApplicationImport.getCategory3()).isEqualTo(UPDATED_CATEGORY_3);
        assertThat(testApplicationImport.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testApplicationImport.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testApplicationImport.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testApplicationImport.getImportStatus()).isEqualTo(UPDATED_IMPORT_STATUS);
        assertThat(testApplicationImport.getImportStatusMessage()).isEqualTo(UPDATED_IMPORT_STATUS_MESSAGE);
        assertThat(testApplicationImport.getExistingApplicationID()).isEqualTo(UPDATED_EXISTING_APPLICATION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationImport() throws Exception {
        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();
        applicationImport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationImport() throws Exception {
        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();
        applicationImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationImport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationImport() throws Exception {
        int databaseSizeBeforeUpdate = applicationImportRepository.findAll().size();
        applicationImport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationImportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationImport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationImport in the database
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationImport() throws Exception {
        // Initialize the database
        applicationImportRepository.saveAndFlush(applicationImport);

        int databaseSizeBeforeDelete = applicationImportRepository.findAll().size();

        // Delete the applicationImport
        restApplicationImportMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationImport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationImport> applicationImportList = applicationImportRepository.findAll();
        assertThat(applicationImportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
