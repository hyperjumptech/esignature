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
import tech.hyperjump.esigning.domain.StorageBlobAttachment;
import tech.hyperjump.esigning.repository.StorageBlobAttachmentRepository;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.StorageBlobAttachment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StorageBlobAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(StorageBlobAttachmentResource.class);

    private static final String ENTITY_NAME = "storageBlobAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageBlobAttachmentRepository storageBlobAttachmentRepository;

    public StorageBlobAttachmentResource(StorageBlobAttachmentRepository storageBlobAttachmentRepository) {
        this.storageBlobAttachmentRepository = storageBlobAttachmentRepository;
    }

    /**
     * {@code POST  /storage-blob-attachments} : Create a new storageBlobAttachment.
     *
     * @param storageBlobAttachment the storageBlobAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageBlobAttachment, or with status {@code 400 (Bad Request)} if the storageBlobAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/storage-blob-attachments")
    public ResponseEntity<StorageBlobAttachment> createStorageBlobAttachment(@RequestBody StorageBlobAttachment storageBlobAttachment)
        throws URISyntaxException {
        log.debug("REST request to save StorageBlobAttachment : {}", storageBlobAttachment);
        if (storageBlobAttachment.getId() != null) {
            throw new BadRequestAlertException("A new storageBlobAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StorageBlobAttachment result = storageBlobAttachmentRepository.save(storageBlobAttachment);
        return ResponseEntity
            .created(new URI("/api/storage-blob-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /storage-blob-attachments/:id} : Updates an existing storageBlobAttachment.
     *
     * @param id the id of the storageBlobAttachment to save.
     * @param storageBlobAttachment the storageBlobAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageBlobAttachment,
     * or with status {@code 400 (Bad Request)} if the storageBlobAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageBlobAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/storage-blob-attachments/{id}")
    public ResponseEntity<StorageBlobAttachment> updateStorageBlobAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageBlobAttachment storageBlobAttachment
    ) throws URISyntaxException {
        log.debug("REST request to update StorageBlobAttachment : {}, {}", id, storageBlobAttachment);
        if (storageBlobAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageBlobAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageBlobAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StorageBlobAttachment result = storageBlobAttachmentRepository.save(storageBlobAttachment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageBlobAttachment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /storage-blob-attachments/:id} : Partial updates given fields of an existing storageBlobAttachment, field will ignore if it is null
     *
     * @param id the id of the storageBlobAttachment to save.
     * @param storageBlobAttachment the storageBlobAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageBlobAttachment,
     * or with status {@code 400 (Bad Request)} if the storageBlobAttachment is not valid,
     * or with status {@code 404 (Not Found)} if the storageBlobAttachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageBlobAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/storage-blob-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StorageBlobAttachment> partialUpdateStorageBlobAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageBlobAttachment storageBlobAttachment
    ) throws URISyntaxException {
        log.debug("REST request to partial update StorageBlobAttachment partially : {}, {}", id, storageBlobAttachment);
        if (storageBlobAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageBlobAttachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageBlobAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StorageBlobAttachment> result = storageBlobAttachmentRepository
            .findById(storageBlobAttachment.getId())
            .map(existingStorageBlobAttachment -> {
                if (storageBlobAttachment.getAttachmentName() != null) {
                    existingStorageBlobAttachment.setAttachmentName(storageBlobAttachment.getAttachmentName());
                }
                if (storageBlobAttachment.getRecordType() != null) {
                    existingStorageBlobAttachment.setRecordType(storageBlobAttachment.getRecordType());
                }
                if (storageBlobAttachment.getRecordId() != null) {
                    existingStorageBlobAttachment.setRecordId(storageBlobAttachment.getRecordId());
                }

                return existingStorageBlobAttachment;
            })
            .map(storageBlobAttachmentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageBlobAttachment.getId().toString())
        );
    }

    /**
     * {@code GET  /storage-blob-attachments} : get all the storageBlobAttachments.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storageBlobAttachments in body.
     */
    @GetMapping("/storage-blob-attachments")
    public List<StorageBlobAttachment> getAllStorageBlobAttachments(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all StorageBlobAttachments");
        if (eagerload) {
            return storageBlobAttachmentRepository.findAllWithEagerRelationships();
        } else {
            return storageBlobAttachmentRepository.findAll();
        }
    }

    /**
     * {@code GET  /storage-blob-attachments/:id} : get the "id" storageBlobAttachment.
     *
     * @param id the id of the storageBlobAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageBlobAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/storage-blob-attachments/{id}")
    public ResponseEntity<StorageBlobAttachment> getStorageBlobAttachment(@PathVariable Long id) {
        log.debug("REST request to get StorageBlobAttachment : {}", id);
        Optional<StorageBlobAttachment> storageBlobAttachment = storageBlobAttachmentRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(storageBlobAttachment);
    }

    /**
     * {@code DELETE  /storage-blob-attachments/:id} : delete the "id" storageBlobAttachment.
     *
     * @param id the id of the storageBlobAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/storage-blob-attachments/{id}")
    public ResponseEntity<Void> deleteStorageBlobAttachment(@PathVariable Long id) {
        log.debug("REST request to delete StorageBlobAttachment : {}", id);
        storageBlobAttachmentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
