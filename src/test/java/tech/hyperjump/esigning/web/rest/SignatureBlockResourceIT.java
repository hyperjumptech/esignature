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
import tech.hyperjump.esigning.domain.SignatureBlock;
import tech.hyperjump.esigning.repository.SignatureBlockRepository;

/**
 * Integration tests for the {@link SignatureBlockResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SignatureBlockResourceIT {

    private static final String DEFAULT_STYLING = "AAAAAAAAAA";
    private static final String UPDATED_STYLING = "BBBBBBBBBB";

    private static final String DEFAULT_PUB_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PUB_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PUB_KEY_FINGERPRINT = "AAAAAAAAAA";
    private static final String UPDATED_PUB_KEY_FINGERPRINT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/signature-blocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SignatureBlockRepository signatureBlockRepository;

    @Mock
    private SignatureBlockRepository signatureBlockRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignatureBlockMockMvc;

    private SignatureBlock signatureBlock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignatureBlock createEntity(EntityManager em) {
        SignatureBlock signatureBlock = new SignatureBlock()
            .styling(DEFAULT_STYLING)
            .pubKey(DEFAULT_PUB_KEY)
            .pubKeyFingerprint(DEFAULT_PUB_KEY_FINGERPRINT);
        return signatureBlock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignatureBlock createUpdatedEntity(EntityManager em) {
        SignatureBlock signatureBlock = new SignatureBlock()
            .styling(UPDATED_STYLING)
            .pubKey(UPDATED_PUB_KEY)
            .pubKeyFingerprint(UPDATED_PUB_KEY_FINGERPRINT);
        return signatureBlock;
    }

    @BeforeEach
    public void initTest() {
        signatureBlock = createEntity(em);
    }

    @Test
    @Transactional
    void createSignatureBlock() throws Exception {
        int databaseSizeBeforeCreate = signatureBlockRepository.findAll().size();
        // Create the SignatureBlock
        restSignatureBlockMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isCreated());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeCreate + 1);
        SignatureBlock testSignatureBlock = signatureBlockList.get(signatureBlockList.size() - 1);
        assertThat(testSignatureBlock.getStyling()).isEqualTo(DEFAULT_STYLING);
        assertThat(testSignatureBlock.getPubKey()).isEqualTo(DEFAULT_PUB_KEY);
        assertThat(testSignatureBlock.getPubKeyFingerprint()).isEqualTo(DEFAULT_PUB_KEY_FINGERPRINT);
    }

    @Test
    @Transactional
    void createSignatureBlockWithExistingId() throws Exception {
        // Create the SignatureBlock with an existing ID
        signatureBlock.setId(1L);

        int databaseSizeBeforeCreate = signatureBlockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignatureBlockMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPubKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureBlockRepository.findAll().size();
        // set the field null
        signatureBlock.setPubKey(null);

        // Create the SignatureBlock, which fails.

        restSignatureBlockMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isBadRequest());

        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPubKeyFingerprintIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureBlockRepository.findAll().size();
        // set the field null
        signatureBlock.setPubKeyFingerprint(null);

        // Create the SignatureBlock, which fails.

        restSignatureBlockMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isBadRequest());

        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSignatureBlocks() throws Exception {
        // Initialize the database
        signatureBlockRepository.saveAndFlush(signatureBlock);

        // Get all the signatureBlockList
        restSignatureBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signatureBlock.getId().intValue())))
            .andExpect(jsonPath("$.[*].styling").value(hasItem(DEFAULT_STYLING.toString())))
            .andExpect(jsonPath("$.[*].pubKey").value(hasItem(DEFAULT_PUB_KEY)))
            .andExpect(jsonPath("$.[*].pubKeyFingerprint").value(hasItem(DEFAULT_PUB_KEY_FINGERPRINT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSignatureBlocksWithEagerRelationshipsIsEnabled() throws Exception {
        when(signatureBlockRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSignatureBlockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(signatureBlockRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSignatureBlocksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(signatureBlockRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSignatureBlockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(signatureBlockRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSignatureBlock() throws Exception {
        // Initialize the database
        signatureBlockRepository.saveAndFlush(signatureBlock);

        // Get the signatureBlock
        restSignatureBlockMockMvc
            .perform(get(ENTITY_API_URL_ID, signatureBlock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signatureBlock.getId().intValue()))
            .andExpect(jsonPath("$.styling").value(DEFAULT_STYLING.toString()))
            .andExpect(jsonPath("$.pubKey").value(DEFAULT_PUB_KEY))
            .andExpect(jsonPath("$.pubKeyFingerprint").value(DEFAULT_PUB_KEY_FINGERPRINT));
    }

    @Test
    @Transactional
    void getNonExistingSignatureBlock() throws Exception {
        // Get the signatureBlock
        restSignatureBlockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSignatureBlock() throws Exception {
        // Initialize the database
        signatureBlockRepository.saveAndFlush(signatureBlock);

        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();

        // Update the signatureBlock
        SignatureBlock updatedSignatureBlock = signatureBlockRepository.findById(signatureBlock.getId()).get();
        // Disconnect from session so that the updates on updatedSignatureBlock are not directly saved in db
        em.detach(updatedSignatureBlock);
        updatedSignatureBlock.styling(UPDATED_STYLING).pubKey(UPDATED_PUB_KEY).pubKeyFingerprint(UPDATED_PUB_KEY_FINGERPRINT);

        restSignatureBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSignatureBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSignatureBlock))
            )
            .andExpect(status().isOk());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
        SignatureBlock testSignatureBlock = signatureBlockList.get(signatureBlockList.size() - 1);
        assertThat(testSignatureBlock.getStyling()).isEqualTo(UPDATED_STYLING);
        assertThat(testSignatureBlock.getPubKey()).isEqualTo(UPDATED_PUB_KEY);
        assertThat(testSignatureBlock.getPubKeyFingerprint()).isEqualTo(UPDATED_PUB_KEY_FINGERPRINT);
    }

    @Test
    @Transactional
    void putNonExistingSignatureBlock() throws Exception {
        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();
        signatureBlock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignatureBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signatureBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSignatureBlock() throws Exception {
        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();
        signatureBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignatureBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSignatureBlock() throws Exception {
        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();
        signatureBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignatureBlockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signatureBlock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSignatureBlockWithPatch() throws Exception {
        // Initialize the database
        signatureBlockRepository.saveAndFlush(signatureBlock);

        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();

        // Update the signatureBlock using partial update
        SignatureBlock partialUpdatedSignatureBlock = new SignatureBlock();
        partialUpdatedSignatureBlock.setId(signatureBlock.getId());

        partialUpdatedSignatureBlock.styling(UPDATED_STYLING).pubKey(UPDATED_PUB_KEY);

        restSignatureBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignatureBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignatureBlock))
            )
            .andExpect(status().isOk());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
        SignatureBlock testSignatureBlock = signatureBlockList.get(signatureBlockList.size() - 1);
        assertThat(testSignatureBlock.getStyling()).isEqualTo(UPDATED_STYLING);
        assertThat(testSignatureBlock.getPubKey()).isEqualTo(UPDATED_PUB_KEY);
        assertThat(testSignatureBlock.getPubKeyFingerprint()).isEqualTo(DEFAULT_PUB_KEY_FINGERPRINT);
    }

    @Test
    @Transactional
    void fullUpdateSignatureBlockWithPatch() throws Exception {
        // Initialize the database
        signatureBlockRepository.saveAndFlush(signatureBlock);

        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();

        // Update the signatureBlock using partial update
        SignatureBlock partialUpdatedSignatureBlock = new SignatureBlock();
        partialUpdatedSignatureBlock.setId(signatureBlock.getId());

        partialUpdatedSignatureBlock.styling(UPDATED_STYLING).pubKey(UPDATED_PUB_KEY).pubKeyFingerprint(UPDATED_PUB_KEY_FINGERPRINT);

        restSignatureBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignatureBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignatureBlock))
            )
            .andExpect(status().isOk());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
        SignatureBlock testSignatureBlock = signatureBlockList.get(signatureBlockList.size() - 1);
        assertThat(testSignatureBlock.getStyling()).isEqualTo(UPDATED_STYLING);
        assertThat(testSignatureBlock.getPubKey()).isEqualTo(UPDATED_PUB_KEY);
        assertThat(testSignatureBlock.getPubKeyFingerprint()).isEqualTo(UPDATED_PUB_KEY_FINGERPRINT);
    }

    @Test
    @Transactional
    void patchNonExistingSignatureBlock() throws Exception {
        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();
        signatureBlock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignatureBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, signatureBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSignatureBlock() throws Exception {
        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();
        signatureBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignatureBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSignatureBlock() throws Exception {
        int databaseSizeBeforeUpdate = signatureBlockRepository.findAll().size();
        signatureBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignatureBlockMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(signatureBlock))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SignatureBlock in the database
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSignatureBlock() throws Exception {
        // Initialize the database
        signatureBlockRepository.saveAndFlush(signatureBlock);

        int databaseSizeBeforeDelete = signatureBlockRepository.findAll().size();

        // Delete the signatureBlock
        restSignatureBlockMockMvc
            .perform(delete(ENTITY_API_URL_ID, signatureBlock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SignatureBlock> signatureBlockList = signatureBlockRepository.findAll();
        assertThat(signatureBlockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
