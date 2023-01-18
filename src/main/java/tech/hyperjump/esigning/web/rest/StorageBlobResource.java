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
import tech.hyperjump.esigning.domain.StorageBlob;
import tech.hyperjump.esigning.repository.StorageBlobRepository;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.StorageBlob}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StorageBlobResource {

    private final Logger log = LoggerFactory.getLogger(StorageBlobResource.class);

    private static final String ENTITY_NAME = "storageBlob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageBlobRepository storageBlobRepository;

    public StorageBlobResource(StorageBlobRepository storageBlobRepository) {
        this.storageBlobRepository = storageBlobRepository;
    }

    /**
     * {@code POST  /storage-blobs} : Create a new storageBlob.
     *
     * @param storageBlob the storageBlob to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageBlob, or with status {@code 400 (Bad Request)} if the storageBlob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/storage-blobs")
    public ResponseEntity<StorageBlob> createStorageBlob(@RequestBody StorageBlob storageBlob) throws URISyntaxException {
        log.debug("REST request to save StorageBlob : {}", storageBlob);
        if (storageBlob.getId() != null) {
            throw new BadRequestAlertException("A new storageBlob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StorageBlob result = storageBlobRepository.save(storageBlob);
        return ResponseEntity
            .created(new URI("/api/storage-blobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /storage-blobs/:id} : Updates an existing storageBlob.
     *
     * @param id the id of the storageBlob to save.
     * @param storageBlob the storageBlob to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageBlob,
     * or with status {@code 400 (Bad Request)} if the storageBlob is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageBlob couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/storage-blobs/{id}")
    public ResponseEntity<StorageBlob> updateStorageBlob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageBlob storageBlob
    ) throws URISyntaxException {
        log.debug("REST request to update StorageBlob : {}, {}", id, storageBlob);
        if (storageBlob.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageBlob.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageBlobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StorageBlob result = storageBlobRepository.save(storageBlob);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageBlob.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /storage-blobs/:id} : Partial updates given fields of an existing storageBlob, field will ignore if it is null
     *
     * @param id the id of the storageBlob to save.
     * @param storageBlob the storageBlob to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageBlob,
     * or with status {@code 400 (Bad Request)} if the storageBlob is not valid,
     * or with status {@code 404 (Not Found)} if the storageBlob is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageBlob couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/storage-blobs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StorageBlob> partialUpdateStorageBlob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageBlob storageBlob
    ) throws URISyntaxException {
        log.debug("REST request to partial update StorageBlob partially : {}, {}", id, storageBlob);
        if (storageBlob.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageBlob.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageBlobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StorageBlob> result = storageBlobRepository
            .findById(storageBlob.getId())
            .map(existingStorageBlob -> {
                if (storageBlob.getKey() != null) {
                    existingStorageBlob.setKey(storageBlob.getKey());
                }
                if (storageBlob.getPath() != null) {
                    existingStorageBlob.setPath(storageBlob.getPath());
                }
                if (storageBlob.getFilename() != null) {
                    existingStorageBlob.setFilename(storageBlob.getFilename());
                }
                if (storageBlob.getContentType() != null) {
                    existingStorageBlob.setContentType(storageBlob.getContentType());
                }
                if (storageBlob.getMetadata() != null) {
                    existingStorageBlob.setMetadata(storageBlob.getMetadata());
                }
                if (storageBlob.getByteSize() != null) {
                    existingStorageBlob.setByteSize(storageBlob.getByteSize());
                }
                if (storageBlob.getChecksum() != null) {
                    existingStorageBlob.setChecksum(storageBlob.getChecksum());
                }
                if (storageBlob.getCreateDate() != null) {
                    existingStorageBlob.setCreateDate(storageBlob.getCreateDate());
                }
                if (storageBlob.getCreateBy() != null) {
                    existingStorageBlob.setCreateBy(storageBlob.getCreateBy());
                }
                if (storageBlob.getUpdateDate() != null) {
                    existingStorageBlob.setUpdateDate(storageBlob.getUpdateDate());
                }
                if (storageBlob.getUpdateBy() != null) {
                    existingStorageBlob.setUpdateBy(storageBlob.getUpdateBy());
                }

                return existingStorageBlob;
            })
            .map(storageBlobRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageBlob.getId().toString())
        );
    }

    /**
     * {@code GET  /storage-blobs} : get all the storageBlobs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storageBlobs in body.
     */
    @GetMapping("/storage-blobs")
    public List<StorageBlob> getAllStorageBlobs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all StorageBlobs");
        if (eagerload) {
            return storageBlobRepository.findAllWithEagerRelationships();
        } else {
            return storageBlobRepository.findAll();
        }
    }

    /**
     * {@code GET  /storage-blobs/:id} : get the "id" storageBlob.
     *
     * @param id the id of the storageBlob to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageBlob, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/storage-blobs/{id}")
    public ResponseEntity<StorageBlob> getStorageBlob(@PathVariable Long id) {
        log.debug("REST request to get StorageBlob : {}", id);
        Optional<StorageBlob> storageBlob = storageBlobRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(storageBlob);
    }

    @GetMapping("/storage-blobs/bydocument/{id}")
    public ResponseEntity<StorageBlob> getStorageBlobByDocumentId(@PathVariable Long id) {
        log.debug("REST request to get StorageBlob by Document id: {}", id);
        Optional<StorageBlob> storageBlob = storageBlobRepository.findOneByDocumentId(id);
        return ResponseUtil.wrapOrNotFound(storageBlob);
    }

    /**
     * {@code DELETE  /storage-blobs/:id} : delete the "id" storageBlob.
     *
     * @param id the id of the storageBlob to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/storage-blobs/{id}")
    public ResponseEntity<Void> deleteStorageBlob(@PathVariable Long id) {
        log.debug("REST request to delete StorageBlob : {}", id);
        storageBlobRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
