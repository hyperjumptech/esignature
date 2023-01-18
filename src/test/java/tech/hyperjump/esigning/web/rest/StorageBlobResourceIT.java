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
import tech.hyperjump.esigning.IntegrationTest;
import tech.hyperjump.esigning.domain.StorageBlob;
import tech.hyperjump.esigning.repository.StorageBlobRepository;

/**
 * Integration tests for the {@link StorageBlobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StorageBlobResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Long DEFAULT_BYTE_SIZE = 1L;
    private static final Long UPDATED_BYTE_SIZE = 2L;

    private static final String DEFAULT_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_CHECKSUM = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/storage-blobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StorageBlobRepository storageBlobRepository;

    @Mock
    private StorageBlobRepository storageBlobRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageBlobMockMvc;

    private StorageBlob storageBlob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageBlob createEntity(EntityManager em) {
        StorageBlob storageBlob = new StorageBlob()
            .key(DEFAULT_KEY)
            .path(DEFAULT_PATH)
            .filename(DEFAULT_FILENAME)
            .contentType(DEFAULT_CONTENT_TYPE)
            .metadata(DEFAULT_METADATA)
            .byteSize(DEFAULT_BYTE_SIZE)
            .checksum(DEFAULT_CHECKSUM)
            .createDate(DEFAULT_CREATE_DATE)
            .createBy(DEFAULT_CREATE_BY)
            .updateDate(DEFAULT_UPDATE_DATE)
            .updateBy(DEFAULT_UPDATE_BY);
        return storageBlob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageBlob createUpdatedEntity(EntityManager em) {
        StorageBlob storageBlob = new StorageBlob()
            .key(UPDATED_KEY)
            .path(UPDATED_PATH)
            .filename(UPDATED_FILENAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .metadata(UPDATED_METADATA)
            .byteSize(UPDATED_BYTE_SIZE)
            .checksum(UPDATED_CHECKSUM)
            .createDate(UPDATED_CREATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateBy(UPDATED_UPDATE_BY);
        return storageBlob;
    }

    @BeforeEach
    public void initTest() {
        storageBlob = createEntity(em);
    }

    @Test
    @Transactional
    void createStorageBlob() throws Exception {
        int databaseSizeBeforeCreate = storageBlobRepository.findAll().size();
        // Create the StorageBlob
        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storageBlob)))
            .andExpect(status().isCreated());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeCreate + 1);
        StorageBlob testStorageBlob = storageBlobList.get(storageBlobList.size() - 1);
        assertThat(testStorageBlob.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testStorageBlob.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testStorageBlob.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testStorageBlob.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testStorageBlob.getMetadata()).isEqualTo(DEFAULT_METADATA);
        assertThat(testStorageBlob.getByteSize()).isEqualTo(DEFAULT_BYTE_SIZE);
        assertThat(testStorageBlob.getChecksum()).isEqualTo(DEFAULT_CHECKSUM);
        assertThat(testStorageBlob.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStorageBlob.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testStorageBlob.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testStorageBlob.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
    }

    @Test
    @Transactional
    void createStorageBlobWithExistingId() throws Exception {
        // Create the StorageBlob with an existing ID
        storageBlob.setId(1L);

        int databaseSizeBeforeCreate = storageBlobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storageBlob)))
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStorageBlobs() throws Exception {
        // Initialize the database
        storageBlobRepository.saveAndFlush(storageBlob);

        // Get all the storageBlobList
        restStorageBlobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageBlob.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].byteSize").value(hasItem(DEFAULT_BYTE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStorageBlobsWithEagerRelationshipsIsEnabled() throws Exception {
        when(storageBlobRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStorageBlobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(storageBlobRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStorageBlobsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(storageBlobRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStorageBlobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(storageBlobRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStorageBlob() throws Exception {
        // Initialize the database
        storageBlobRepository.saveAndFlush(storageBlob);

        // Get the storageBlob
        restStorageBlobMockMvc
            .perform(get(ENTITY_API_URL_ID, storageBlob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storageBlob.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.byteSize").value(DEFAULT_BYTE_SIZE.intValue()))
            .andExpect(jsonPath("$.checksum").value(DEFAULT_CHECKSUM))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY));
    }

    @Test
    @Transactional
    void getNonExistingStorageBlob() throws Exception {
        // Get the storageBlob
        restStorageBlobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStorageBlob() throws Exception {
        // Initialize the database
        storageBlobRepository.saveAndFlush(storageBlob);

        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();

        // Update the storageBlob
        StorageBlob updatedStorageBlob = storageBlobRepository.findById(storageBlob.getId()).get();
        // Disconnect from session so that the updates on updatedStorageBlob are not directly saved in db
        em.detach(updatedStorageBlob);
        updatedStorageBlob
            .key(UPDATED_KEY)
            .path(UPDATED_PATH)
            .filename(UPDATED_FILENAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .metadata(UPDATED_METADATA)
            .byteSize(UPDATED_BYTE_SIZE)
            .checksum(UPDATED_CHECKSUM)
            .createDate(UPDATED_CREATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateBy(UPDATED_UPDATE_BY);

        restStorageBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStorageBlob.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStorageBlob))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
        StorageBlob testStorageBlob = storageBlobList.get(storageBlobList.size() - 1);
        assertThat(testStorageBlob.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testStorageBlob.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testStorageBlob.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testStorageBlob.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testStorageBlob.getMetadata()).isEqualTo(UPDATED_METADATA);
        assertThat(testStorageBlob.getByteSize()).isEqualTo(UPDATED_BYTE_SIZE);
        assertThat(testStorageBlob.getChecksum()).isEqualTo(UPDATED_CHECKSUM);
        assertThat(testStorageBlob.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStorageBlob.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testStorageBlob.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testStorageBlob.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
    }

    @Test
    @Transactional
    void putNonExistingStorageBlob() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();
        storageBlob.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageBlob.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageBlob))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorageBlob() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();
        storageBlob.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storageBlob))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorageBlob() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();
        storageBlob.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storageBlob)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStorageBlobWithPatch() throws Exception {
        // Initialize the database
        storageBlobRepository.saveAndFlush(storageBlob);

        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();

        // Update the storageBlob using partial update
        StorageBlob partialUpdatedStorageBlob = new StorageBlob();
        partialUpdatedStorageBlob.setId(storageBlob.getId());

        partialUpdatedStorageBlob
            .key(UPDATED_KEY)
            .path(UPDATED_PATH)
            .filename(UPDATED_FILENAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .byteSize(UPDATED_BYTE_SIZE);

        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageBlob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStorageBlob))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
        StorageBlob testStorageBlob = storageBlobList.get(storageBlobList.size() - 1);
        assertThat(testStorageBlob.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testStorageBlob.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testStorageBlob.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testStorageBlob.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testStorageBlob.getMetadata()).isEqualTo(DEFAULT_METADATA);
        assertThat(testStorageBlob.getByteSize()).isEqualTo(UPDATED_BYTE_SIZE);
        assertThat(testStorageBlob.getChecksum()).isEqualTo(DEFAULT_CHECKSUM);
        assertThat(testStorageBlob.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStorageBlob.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testStorageBlob.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testStorageBlob.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
    }

    @Test
    @Transactional
    void fullUpdateStorageBlobWithPatch() throws Exception {
        // Initialize the database
        storageBlobRepository.saveAndFlush(storageBlob);

        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();

        // Update the storageBlob using partial update
        StorageBlob partialUpdatedStorageBlob = new StorageBlob();
        partialUpdatedStorageBlob.setId(storageBlob.getId());

        partialUpdatedStorageBlob
            .key(UPDATED_KEY)
            .path(UPDATED_PATH)
            .filename(UPDATED_FILENAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .metadata(UPDATED_METADATA)
            .byteSize(UPDATED_BYTE_SIZE)
            .checksum(UPDATED_CHECKSUM)
            .createDate(UPDATED_CREATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateBy(UPDATED_UPDATE_BY);

        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageBlob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStorageBlob))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
        StorageBlob testStorageBlob = storageBlobList.get(storageBlobList.size() - 1);
        assertThat(testStorageBlob.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testStorageBlob.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testStorageBlob.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testStorageBlob.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testStorageBlob.getMetadata()).isEqualTo(UPDATED_METADATA);
        assertThat(testStorageBlob.getByteSize()).isEqualTo(UPDATED_BYTE_SIZE);
        assertThat(testStorageBlob.getChecksum()).isEqualTo(UPDATED_CHECKSUM);
        assertThat(testStorageBlob.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStorageBlob.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testStorageBlob.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testStorageBlob.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
    }

    @Test
    @Transactional
    void patchNonExistingStorageBlob() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();
        storageBlob.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageBlob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageBlob))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorageBlob() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();
        storageBlob.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storageBlob))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorageBlob() throws Exception {
        int databaseSizeBeforeUpdate = storageBlobRepository.findAll().size();
        storageBlob.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(storageBlob))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageBlob in the database
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStorageBlob() throws Exception {
        // Initialize the database
        storageBlobRepository.saveAndFlush(storageBlob);

        int databaseSizeBeforeDelete = storageBlobRepository.findAll().size();

        // Delete the storageBlob
        restStorageBlobMockMvc
            .perform(delete(ENTITY_API_URL_ID, storageBlob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StorageBlob> storageBlobList = storageBlobRepository.findAll();
        assertThat(storageBlobList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
