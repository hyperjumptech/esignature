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
import tech.hyperjump.esigning.domain.AuditTrail;
import tech.hyperjump.esigning.repository.AuditTrailRepository;

/**
 * Integration tests for the {@link AuditTrailResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AuditTrailResourceIT {

    private static final String DEFAULT_ACTIVITY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IPADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IPADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/audit-trails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    @Mock
    private AuditTrailRepository auditTrailRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditTrailMockMvc;

    private AuditTrail auditTrail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditTrail createEntity(EntityManager em) {
        AuditTrail auditTrail = new AuditTrail()
            .activity(DEFAULT_ACTIVITY)
            .description(DEFAULT_DESCRIPTION)
            .ipaddress(DEFAULT_IPADDRESS)
            .time(DEFAULT_TIME);
        return auditTrail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditTrail createUpdatedEntity(EntityManager em) {
        AuditTrail auditTrail = new AuditTrail()
            .activity(UPDATED_ACTIVITY)
            .description(UPDATED_DESCRIPTION)
            .ipaddress(UPDATED_IPADDRESS)
            .time(UPDATED_TIME);
        return auditTrail;
    }

    @BeforeEach
    public void initTest() {
        auditTrail = createEntity(em);
    }

    @Test
    @Transactional
    void createAuditTrail() throws Exception {
        int databaseSizeBeforeCreate = auditTrailRepository.findAll().size();
        // Create the AuditTrail
        restAuditTrailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auditTrail)))
            .andExpect(status().isCreated());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeCreate + 1);
        AuditTrail testAuditTrail = auditTrailList.get(auditTrailList.size() - 1);
        assertThat(testAuditTrail.getActivity()).isEqualTo(DEFAULT_ACTIVITY);
        assertThat(testAuditTrail.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAuditTrail.getIpaddress()).isEqualTo(DEFAULT_IPADDRESS);
        assertThat(testAuditTrail.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    void createAuditTrailWithExistingId() throws Exception {
        // Create the AuditTrail with an existing ID
        auditTrail.setId(1L);

        int databaseSizeBeforeCreate = auditTrailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditTrailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auditTrail)))
            .andExpect(status().isBadRequest());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAuditTrails() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

        // Get all the auditTrailList
        restAuditTrailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditTrail.getId().intValue())))
            .andExpect(jsonPath("$.[*].activity").value(hasItem(DEFAULT_ACTIVITY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].ipaddress").value(hasItem(DEFAULT_IPADDRESS)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAuditTrailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(auditTrailRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAuditTrailMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(auditTrailRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAuditTrailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(auditTrailRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAuditTrailMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(auditTrailRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAuditTrail() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

        // Get the auditTrail
        restAuditTrailMockMvc
            .perform(get(ENTITY_API_URL_ID, auditTrail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditTrail.getId().intValue()))
            .andExpect(jsonPath("$.activity").value(DEFAULT_ACTIVITY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.ipaddress").value(DEFAULT_IPADDRESS))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAuditTrail() throws Exception {
        // Get the auditTrail
        restAuditTrailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuditTrail() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();

        // Update the auditTrail
        AuditTrail updatedAuditTrail = auditTrailRepository.findById(auditTrail.getId()).get();
        // Disconnect from session so that the updates on updatedAuditTrail are not directly saved in db
        em.detach(updatedAuditTrail);
        updatedAuditTrail.activity(UPDATED_ACTIVITY).description(UPDATED_DESCRIPTION).ipaddress(UPDATED_IPADDRESS).time(UPDATED_TIME);

        restAuditTrailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAuditTrail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAuditTrail))
            )
            .andExpect(status().isOk());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
        AuditTrail testAuditTrail = auditTrailList.get(auditTrailList.size() - 1);
        assertThat(testAuditTrail.getActivity()).isEqualTo(UPDATED_ACTIVITY);
        assertThat(testAuditTrail.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAuditTrail.getIpaddress()).isEqualTo(UPDATED_IPADDRESS);
        assertThat(testAuditTrail.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    void putNonExistingAuditTrail() throws Exception {
        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();
        auditTrail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditTrailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auditTrail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auditTrail))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuditTrail() throws Exception {
        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();
        auditTrail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditTrailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auditTrail))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuditTrail() throws Exception {
        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();
        auditTrail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditTrailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auditTrail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuditTrailWithPatch() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();

        // Update the auditTrail using partial update
        AuditTrail partialUpdatedAuditTrail = new AuditTrail();
        partialUpdatedAuditTrail.setId(auditTrail.getId());

        partialUpdatedAuditTrail.time(UPDATED_TIME);

        restAuditTrailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuditTrail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuditTrail))
            )
            .andExpect(status().isOk());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
        AuditTrail testAuditTrail = auditTrailList.get(auditTrailList.size() - 1);
        assertThat(testAuditTrail.getActivity()).isEqualTo(DEFAULT_ACTIVITY);
        assertThat(testAuditTrail.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAuditTrail.getIpaddress()).isEqualTo(DEFAULT_IPADDRESS);
        assertThat(testAuditTrail.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    void fullUpdateAuditTrailWithPatch() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();

        // Update the auditTrail using partial update
        AuditTrail partialUpdatedAuditTrail = new AuditTrail();
        partialUpdatedAuditTrail.setId(auditTrail.getId());

        partialUpdatedAuditTrail
            .activity(UPDATED_ACTIVITY)
            .description(UPDATED_DESCRIPTION)
            .ipaddress(UPDATED_IPADDRESS)
            .time(UPDATED_TIME);

        restAuditTrailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuditTrail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuditTrail))
            )
            .andExpect(status().isOk());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
        AuditTrail testAuditTrail = auditTrailList.get(auditTrailList.size() - 1);
        assertThat(testAuditTrail.getActivity()).isEqualTo(UPDATED_ACTIVITY);
        assertThat(testAuditTrail.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAuditTrail.getIpaddress()).isEqualTo(UPDATED_IPADDRESS);
        assertThat(testAuditTrail.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingAuditTrail() throws Exception {
        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();
        auditTrail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditTrailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, auditTrail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(auditTrail))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuditTrail() throws Exception {
        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();
        auditTrail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditTrailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(auditTrail))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuditTrail() throws Exception {
        int databaseSizeBeforeUpdate = auditTrailRepository.findAll().size();
        auditTrail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditTrailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(auditTrail))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuditTrail in the database
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuditTrail() throws Exception {
        // Initialize the database
        auditTrailRepository.saveAndFlush(auditTrail);

        int databaseSizeBeforeDelete = auditTrailRepository.findAll().size();

        // Delete the auditTrail
        restAuditTrailMockMvc
            .perform(delete(ENTITY_API_URL_ID, auditTrail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditTrail> auditTrailList = auditTrailRepository.findAll();
        assertThat(auditTrailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
