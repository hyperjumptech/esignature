package tech.hyperjump.esigning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import tech.hyperjump.esigning.IntegrationTest;
import tech.hyperjump.esigning.domain.SentinelBlock;
import tech.hyperjump.esigning.repository.SentinelBlockRepository;

/**
 * Integration tests for the {@link SentinelBlockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SentinelBlockResourceIT {

    private static final String DEFAULT_BLOCK_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BLOCK_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PLACEHOLDER = "AAAAAAAAAA";
    private static final String UPDATED_PLACEHOLDER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sentinel-blocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SentinelBlockRepository sentinelBlockRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSentinelBlockMockMvc;

    private SentinelBlock sentinelBlock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SentinelBlock createEntity(EntityManager em) {
        SentinelBlock sentinelBlock = new SentinelBlock().blockType(DEFAULT_BLOCK_TYPE).placeholder(DEFAULT_PLACEHOLDER);
        return sentinelBlock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SentinelBlock createUpdatedEntity(EntityManager em) {
        SentinelBlock sentinelBlock = new SentinelBlock().blockType(UPDATED_BLOCK_TYPE).placeholder(UPDATED_PLACEHOLDER);
        return sentinelBlock;
    }

    @BeforeEach
    public void initTest() {
        sentinelBlock = createEntity(em);
    }

    @Test
    @Transactional
    void createSentinelBlock() throws Exception {
        int databaseSizeBeforeCreate = sentinelBlockRepository.findAll().size();
        // Create the SentinelBlock
        restSentinelBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sentinelBlock)))
            .andExpect(status().isCreated());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeCreate + 1);
        SentinelBlock testSentinelBlock = sentinelBlockList.get(sentinelBlockList.size() - 1);
        assertThat(testSentinelBlock.getBlockType()).isEqualTo(DEFAULT_BLOCK_TYPE);
        assertThat(testSentinelBlock.getPlaceholder()).isEqualTo(DEFAULT_PLACEHOLDER);
    }

    @Test
    @Transactional
    void createSentinelBlockWithExistingId() throws Exception {
        // Create the SentinelBlock with an existing ID
        sentinelBlock.setId(1L);

        int databaseSizeBeforeCreate = sentinelBlockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSentinelBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sentinelBlock)))
            .andExpect(status().isBadRequest());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSentinelBlocks() throws Exception {
        // Initialize the database
        sentinelBlockRepository.saveAndFlush(sentinelBlock);

        // Get all the sentinelBlockList
        restSentinelBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sentinelBlock.getId().intValue())))
            .andExpect(jsonPath("$.[*].blockType").value(hasItem(DEFAULT_BLOCK_TYPE)))
            .andExpect(jsonPath("$.[*].placeholder").value(hasItem(DEFAULT_PLACEHOLDER)));
    }

    @Test
    @Transactional
    void getSentinelBlock() throws Exception {
        // Initialize the database
        sentinelBlockRepository.saveAndFlush(sentinelBlock);

        // Get the sentinelBlock
        restSentinelBlockMockMvc
            .perform(get(ENTITY_API_URL_ID, sentinelBlock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sentinelBlock.getId().intValue()))
            .andExpect(jsonPath("$.blockType").value(DEFAULT_BLOCK_TYPE))
            .andExpect(jsonPath("$.placeholder").value(DEFAULT_PLACEHOLDER));
    }

    @Test
    @Transactional
    void getNonExistingSentinelBlock() throws Exception {
        // Get the sentinelBlock
        restSentinelBlockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSentinelBlock() throws Exception {
        // Initialize the database
        sentinelBlockRepository.saveAndFlush(sentinelBlock);

        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();

        // Update the sentinelBlock
        SentinelBlock updatedSentinelBlock = sentinelBlockRepository.findById(sentinelBlock.getId()).get();
        // Disconnect from session so that the updates on updatedSentinelBlock are not directly saved in db
        em.detach(updatedSentinelBlock);
        updatedSentinelBlock.blockType(UPDATED_BLOCK_TYPE).placeholder(UPDATED_PLACEHOLDER);

        restSentinelBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSentinelBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSentinelBlock))
            )
            .andExpect(status().isOk());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
        SentinelBlock testSentinelBlock = sentinelBlockList.get(sentinelBlockList.size() - 1);
        assertThat(testSentinelBlock.getBlockType()).isEqualTo(UPDATED_BLOCK_TYPE);
        assertThat(testSentinelBlock.getPlaceholder()).isEqualTo(UPDATED_PLACEHOLDER);
    }

    @Test
    @Transactional
    void putNonExistingSentinelBlock() throws Exception {
        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();
        sentinelBlock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSentinelBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sentinelBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sentinelBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSentinelBlock() throws Exception {
        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();
        sentinelBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSentinelBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sentinelBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSentinelBlock() throws Exception {
        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();
        sentinelBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSentinelBlockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sentinelBlock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSentinelBlockWithPatch() throws Exception {
        // Initialize the database
        sentinelBlockRepository.saveAndFlush(sentinelBlock);

        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();

        // Update the sentinelBlock using partial update
        SentinelBlock partialUpdatedSentinelBlock = new SentinelBlock();
        partialUpdatedSentinelBlock.setId(sentinelBlock.getId());

        partialUpdatedSentinelBlock.blockType(UPDATED_BLOCK_TYPE).placeholder(UPDATED_PLACEHOLDER);

        restSentinelBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSentinelBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSentinelBlock))
            )
            .andExpect(status().isOk());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
        SentinelBlock testSentinelBlock = sentinelBlockList.get(sentinelBlockList.size() - 1);
        assertThat(testSentinelBlock.getBlockType()).isEqualTo(UPDATED_BLOCK_TYPE);
        assertThat(testSentinelBlock.getPlaceholder()).isEqualTo(UPDATED_PLACEHOLDER);
    }

    @Test
    @Transactional
    void fullUpdateSentinelBlockWithPatch() throws Exception {
        // Initialize the database
        sentinelBlockRepository.saveAndFlush(sentinelBlock);

        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();

        // Update the sentinelBlock using partial update
        SentinelBlock partialUpdatedSentinelBlock = new SentinelBlock();
        partialUpdatedSentinelBlock.setId(sentinelBlock.getId());

        partialUpdatedSentinelBlock.blockType(UPDATED_BLOCK_TYPE).placeholder(UPDATED_PLACEHOLDER);

        restSentinelBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSentinelBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSentinelBlock))
            )
            .andExpect(status().isOk());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
        SentinelBlock testSentinelBlock = sentinelBlockList.get(sentinelBlockList.size() - 1);
        assertThat(testSentinelBlock.getBlockType()).isEqualTo(UPDATED_BLOCK_TYPE);
        assertThat(testSentinelBlock.getPlaceholder()).isEqualTo(UPDATED_PLACEHOLDER);
    }

    @Test
    @Transactional
    void patchNonExistingSentinelBlock() throws Exception {
        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();
        sentinelBlock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSentinelBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sentinelBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sentinelBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSentinelBlock() throws Exception {
        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();
        sentinelBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSentinelBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sentinelBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSentinelBlock() throws Exception {
        int databaseSizeBeforeUpdate = sentinelBlockRepository.findAll().size();
        sentinelBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSentinelBlockMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sentinelBlock))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SentinelBlock in the database
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSentinelBlock() throws Exception {
        // Initialize the database
        sentinelBlockRepository.saveAndFlush(sentinelBlock);

        int databaseSizeBeforeDelete = sentinelBlockRepository.findAll().size();

        // Delete the sentinelBlock
        restSentinelBlockMockMvc
            .perform(delete(ENTITY_API_URL_ID, sentinelBlock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SentinelBlock> sentinelBlockList = sentinelBlockRepository.findAll();
        assertThat(sentinelBlockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
