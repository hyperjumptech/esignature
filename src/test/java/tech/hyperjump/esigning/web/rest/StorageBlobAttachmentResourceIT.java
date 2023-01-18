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
import tech.hyperjump.esigning.IntegrationTest;
import tech.hyperjump.esigning.domain.StorageBlobAttachment;
import tech.hyperjump.esigning.repository.StorageBlobAttachmentRepository;

/**
 * Integration tests for the {@link StorageBlobAttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StorageBlobAttachmentResourceIT {

    private static final String DEFAULT_ATTACHMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_RECORD_ID = 1L;
    private static final Long UPDATED_RECORD_ID = 2L;

    private static final String ENTITY_API_URL = "/api/storage-blob-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StorageBlobAttachmentRepository storageBlobAttachmentRepository;

    @Mock
    private StorageBlobAttachmentRepository storageBlobAttachmentRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageBlobAttachmentMockMvc;

    private StorageBlobAttachment storageBlobAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageBlobAttachment createEntity(EntityManager em) {
        StorageBlobAttachment storageBlobAttachment = new StorageBlobAttachment()
            .attachmentName(DEFAULT_ATTACHMENT_NAME)
            .recordType(DEFAULT_RECORD_TYPE)
            .recordId(DEFAULT_RECORD_ID);
        return storageBlobAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageBlobAttachment createUpdatedEntity(EntityManager em) {
        StorageBlobAttachment storageBlobAttachment = new StorageBlobAttachment()
            .attachmentName(UPDATED_ATTACHMENT_NAME)
            .recordType(UPDATED_RECORD_TYPE)
            .recordId(UPDATED_RECORD_ID);
        return storageBlobAttachment;
    }

    @BeforeEach
    public void initTest() {
        storageBlobAttachment = createEntity(em);
    }

    @Test
    @Transactional
    void createStorageBlobAttachment() throws Exception {
        int databaseSizeBeforeCreate = storageBlobAttachmentRepository.findAll().size();
        // Create the StorageBlobAttachment
        restStorageBlobAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageBlobAttachment))
            )
            .andExpect(status().isCreated());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeCreate + 1);
        StorageBlobAttachment testStorageBlobAttachment = storageBlobAttachmentList.get(storageBlobAttachmentList.size() - 1);
        assertThat(testStorageBlobAttachment.getAttachmentName()).isEqualTo(DEFAULT_ATTACHMENT_NAME);
        assertThat(testStorageBlobAttachment.getRecordType()).isEqualTo(DEFAULT_RECORD_TYPE);
        assertThat(testStorageBlobAttachment.getRecordId()).isEqualTo(DEFAULT_RECORD_ID);
    }

    @Test
    @Transactional
    void createStorageBlobAttachmentWithExistingId() throws Exception {
        // Create the StorageBlobAttachment with an existing ID
        storageBlobAttachment.setId(1L);

        int databaseSizeBeforeCreate = storageBlobAttachmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageBlobAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageBlobAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStorageBlobAttachments() throws Exception {
        // Initialize the database
        storageBlobAttachmentRepository.saveAndFlush(storageBlobAttachment);

        // Get all the storageBlobAttachmentList
        restStorageBlobAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageBlobAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentName").value(hasItem(DEFAULT_ATTACHMENT_NAME)))
            .andExpect(jsonPath("$.[*].recordType").value(hasItem(DEFAULT_RECORD_TYPE)))
            .andExpect(jsonPath("$.[*].recordId").value(hasItem(DEFAULT_RECORD_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStorageBlobAttachmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(storageBlobAttachmentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStorageBlobAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(storageBlobAttachmentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStorageBlobAttachmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(storageBlobAttachmentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStorageBlobAttachmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(storageBlobAttachmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStorageBlobAttachment() throws Exception {
        // Initialize the database
        storageBlobAttachmentRepository.saveAndFlush(storageBlobAttachment);

        // Get the storageBlobAttachment
        restStorageBlobAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, storageBlobAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storageBlobAttachment.getId().intValue()))
            .andExpect(jsonPath("$.attachmentName").value(DEFAULT_ATTACHMENT_NAME))
            .andExpect(jsonPath("$.recordType").value(DEFAULT_RECORD_TYPE))
            .andExpect(jsonPath("$.recordId").value(DEFAULT_RECORD_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingStorageBlobAttachment() throws Exception {
        // Get the storageBlobAttachment
        restStorageBlobAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStorageBlobAttachment() throws Exception {
        // Initialize the database
        storageBlobAttachmentRepository.saveAndFlush(storageBlobAttachment);

        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();

        // Update the storageBlobAttachment
        StorageBlobAttachment updatedStorageBlobAttachment = storageBlobAttachmentRepository.findById(storageBlobAttachment.getId()).get();
        // Disconnect from session so that the updates on updatedStorageBlobAttachment are not directly saved in db
        em.detach(updatedStorageBlobAttachment);
        updatedStorageBlobAttachment.attachmentName(UPDATED_ATTACHMENT_NAME).recordType(UPDATED_RECORD_TYPE).recordId(UPDATED_RECORD_ID);

        restStorageBlobAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStorageBlobAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStorageBlobAttachment))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
        StorageBlobAttachment testStorageBlobAttachment = storageBlobAttachmentList.get(storageBlobAttachmentList.size() - 1);
        assertThat(testStorageBlobAttachment.getAttachmentName()).isEqualTo(UPDATED_ATTACHMENT_NAME);
        assertThat(testStorageBlobAttachment.getRecordType()).isEqualTo(UPDATED_RECORD_TYPE);
        assertThat(testStorageBlobAttachment.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void putNonExistingStorageBlobAttachment() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();
        storageBlobAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageBlobAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageBlobAttachment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageBlobAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorageBlobAttachment() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();
        storageBlobAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageBlobAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorageBlobAttachment() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();
        storageBlobAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageBlobAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStorageBlobAttachmentWithPatch() throws Exception {
        // Initialize the database
        storageBlobAttachmentRepository.saveAndFlush(storageBlobAttachment);

        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();

        // Update the storageBlobAttachment using partial update
        StorageBlobAttachment partialUpdatedStorageBlobAttachment = new StorageBlobAttachment();
        partialUpdatedStorageBlobAttachment.setId(storageBlobAttachment.getId());

        partialUpdatedStorageBlobAttachment.recordType(UPDATED_RECORD_TYPE).recordId(UPDATED_RECORD_ID);

        restStorageBlobAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageBlobAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStorageBlobAttachment))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
        StorageBlobAttachment testStorageBlobAttachment = storageBlobAttachmentList.get(storageBlobAttachmentList.size() - 1);
        assertThat(testStorageBlobAttachment.getAttachmentName()).isEqualTo(DEFAULT_ATTACHMENT_NAME);
        assertThat(testStorageBlobAttachment.getRecordType()).isEqualTo(UPDATED_RECORD_TYPE);
        assertThat(testStorageBlobAttachment.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void fullUpdateStorageBlobAttachmentWithPatch() throws Exception {
        // Initialize the database
        storageBlobAttachmentRepository.saveAndFlush(storageBlobAttachment);

        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();

        // Update the storageBlobAttachment using partial update
        StorageBlobAttachment partialUpdatedStorageBlobAttachment = new StorageBlobAttachment();
        partialUpdatedStorageBlobAttachment.setId(storageBlobAttachment.getId());

        partialUpdatedStorageBlobAttachment
            .attachmentName(UPDATED_ATTACHMENT_NAME)
            .recordType(UPDATED_RECORD_TYPE)
            .recordId(UPDATED_RECORD_ID);

        restStorageBlobAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageBlobAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStorageBlobAttachment))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
        StorageBlobAttachment testStorageBlobAttachment = storageBlobAttachmentList.get(storageBlobAttachmentList.size() - 1);
        assertThat(testStorageBlobAttachment.getAttachmentName()).isEqualTo(UPDATED_ATTACHMENT_NAME);
        assertThat(testStorageBlobAttachment.getRecordType()).isEqualTo(UPDATED_RECORD_TYPE);
        assertThat(testStorageBlobAttachment.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void patchNonExistingStorageBlobAttachment() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();
        storageBlobAttachment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageBlobAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageBlobAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageBlobAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorageBlobAttachment() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();
        storageBlobAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageBlobAttachment))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorageBlobAttachment() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobAttachmentRepository.findAll().size();
        storageBlobAttachment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageBlobAttachment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageBlobAttachment in the database
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStorageBlobAttachment() throws Exception {
        // Initialize the database
        storageBlobAttachmentRepository.saveAndFlush(storageBlobAttachment);

        int databaseSizeBeforeDelete = storageBlobAttachmentRepository.findAll().size();

        // Delete the storageBlobAttachment
        restStorageBlobAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, storageBlobAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StorageBlobAttachment> storageBlobAttachmentList = storageBlobAttachmentRepository.findAll();
        assertThat(storageBlobAttachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
