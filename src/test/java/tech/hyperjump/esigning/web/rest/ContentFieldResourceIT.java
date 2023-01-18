package tech.hyperjump.esigning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import tech.hyperjump.esigning.IntegrationTest;
import tech.hyperjump.esigning.domain.ContentField;
import tech.hyperjump.esigning.repository.ContentFieldRepository;

/**
 * Integration tests for the {@link ContentFieldResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContentFieldResourceIT {

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BBOX = "AAAAAAAAAA";
    private static final String UPDATED_BBOX = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/content-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContentFieldRepository contentFieldRepository;

    @Mock
    private ContentFieldRepository contentFieldRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentFieldMockMvc;

    private ContentField contentField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentField createEntity(EntityManager em) {
        ContentField contentField = new ContentField()
            .contentType(DEFAULT_CONTENT_TYPE)
            .bbox(DEFAULT_BBOX)
            .createDate(DEFAULT_CREATE_DATE)
            .createBy(DEFAULT_CREATE_BY)
            .updateDate(DEFAULT_UPDATE_DATE)
            .updateBy(DEFAULT_UPDATE_BY);
        return contentField;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentField createUpdatedEntity(EntityManager em) {
        ContentField contentField = new ContentField()
            .contentType(UPDATED_CONTENT_TYPE)
            .bbox(UPDATED_BBOX)
            .createDate(UPDATED_CREATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateBy(UPDATED_UPDATE_BY);
        return contentField;
    }

    @BeforeEach
    public void initTest() {
        contentField = createEntity(em);
    }

    @Test
    @Transactional
    void createContentField() throws Exception {
        int databaseSizeBeforeCreate = contentFieldRepository.findAll().size();
        // Create the ContentField
        restContentFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentField)))
            .andExpect(status().isCreated());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeCreate + 1);
        ContentField testContentField = contentFieldList.get(contentFieldList.size() - 1);
        assertThat(testContentField.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testContentField.getBbox()).isEqualTo(DEFAULT_BBOX);
        assertThat(testContentField.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testContentField.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testContentField.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testContentField.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
    }

    @Test
    @Transactional
    void createContentFieldWithExistingId() throws Exception {
        // Create the ContentField with an existing ID
        contentField.setId(1L);

        int databaseSizeBeforeCreate = contentFieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentField)))
            .andExpect(status().isBadRequest());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContentFields() throws Exception {
        // Initialize the database
        contentFieldRepository.saveAndFlush(contentField);

        // Get all the contentFieldList
        restContentFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentField.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bbox").value(hasItem(DEFAULT_BBOX.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContentFieldsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contentFieldRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContentFieldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contentFieldRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContentFieldsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contentFieldRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContentFieldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contentFieldRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContentField() throws Exception {
        // Initialize the database
        contentFieldRepository.saveAndFlush(contentField);

        // Get the contentField
        restContentFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, contentField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contentField.getId().intValue()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.bbox").value(DEFAULT_BBOX.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY));
    }

    @Test
    @Transactional
    void getNonExistingContentField() throws Exception {
        // Get the contentField
        restContentFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContentField() throws Exception {
        // Initialize the database
        contentFieldRepository.saveAndFlush(contentField);

        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();

        // Update the contentField
        ContentField updatedContentField = contentFieldRepository.findById(contentField.getId()).get();
        // Disconnect from session so that the updates on updatedContentField are not directly saved in db
        em.detach(updatedContentField);
        updatedContentField
            .contentType(UPDATED_CONTENT_TYPE)
            .bbox(UPDATED_BBOX)
            .createDate(UPDATED_CREATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateBy(UPDATED_UPDATE_BY);

        restContentFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContentField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContentField))
            )
            .andExpect(status().isOk());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
        ContentField testContentField = contentFieldList.get(contentFieldList.size() - 1);
        assertThat(testContentField.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testContentField.getBbox()).isEqualTo(UPDATED_BBOX);
        assertThat(testContentField.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testContentField.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testContentField.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testContentField.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
    }

    @Test
    @Transactional
    void putNonExistingContentField() throws Exception {
        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();
        contentField.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentField))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContentField() throws Exception {
        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();
        contentField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentField))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContentField() throws Exception {
        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();
        contentField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentField)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentFieldWithPatch() throws Exception {
        // Initialize the database
        contentFieldRepository.saveAndFlush(contentField);

        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();

        // Update the contentField using partial update
        ContentField partialUpdatedContentField = new ContentField();
        partialUpdatedContentField.setId(contentField.getId());

        partialUpdatedContentField
            .contentType(UPDATED_CONTENT_TYPE)
            .bbox(UPDATED_BBOX)
            .createBy(UPDATED_CREATE_BY)
            .updateDate(UPDATED_UPDATE_DATE);

        restContentFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentField))
            )
            .andExpect(status().isOk());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
        ContentField testContentField = contentFieldList.get(contentFieldList.size() - 1);
        assertThat(testContentField.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testContentField.getBbox()).isEqualTo(UPDATED_BBOX);
        assertThat(testContentField.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testContentField.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testContentField.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testContentField.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
    }

    @Test
    @Transactional
    void fullUpdateContentFieldWithPatch() throws Exception {
        // Initialize the database
        contentFieldRepository.saveAndFlush(contentField);

        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();

        // Update the contentField using partial update
        ContentField partialUpdatedContentField = new ContentField();
        partialUpdatedContentField.setId(contentField.getId());

        partialUpdatedContentField
            .contentType(UPDATED_CONTENT_TYPE)
            .bbox(UPDATED_BBOX)
            .createDate(UPDATED_CREATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateBy(UPDATED_UPDATE_BY);

        restContentFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentField))
            )
            .andExpect(status().isOk());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
        ContentField testContentField = contentFieldList.get(contentFieldList.size() - 1);
        assertThat(testContentField.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testContentField.getBbox()).isEqualTo(UPDATED_BBOX);
        assertThat(testContentField.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testContentField.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testContentField.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testContentField.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
    }

    @Test
    @Transactional
    void patchNonExistingContentField() throws Exception {
        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();
        contentField.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentField))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContentField() throws Exception {
        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();
        contentField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentField))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContentField() throws Exception {
        int databaseSizeBeforeUpdate = contentFieldRepository.findAll().size();
        contentField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentFieldMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contentField))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentField in the database
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContentField() throws Exception {
        // Initialize the database
        contentFieldRepository.saveAndFlush(contentField);

        int databaseSizeBeforeDelete = contentFieldRepository.findAll().size();

        // Delete the contentField
        restContentFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, contentField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContentField> contentFieldList = contentFieldRepository.findAll();
        assertThat(contentFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
