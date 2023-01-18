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
import tech.hyperjump.esigning.domain.DocumentParticipant;
import tech.hyperjump.esigning.repository.DocumentParticipantRepository;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.DocumentParticipant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DocumentParticipantResource {

    private final Logger log = LoggerFactory.getLogger(DocumentParticipantResource.class);

    private static final String ENTITY_NAME = "documentParticipant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentParticipantRepository documentParticipantRepository;

    public DocumentParticipantResource(DocumentParticipantRepository documentParticipantRepository) {
        this.documentParticipantRepository = documentParticipantRepository;
    }

    /**
     * {@code POST  /document-participants} : Create a new documentParticipant.
     *
     * @param documentParticipant the documentParticipant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentParticipant, or with status {@code 400 (Bad Request)} if the documentParticipant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-participants")
    public ResponseEntity<DocumentParticipant> createDocumentParticipant(@RequestBody DocumentParticipant documentParticipant)
        throws URISyntaxException {
        log.debug("REST request to save DocumentParticipant : {}", documentParticipant);
        if (documentParticipant.getId() != null) {
            throw new BadRequestAlertException("A new documentParticipant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentParticipant result = documentParticipantRepository.save(documentParticipant);
        return ResponseEntity
            .created(new URI("/api/document-participants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-participants/:id} : Updates an existing documentParticipant.
     *
     * @param id the id of the documentParticipant to save.
     * @param documentParticipant the documentParticipant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentParticipant,
     * or with status {@code 400 (Bad Request)} if the documentParticipant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentParticipant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-participants/{id}")
    public ResponseEntity<DocumentParticipant> updateDocumentParticipant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentParticipant documentParticipant
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentParticipant : {}, {}", id, documentParticipant);
        if (documentParticipant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentParticipant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentParticipantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentParticipant result = documentParticipantRepository.save(documentParticipant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentParticipant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-participants/:id} : Partial updates given fields of an existing documentParticipant, field will ignore if it is null
     *
     * @param id the id of the documentParticipant to save.
     * @param documentParticipant the documentParticipant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentParticipant,
     * or with status {@code 400 (Bad Request)} if the documentParticipant is not valid,
     * or with status {@code 404 (Not Found)} if the documentParticipant is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentParticipant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-participants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentParticipant> partialUpdateDocumentParticipant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentParticipant documentParticipant
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentParticipant partially : {}, {}", id, documentParticipant);
        if (documentParticipant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentParticipant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentParticipantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentParticipant> result = documentParticipantRepository
            .findById(documentParticipant.getId())
            .map(existingDocumentParticipant -> {
                if (documentParticipant.getIsOwner() != null) {
                    existingDocumentParticipant.setIsOwner(documentParticipant.getIsOwner());
                }

                return existingDocumentParticipant;
            })
            .map(documentParticipantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentParticipant.getId().toString())
        );
    }

    /**
     * {@code GET  /document-participants} : get all the documentParticipants.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentParticipants in body.
     */
    @GetMapping("/document-participants")
    public List<DocumentParticipant> getAllDocumentParticipants(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DocumentParticipants");
        if (eagerload) {
            return documentParticipantRepository.findAllWithEagerRelationships();
        } else {
            return documentParticipantRepository.findAll();
        }
    }

    /**
     * {@code GET  /document-participants/:id} : get the "id" documentParticipant.
     *
     * @param id the id of the documentParticipant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentParticipant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-participants/{id}")
    public ResponseEntity<DocumentParticipant> getDocumentParticipant(@PathVariable Long id) {
        log.debug("REST request to get DocumentParticipant : {}", id);
        Optional<DocumentParticipant> documentParticipant = documentParticipantRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(documentParticipant);
    }

    /**
     * {@code DELETE  /document-participants/:id} : delete the "id" documentParticipant.
     *
     * @param id the id of the documentParticipant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-participants/{id}")
    public ResponseEntity<Void> deleteDocumentParticipant(@PathVariable Long id) {
        log.debug("REST request to delete DocumentParticipant : {}", id);
        documentParticipantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
