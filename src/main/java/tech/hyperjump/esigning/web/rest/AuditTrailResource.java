package tech.hyperjump.esigning.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.hyperjump.esigning.domain.AuditTrail;
import tech.hyperjump.esigning.repository.AuditTrailRepository;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.AuditTrail}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AuditTrailResource {

    private final Logger log = LoggerFactory.getLogger(AuditTrailResource.class);

    private static final String ENTITY_NAME = "auditTrail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditTrailRepository auditTrailRepository;

    public AuditTrailResource(AuditTrailRepository auditTrailRepository) {
        this.auditTrailRepository = auditTrailRepository;
    }

    /**
     * {@code POST  /audit-trails} : Create a new auditTrail.
     *
     * @param auditTrail the auditTrail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditTrail, or with status {@code 400 (Bad Request)} if the auditTrail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-trails")
    public ResponseEntity<AuditTrail> createAuditTrail(@RequestBody AuditTrail auditTrail) throws URISyntaxException {
        log.debug("REST request to save AuditTrail : {}", auditTrail);
        if (auditTrail.getId() != null) {
            throw new BadRequestAlertException("A new auditTrail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditTrail result = auditTrailRepository.save(auditTrail);
        return ResponseEntity
            .created(new URI("/api/audit-trails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-trails/:id} : Updates an existing auditTrail.
     *
     * @param id the id of the auditTrail to save.
     * @param auditTrail the auditTrail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditTrail,
     * or with status {@code 400 (Bad Request)} if the auditTrail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditTrail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-trails/{id}")
    public ResponseEntity<AuditTrail> updateAuditTrail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuditTrail auditTrail
    ) throws URISyntaxException {
        log.debug("REST request to update AuditTrail : {}, {}", id, auditTrail);
        if (auditTrail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auditTrail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auditTrailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AuditTrail result = auditTrailRepository.save(auditTrail);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditTrail.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /audit-trails/:id} : Partial updates given fields of an existing auditTrail, field will ignore if it is null
     *
     * @param id the id of the auditTrail to save.
     * @param auditTrail the auditTrail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditTrail,
     * or with status {@code 400 (Bad Request)} if the auditTrail is not valid,
     * or with status {@code 404 (Not Found)} if the auditTrail is not found,
     * or with status {@code 500 (Internal Server Error)} if the auditTrail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/audit-trails/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuditTrail> partialUpdateAuditTrail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuditTrail auditTrail
    ) throws URISyntaxException {
        log.debug("REST request to partial update AuditTrail partially : {}, {}", id, auditTrail);
        if (auditTrail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auditTrail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auditTrailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuditTrail> result = auditTrailRepository
            .findById(auditTrail.getId())
            .map(existingAuditTrail -> {
                if (auditTrail.getActivity() != null) {
                    existingAuditTrail.setActivity(auditTrail.getActivity());
                }
                if (auditTrail.getDescription() != null) {
                    existingAuditTrail.setDescription(auditTrail.getDescription());
                }
                if (auditTrail.getIpaddress() != null) {
                    existingAuditTrail.setIpaddress(auditTrail.getIpaddress());
                }
                if (auditTrail.getTime() != null) {
                    existingAuditTrail.setTime(auditTrail.getTime());
                }

                return existingAuditTrail;
            })
            .map(auditTrailRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditTrail.getId().toString())
        );
    }

    /**
     * {@code GET  /audit-trails} : get all the auditTrails.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditTrails in body.
     */
    @GetMapping("/audit-trails")
    public List<AuditTrail> getAllAuditTrails(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all AuditTrails");
        if (eagerload) {
            return auditTrailRepository.findAllWithEagerRelationships();
        } else {
            return auditTrailRepository.findAll();
        }
    }

    /**
     * {@code GET  /audit-trails/:id} : get the "id" auditTrail.
     *
     * @param id the id of the auditTrail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditTrail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-trails/{id}")
    public ResponseEntity<AuditTrail> getAuditTrail(@PathVariable Long id) {
        log.debug("REST request to get AuditTrail : {}", id);
        Optional<AuditTrail> auditTrail = auditTrailRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(auditTrail);
    }

    /**
     * {@code DELETE  /audit-trails/:id} : delete the "id" auditTrail.
     *
     * @param id the id of the auditTrail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-trails/{id}")
    public ResponseEntity<Void> deleteAuditTrail(@PathVariable Long id) {
        log.debug("REST request to delete AuditTrail : {}", id);
        auditTrailRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
