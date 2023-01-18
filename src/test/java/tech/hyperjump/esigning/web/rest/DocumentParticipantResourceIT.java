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
import tech.hyperjump.esigning.domain.DocumentParticipant;
import tech.hyperjump.esigning.repository.DocumentParticipantRepository;

/**
 * Integration tests for the {@link DocumentParticipantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentParticipantResourceIT {

    private static final Boolean DEFAULT_IS_OWNER = false;
    private static final Boolean UPDATED_IS_OWNER = true;

    private static final String ENTITY_API_URL = "/api/document-participants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentParticipantRepository documentParticipantRepository;

    @Mock
    private DocumentParticipantRepository documentParticipantRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentParticipantMockMvc;

    private DocumentParticipant documentParticipant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentParticipant createEntity(EntityManager em) {
        DocumentParticipant documentParticipant = new DocumentParticipant().isOwner(DEFAULT_IS_OWNER);
        return documentParticipant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentParticipant createUpdatedEntity(EntityManager em) {
        DocumentParticipant documentParticipant = new DocumentParticipant().isOwner(UPDATED_IS_OWNER);
        return documentParticipant;
    }

    @BeforeEach
    public void initTest() {
        documentParticipant = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentParticipant() throws Exception {
        int databaseSizeBeforeCreate = documentParticipantRepository.findAll().size();
        // Create the DocumentParticipant
        restDocumentParticipantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentParticipant))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentParticipant testDocumentParticipant = documentParticipantList.get(documentParticipantList.size() - 1);
        assertThat(testDocumentParticipant.getIsOwner()).isEqualTo(DEFAULT_IS_OWNER);
    }

    @Test
    @Transactional
    void createDocumentParticipantWithExistingId() throws Exception {
        // Create the DocumentParticipant with an existing ID
        documentParticipant.setId(1L);

        int databaseSizeBeforeCreate = documentParticipantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentParticipantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentParticipants() throws Exception {
        // Initialize the database
        documentParticipantRepository.saveAndFlush(documentParticipant);

        // Get all the documentParticipantList
        restDocumentParticipantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentParticipant.getId().intValue())))
            .andExpect(jsonPath("$.[*].isOwner").value(hasItem(DEFAULT_IS_OWNER.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentParticipantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(documentParticipantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentParticipantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(documentParticipantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentParticipantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(documentParticipantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentParticipantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(documentParticipantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDocumentParticipant() throws Exception {
        // Initialize the database
        documentParticipantRepository.saveAndFlush(documentParticipant);

        // Get the documentParticipant
        restDocumentParticipantMockMvc
            .perform(get(ENTITY_API_URL_ID, documentParticipant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentParticipant.getId().intValue()))
            .andExpect(jsonPath("$.isOwner").value(DEFAULT_IS_OWNER.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentParticipant() throws Exception {
        // Get the documentParticipant
        restDocumentParticipantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentParticipant() throws Exception {
        // Initialize the database
        documentParticipantRepository.saveAndFlush(documentParticipant);

        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();

        // Update the documentParticipant
        DocumentParticipant updatedDocumentParticipant = documentParticipantRepository.findById(documentParticipant.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentParticipant are not directly saved in db
        em.detach(updatedDocumentParticipant);
        updatedDocumentParticipant.isOwner(UPDATED_IS_OWNER);

        restDocumentParticipantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentParticipant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentParticipant))
            )
            .andExpect(status().isOk());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
        DocumentParticipant testDocumentParticipant = documentParticipantList.get(documentParticipantList.size() - 1);
        assertThat(testDocumentParticipant.getIsOwner()).isEqualTo(UPDATED_IS_OWNER);
    }

    @Test
    @Transactional
    void putNonExistingDocumentParticipant() throws Exception {
        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();
        documentParticipant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentParticipantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentParticipant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentParticipant() throws Exception {
        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();
        documentParticipant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentParticipantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentParticipant() throws Exception {
        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();
        documentParticipant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentParticipantMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentParticipant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentParticipantWithPatch() throws Exception {
        // Initialize the database
        documentParticipantRepository.saveAndFlush(documentParticipant);

        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();

        // Update the documentParticipant using partial update
        DocumentParticipant partialUpdatedDocumentParticipant = new DocumentParticipant();
        partialUpdatedDocumentParticipant.setId(documentParticipant.getId());

        partialUpdatedDocumentParticipant.isOwner(UPDATED_IS_OWNER);

        restDocumentParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentParticipant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentParticipant))
            )
            .andExpect(status().isOk());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
        DocumentParticipant testDocumentParticipant = documentParticipantList.get(documentParticipantList.size() - 1);
        assertThat(testDocumentParticipant.getIsOwner()).isEqualTo(UPDATED_IS_OWNER);
    }

    @Test
    @Transactional
    void fullUpdateDocumentParticipantWithPatch() throws Exception {
        // Initialize the database
        documentParticipantRepository.saveAndFlush(documentParticipant);

        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();

        // Update the documentParticipant using partial update
        DocumentParticipant partialUpdatedDocumentParticipant = new DocumentParticipant();
        partialUpdatedDocumentParticipant.setId(documentParticipant.getId());

        partialUpdatedDocumentParticipant.isOwner(UPDATED_IS_OWNER);

        restDocumentParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentParticipant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentParticipant))
            )
            .andExpect(status().isOk());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
        DocumentParticipant testDocumentParticipant = documentParticipantList.get(documentParticipantList.size() - 1);
        assertThat(testDocumentParticipant.getIsOwner()).isEqualTo(UPDATED_IS_OWNER);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentParticipant() throws Exception {
        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();
        documentParticipant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentParticipant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentParticipant() throws Exception {
        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();
        documentParticipant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentParticipant))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentParticipant() throws Exception {
        int databaseSizeBeforeUpdate = documentParticipantRepository.findAll().size();
        documentParticipant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentParticipantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentParticipant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentParticipant in the database
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentParticipant() throws Exception {
        // Initialize the database
        documentParticipantRepository.saveAndFlush(documentParticipant);

        int databaseSizeBeforeDelete = documentParticipantRepository.findAll().size();

        // Delete the documentParticipant
        restDocumentParticipantMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentParticipant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentParticipant> documentParticipantList = documentParticipantRepository.findAll();
        assertThat(documentParticipantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
