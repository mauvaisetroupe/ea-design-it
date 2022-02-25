package com.mauvaisetroupe.eadesignit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ViewPoint;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link LandscapeViewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LandscapeViewResourceIT {

    private static final ViewPoint DEFAULT_VIEWPOINT = ViewPoint.APPLICATION_LANDSCAPE;
    private static final ViewPoint UPDATED_VIEWPOINT = ViewPoint.APPLICATION_LANDSCAPE;

    private static final String DEFAULT_DIAGRAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DIAGRAM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPRESSED_DRAW_XML = "AAAAAAAAAA";
    private static final String UPDATED_COMPRESSED_DRAW_XML = "BBBBBBBBBB";

    private static final String DEFAULT_COMPRESSED_DRAW_SVG = "AAAAAAAAAA";
    private static final String UPDATED_COMPRESSED_DRAW_SVG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/landscape-views";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LandscapeViewRepository landscapeViewRepository;

    @Mock
    private LandscapeViewRepository landscapeViewRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLandscapeViewMockMvc;

    private LandscapeView landscapeView;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandscapeView createEntity(EntityManager em) {
        LandscapeView landscapeView = new LandscapeView()
            .viewpoint(DEFAULT_VIEWPOINT)
            .diagramName(DEFAULT_DIAGRAM_NAME)
            .compressedDrawXML(DEFAULT_COMPRESSED_DRAW_XML)
            .compressedDrawSVG(DEFAULT_COMPRESSED_DRAW_SVG);
        return landscapeView;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandscapeView createUpdatedEntity(EntityManager em) {
        LandscapeView landscapeView = new LandscapeView()
            .viewpoint(UPDATED_VIEWPOINT)
            .diagramName(UPDATED_DIAGRAM_NAME)
            .compressedDrawXML(UPDATED_COMPRESSED_DRAW_XML)
            .compressedDrawSVG(UPDATED_COMPRESSED_DRAW_SVG);
        return landscapeView;
    }

    @BeforeEach
    public void initTest() {
        landscapeView = createEntity(em);
    }

    @Test
    @Transactional
    void createLandscapeView() throws Exception {
        int databaseSizeBeforeCreate = landscapeViewRepository.findAll().size();
        // Create the LandscapeView
        restLandscapeViewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeView)))
            .andExpect(status().isCreated());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeCreate + 1);
        LandscapeView testLandscapeView = landscapeViewList.get(landscapeViewList.size() - 1);
        assertThat(testLandscapeView.getViewpoint()).isEqualTo(DEFAULT_VIEWPOINT);
        assertThat(testLandscapeView.getDiagramName()).isEqualTo(DEFAULT_DIAGRAM_NAME);
        assertThat(testLandscapeView.getCompressedDrawXML()).isEqualTo(DEFAULT_COMPRESSED_DRAW_XML);
        assertThat(testLandscapeView.getCompressedDrawSVG()).isEqualTo(DEFAULT_COMPRESSED_DRAW_SVG);
    }

    @Test
    @Transactional
    void createLandscapeViewWithExistingId() throws Exception {
        // Create the LandscapeView with an existing ID
        landscapeView.setId(1L);

        int databaseSizeBeforeCreate = landscapeViewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandscapeViewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeView)))
            .andExpect(status().isBadRequest());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLandscapeViews() throws Exception {
        // Initialize the database
        landscapeViewRepository.saveAndFlush(landscapeView);

        // Get all the landscapeViewList
        restLandscapeViewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landscapeView.getId().intValue())))
            .andExpect(jsonPath("$.[*].viewpoint").value(hasItem(DEFAULT_VIEWPOINT.toString())))
            .andExpect(jsonPath("$.[*].diagramName").value(hasItem(DEFAULT_DIAGRAM_NAME)))
            .andExpect(jsonPath("$.[*].compressedDrawXML").value(hasItem(DEFAULT_COMPRESSED_DRAW_XML.toString())))
            .andExpect(jsonPath("$.[*].compressedDrawSVG").value(hasItem(DEFAULT_COMPRESSED_DRAW_SVG.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLandscapeViewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(landscapeViewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLandscapeViewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(landscapeViewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLandscapeViewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(landscapeViewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLandscapeViewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(landscapeViewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLandscapeView() throws Exception {
        // Initialize the database
        landscapeViewRepository.saveAndFlush(landscapeView);

        // Get the landscapeView
        restLandscapeViewMockMvc
            .perform(get(ENTITY_API_URL_ID, landscapeView.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(landscapeView.getId().intValue()))
            .andExpect(jsonPath("$.viewpoint").value(DEFAULT_VIEWPOINT.toString()))
            .andExpect(jsonPath("$.diagramName").value(DEFAULT_DIAGRAM_NAME))
            .andExpect(jsonPath("$.compressedDrawXML").value(DEFAULT_COMPRESSED_DRAW_XML.toString()))
            .andExpect(jsonPath("$.compressedDrawSVG").value(DEFAULT_COMPRESSED_DRAW_SVG.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLandscapeView() throws Exception {
        // Get the landscapeView
        restLandscapeViewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLandscapeView() throws Exception {
        // Initialize the database
        landscapeViewRepository.saveAndFlush(landscapeView);

        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();

        // Update the landscapeView
        LandscapeView updatedLandscapeView = landscapeViewRepository.findById(landscapeView.getId()).get();
        // Disconnect from session so that the updates on updatedLandscapeView are not directly saved in db
        em.detach(updatedLandscapeView);
        updatedLandscapeView
            .viewpoint(UPDATED_VIEWPOINT)
            .diagramName(UPDATED_DIAGRAM_NAME)
            .compressedDrawXML(UPDATED_COMPRESSED_DRAW_XML)
            .compressedDrawSVG(UPDATED_COMPRESSED_DRAW_SVG);

        restLandscapeViewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLandscapeView.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLandscapeView))
            )
            .andExpect(status().isOk());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
        LandscapeView testLandscapeView = landscapeViewList.get(landscapeViewList.size() - 1);
        assertThat(testLandscapeView.getViewpoint()).isEqualTo(UPDATED_VIEWPOINT);
        assertThat(testLandscapeView.getDiagramName()).isEqualTo(UPDATED_DIAGRAM_NAME);
        assertThat(testLandscapeView.getCompressedDrawXML()).isEqualTo(UPDATED_COMPRESSED_DRAW_XML);
        assertThat(testLandscapeView.getCompressedDrawSVG()).isEqualTo(UPDATED_COMPRESSED_DRAW_SVG);
    }

    @Test
    @Transactional
    void putNonExistingLandscapeView() throws Exception {
        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();
        landscapeView.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandscapeViewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, landscapeView.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landscapeView))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLandscapeView() throws Exception {
        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();
        landscapeView.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandscapeViewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landscapeView))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLandscapeView() throws Exception {
        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();
        landscapeView.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandscapeViewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeView)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLandscapeViewWithPatch() throws Exception {
        // Initialize the database
        landscapeViewRepository.saveAndFlush(landscapeView);

        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();

        // Update the landscapeView using partial update
        LandscapeView partialUpdatedLandscapeView = new LandscapeView();
        partialUpdatedLandscapeView.setId(landscapeView.getId());

        partialUpdatedLandscapeView
            .viewpoint(UPDATED_VIEWPOINT)
            .diagramName(UPDATED_DIAGRAM_NAME)
            .compressedDrawXML(UPDATED_COMPRESSED_DRAW_XML)
            .compressedDrawSVG(UPDATED_COMPRESSED_DRAW_SVG);

        restLandscapeViewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandscapeView.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandscapeView))
            )
            .andExpect(status().isOk());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
        LandscapeView testLandscapeView = landscapeViewList.get(landscapeViewList.size() - 1);
        assertThat(testLandscapeView.getViewpoint()).isEqualTo(UPDATED_VIEWPOINT);
        assertThat(testLandscapeView.getDiagramName()).isEqualTo(UPDATED_DIAGRAM_NAME);
        assertThat(testLandscapeView.getCompressedDrawXML()).isEqualTo(UPDATED_COMPRESSED_DRAW_XML);
        assertThat(testLandscapeView.getCompressedDrawSVG()).isEqualTo(UPDATED_COMPRESSED_DRAW_SVG);
    }

    @Test
    @Transactional
    void fullUpdateLandscapeViewWithPatch() throws Exception {
        // Initialize the database
        landscapeViewRepository.saveAndFlush(landscapeView);

        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();

        // Update the landscapeView using partial update
        LandscapeView partialUpdatedLandscapeView = new LandscapeView();
        partialUpdatedLandscapeView.setId(landscapeView.getId());

        partialUpdatedLandscapeView
            .viewpoint(UPDATED_VIEWPOINT)
            .diagramName(UPDATED_DIAGRAM_NAME)
            .compressedDrawXML(UPDATED_COMPRESSED_DRAW_XML)
            .compressedDrawSVG(UPDATED_COMPRESSED_DRAW_SVG);

        restLandscapeViewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandscapeView.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandscapeView))
            )
            .andExpect(status().isOk());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
        LandscapeView testLandscapeView = landscapeViewList.get(landscapeViewList.size() - 1);
        assertThat(testLandscapeView.getViewpoint()).isEqualTo(UPDATED_VIEWPOINT);
        assertThat(testLandscapeView.getDiagramName()).isEqualTo(UPDATED_DIAGRAM_NAME);
        assertThat(testLandscapeView.getCompressedDrawXML()).isEqualTo(UPDATED_COMPRESSED_DRAW_XML);
        assertThat(testLandscapeView.getCompressedDrawSVG()).isEqualTo(UPDATED_COMPRESSED_DRAW_SVG);
    }

    @Test
    @Transactional
    void patchNonExistingLandscapeView() throws Exception {
        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();
        landscapeView.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandscapeViewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, landscapeView.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landscapeView))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLandscapeView() throws Exception {
        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();
        landscapeView.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandscapeViewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landscapeView))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLandscapeView() throws Exception {
        int databaseSizeBeforeUpdate = landscapeViewRepository.findAll().size();
        landscapeView.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandscapeViewMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(landscapeView))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandscapeView in the database
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLandscapeView() throws Exception {
        // Initialize the database
        landscapeViewRepository.saveAndFlush(landscapeView);

        int databaseSizeBeforeDelete = landscapeViewRepository.findAll().size();

        // Delete the landscapeView
        restLandscapeViewMockMvc
            .perform(delete(ENTITY_API_URL_ID, landscapeView.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LandscapeView> landscapeViewList = landscapeViewRepository.findAll();
        assertThat(landscapeViewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
