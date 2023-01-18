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
import tech.hyperjump.esigning.domain.ContentField;
import tech.hyperjump.esigning.repository.ContentFieldRepository;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.ContentField}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContentFieldResource {

    private final Logger log = LoggerFactory.getLogger(ContentFieldResource.class);

    private static final String ENTITY_NAME = "contentField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentFieldRepository contentFieldRepository;

    public ContentFieldResource(ContentFieldRepository contentFieldRepository) {
        this.contentFieldRepository = contentFieldRepository;
    }

    /**
     * {@code POST  /content-fields} : Create a new contentField.
     *
     * @param contentField the contentField to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentField, or with status {@code 400 (Bad Request)} if the contentField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/content-fields")
    public ResponseEntity<ContentField> createContentField(@RequestBody ContentField contentField) throws URISyntaxException {
        log.debug("REST request to save ContentField : {}", contentField);
        if (contentField.getId() != null) {
            throw new BadRequestAlertException("A new contentField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContentField result = contentFieldRepository.save(contentField);
        return ResponseEntity
            .created(new URI("/api/content-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /content-fields/:id} : Updates an existing contentField.
     *
     * @param id the id of the contentField to save.
     * @param contentField the contentField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentField,
     * or with status {@code 400 (Bad Request)} if the contentField is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/content-fields/{id}")
    public ResponseEntity<ContentField> updateContentField(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContentField contentField
    ) throws URISyntaxException {
        log.debug("REST request to update ContentField : {}, {}", id, contentField);
        if (contentField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentField.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContentField result = contentFieldRepository.save(contentField);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentField.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /content-fields/:id} : Partial updates given fields of an existing contentField, field will ignore if it is null
     *
     * @param id the id of the contentField to save.
     * @param contentField the contentField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentField,
     * or with status {@code 400 (Bad Request)} if the contentField is not valid,
     * or with status {@code 404 (Not Found)} if the contentField is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/content-fields/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContentField> partialUpdateContentField(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContentField contentField
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContentField partially : {}, {}", id, contentField);
        if (contentField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentField.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentField> result = contentFieldRepository
            .findById(contentField.getId())
            .map(existingContentField -> {
                if (contentField.getContentType() != null) {
                    existingContentField.setContentType(contentField.getContentType());
                }
                if (contentField.getBbox() != null) {
                    existingContentField.setBbox(contentField.getBbox());
                }
                if (contentField.getCreateDate() != null) {
                    existingContentField.setCreateDate(contentField.getCreateDate());
                }
                if (contentField.getCreateBy() != null) {
                    existingContentField.setCreateBy(contentField.getCreateBy());
                }
                if (contentField.getUpdateDate() != null) {
                    existingContentField.setUpdateDate(contentField.getUpdateDate());
                }
                if (contentField.getUpdateBy() != null) {
                    existingContentField.setUpdateBy(contentField.getUpdateBy());
                }

                return existingContentField;
            })
            .map(contentFieldRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentField.getId().toString())
        );
    }

    /**
     * {@code GET  /content-fields} : get all the contentFields.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentFields in body.
     */
    @GetMapping("/content-fields")
    public List<ContentField> getAllContentFields(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ContentFields");
        if (eagerload) {
            return contentFieldRepository.findAllWithEagerRelationships();
        } else {
            return contentFieldRepository.findAll();
        }
    }

    /**
     * {@code GET  /content-fields/:id} : get the "id" contentField.
     *
     * @param id the id of the contentField to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentField, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-fields/{id}")
    public ResponseEntity<ContentField> getContentField(@PathVariable Long id) {
        log.debug("REST request to get ContentField : {}", id);
        Optional<ContentField> contentField = contentFieldRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(contentField);
    }

    /**
     * {@code DELETE  /content-fields/:id} : delete the "id" contentField.
     *
     * @param id the id of the contentField to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/content-fields/{id}")
    public ResponseEntity<Void> deleteContentField(@PathVariable Long id) {
        log.debug("REST request to delete ContentField : {}", id);
        contentFieldRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
