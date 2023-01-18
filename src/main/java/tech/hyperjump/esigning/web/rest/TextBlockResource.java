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
import tech.hyperjump.esigning.domain.TextBlock;
import tech.hyperjump.esigning.repository.TextBlockRepository;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.TextBlock}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TextBlockResource {

    private final Logger log = LoggerFactory.getLogger(TextBlockResource.class);

    private static final String ENTITY_NAME = "textBlock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TextBlockRepository textBlockRepository;

    public TextBlockResource(TextBlockRepository textBlockRepository) {
        this.textBlockRepository = textBlockRepository;
    }

    /**
     * {@code POST  /text-blocks} : Create a new textBlock.
     *
     * @param textBlock the textBlock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new textBlock, or with status {@code 400 (Bad Request)} if the textBlock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/text-blocks")
    public ResponseEntity<TextBlock> createTextBlock(@RequestBody TextBlock textBlock) throws URISyntaxException {
        log.debug("REST request to save TextBlock : {}", textBlock);
        if (textBlock.getId() != null) {
            throw new BadRequestAlertException("A new textBlock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TextBlock result = textBlockRepository.save(textBlock);
        return ResponseEntity
            .created(new URI("/api/text-blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /text-blocks/:id} : Updates an existing textBlock.
     *
     * @param id the id of the textBlock to save.
     * @param textBlock the textBlock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated textBlock,
     * or with status {@code 400 (Bad Request)} if the textBlock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the textBlock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/text-blocks/{id}")
    public ResponseEntity<TextBlock> updateTextBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TextBlock textBlock
    ) throws URISyntaxException {
        log.debug("REST request to update TextBlock : {}, {}", id, textBlock);
        if (textBlock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, textBlock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!textBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TextBlock result = textBlockRepository.save(textBlock);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, textBlock.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /text-blocks/:id} : Partial updates given fields of an existing textBlock, field will ignore if it is null
     *
     * @param id the id of the textBlock to save.
     * @param textBlock the textBlock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated textBlock,
     * or with status {@code 400 (Bad Request)} if the textBlock is not valid,
     * or with status {@code 404 (Not Found)} if the textBlock is not found,
     * or with status {@code 500 (Internal Server Error)} if the textBlock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/text-blocks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TextBlock> partialUpdateTextBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TextBlock textBlock
    ) throws URISyntaxException {
        log.debug("REST request to partial update TextBlock partially : {}, {}", id, textBlock);
        if (textBlock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, textBlock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!textBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TextBlock> result = textBlockRepository
            .findById(textBlock.getId())
            .map(existingTextBlock -> {
                if (textBlock.getTextType() != null) {
                    existingTextBlock.setTextType(textBlock.getTextType());
                }
                if (textBlock.getBody() != null) {
                    existingTextBlock.setBody(textBlock.getBody());
                }
                if (textBlock.getStyling() != null) {
                    existingTextBlock.setStyling(textBlock.getStyling());
                }

                return existingTextBlock;
            })
            .map(textBlockRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, textBlock.getId().toString())
        );
    }

    /**
     * {@code GET  /text-blocks} : get all the textBlocks.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of textBlocks in body.
     */
    @GetMapping("/text-blocks")
    public List<TextBlock> getAllTextBlocks(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all TextBlocks");
        if (eagerload) {
            return textBlockRepository.findAllWithEagerRelationships();
        } else {
            return textBlockRepository.findAll();
        }
    }

    /**
     * {@code GET  /text-blocks/:id} : get the "id" textBlock.
     *
     * @param id the id of the textBlock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the textBlock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/text-blocks/{id}")
    public ResponseEntity<TextBlock> getTextBlock(@PathVariable Long id) {
        log.debug("REST request to get TextBlock : {}", id);
        Optional<TextBlock> textBlock = textBlockRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(textBlock);
    }

    /**
     * {@code DELETE  /text-blocks/:id} : delete the "id" textBlock.
     *
     * @param id the id of the textBlock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/text-blocks/{id}")
    public ResponseEntity<Void> deleteTextBlock(@PathVariable Long id) {
        log.debug("REST request to delete TextBlock : {}", id);
        textBlockRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
