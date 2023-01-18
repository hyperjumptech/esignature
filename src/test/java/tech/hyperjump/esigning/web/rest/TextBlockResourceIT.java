package tech.hyperjump.esigning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import tech.hyperjump.esigning.domain.TextBlock;
import tech.hyperjump.esigning.repository.TextBlockRepository;

/**
 * Integration tests for the {@link TextBlockResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TextBlockResourceIT {

    private static final String DEFAULT_TEXT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_STYLING = "AAAAAAAAAA";
    private static final String UPDATED_STYLING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/text-blocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TextBlockRepository textBlockRepository;

    @Mock
    private TextBlockRepository textBlockRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTextBlockMockMvc;

    private TextBlock textBlock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TextBlock createEntity(EntityManager em) {
        TextBlock textBlock = new TextBlock().textType(DEFAULT_TEXT_TYPE).body(DEFAULT_BODY).styling(DEFAULT_STYLING);
        return textBlock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TextBlock createUpdatedEntity(EntityManager em) {
        TextBlock textBlock = new TextBlock().textType(UPDATED_TEXT_TYPE).body(UPDATED_BODY).styling(UPDATED_STYLING);
        return textBlock;
    }

    @BeforeEach
    public void initTest() {
        textBlock = createEntity(em);
    }

    @Test
    @Transactional
    void createTextBlock() throws Exception {
        int databaseSizeBeforeCreate = textBlockRepository.findAll().size();
        // Create the TextBlock
        restTextBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textBlock)))
            .andExpect(status().isCreated());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeCreate + 1);
        TextBlock testTextBlock = textBlockList.get(textBlockList.size() - 1);
        assertThat(testTextBlock.getTextType()).isEqualTo(DEFAULT_TEXT_TYPE);
        assertThat(testTextBlock.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testTextBlock.getStyling()).isEqualTo(DEFAULT_STYLING);
    }

    @Test
    @Transactional
    void createTextBlockWithExistingId() throws Exception {
        // Create the TextBlock with an existing ID
        textBlock.setId(1L);

        int databaseSizeBeforeCreate = textBlockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTextBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textBlock)))
            .andExpect(status().isBadRequest());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTextBlocks() throws Exception {
        // Initialize the database
        textBlockRepository.saveAndFlush(textBlock);

        // Get all the textBlockList
        restTextBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(textBlock.getId().intValue())))
            .andExpect(jsonPath("$.[*].textType").value(hasItem(DEFAULT_TEXT_TYPE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].styling").value(hasItem(DEFAULT_STYLING.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTextBlocksWithEagerRelationshipsIsEnabled() throws Exception {
        when(textBlockRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTextBlockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(textBlockRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTextBlocksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(textBlockRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTextBlockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(textBlockRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTextBlock() throws Exception {
        // Initialize the database
        textBlockRepository.saveAndFlush(textBlock);

        // Get the textBlock
        restTextBlockMockMvc
            .perform(get(ENTITY_API_URL_ID, textBlock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(textBlock.getId().intValue()))
            .andExpect(jsonPath("$.textType").value(DEFAULT_TEXT_TYPE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.styling").value(DEFAULT_STYLING.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTextBlock() throws Exception {
        // Get the textBlock
        restTextBlockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTextBlock() throws Exception {
        // Initialize the database
        textBlockRepository.saveAndFlush(textBlock);

        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();

        // Update the textBlock
        TextBlock updatedTextBlock = textBlockRepository.findById(textBlock.getId()).get();
        // Disconnect from session so that the updates on updatedTextBlock are not directly saved in db
        em.detach(updatedTextBlock);
        updatedTextBlock.textType(UPDATED_TEXT_TYPE).body(UPDATED_BODY).styling(UPDATED_STYLING);

        restTextBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTextBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTextBlock))
            )
            .andExpect(status().isOk());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
        TextBlock testTextBlock = textBlockList.get(textBlockList.size() - 1);
        assertThat(testTextBlock.getTextType()).isEqualTo(UPDATED_TEXT_TYPE);
        assertThat(testTextBlock.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testTextBlock.getStyling()).isEqualTo(UPDATED_STYLING);
    }

    @Test
    @Transactional
    void putNonExistingTextBlock() throws Exception {
        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();
        textBlock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, textBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(textBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTextBlock() throws Exception {
        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();
        textBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(textBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTextBlock() throws Exception {
        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();
        textBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextBlockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textBlock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTextBlockWithPatch() throws Exception {
        // Initialize the database
        textBlockRepository.saveAndFlush(textBlock);

        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();

        // Update the textBlock using partial update
        TextBlock partialUpdatedTextBlock = new TextBlock();
        partialUpdatedTextBlock.setId(textBlock.getId());

        partialUpdatedTextBlock.textType(UPDATED_TEXT_TYPE).body(UPDATED_BODY);

        restTextBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTextBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTextBlock))
            )
            .andExpect(status().isOk());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
        TextBlock testTextBlock = textBlockList.get(textBlockList.size() - 1);
        assertThat(testTextBlock.getTextType()).isEqualTo(UPDATED_TEXT_TYPE);
        assertThat(testTextBlock.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testTextBlock.getStyling()).isEqualTo(DEFAULT_STYLING);
    }

    @Test
    @Transactional
    void fullUpdateTextBlockWithPatch() throws Exception {
        // Initialize the database
        textBlockRepository.saveAndFlush(textBlock);

        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();

        // Update the textBlock using partial update
        TextBlock partialUpdatedTextBlock = new TextBlock();
        partialUpdatedTextBlock.setId(textBlock.getId());

        partialUpdatedTextBlock.textType(UPDATED_TEXT_TYPE).body(UPDATED_BODY).styling(UPDATED_STYLING);

        restTextBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTextBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTextBlock))
            )
            .andExpect(status().isOk());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
        TextBlock testTextBlock = textBlockList.get(textBlockList.size() - 1);
        assertThat(testTextBlock.getTextType()).isEqualTo(UPDATED_TEXT_TYPE);
        assertThat(testTextBlock.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testTextBlock.getStyling()).isEqualTo(UPDATED_STYLING);
    }

    @Test
    @Transactional
    void patchNonExistingTextBlock() throws Exception {
        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();
        textBlock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, textBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(textBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTextBlock() throws Exception {
        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();
        textBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(textBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTextBlock() throws Exception {
        int databaseSizeBeforeUpdate = textBlockRepository.findAll().size();
        textBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextBlockMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(textBlock))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TextBlock in the database
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTextBlock() throws Exception {
        // Initialize the database
        textBlockRepository.saveAndFlush(textBlock);

        int databaseSizeBeforeDelete = textBlockRepository.findAll().size();

        // Delete the textBlock
        restTextBlockMockMvc
            .perform(delete(ENTITY_API_URL_ID, textBlock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TextBlock> textBlockList = textBlockRepository.findAll();
        assertThat(textBlockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
