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
import tech.hyperjump.esigning.domain.SentinelBlock;
import tech.hyperjump.esigning.repository.SentinelBlockRepository;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.SentinelBlock}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SentinelBlockResource {

    private final Logger log = LoggerFactory.getLogger(SentinelBlockResource.class);

    private static final String ENTITY_NAME = "sentinelBlock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SentinelBlockRepository sentinelBlockRepository;

    public SentinelBlockResource(SentinelBlockRepository sentinelBlockRepository) {
        this.sentinelBlockRepository = sentinelBlockRepository;
    }

    /**
     * {@code POST  /sentinel-blocks} : Create a new sentinelBlock.
     *
     * @param sentinelBlock the sentinelBlock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sentinelBlock, or with status {@code 400 (Bad Request)} if the sentinelBlock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sentinel-blocks")
    public ResponseEntity<SentinelBlock> createSentinelBlock(@RequestBody SentinelBlock sentinelBlock) throws URISyntaxException {
        log.debug("REST request to save SentinelBlock : {}", sentinelBlock);
        if (sentinelBlock.getId() != null) {
            throw new BadRequestAlertException("A new sentinelBlock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SentinelBlock result = sentinelBlockRepository.save(sentinelBlock);
        return ResponseEntity
            .created(new URI("/api/sentinel-blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sentinel-blocks/:id} : Updates an existing sentinelBlock.
     *
     * @param id the id of the sentinelBlock to save.
     * @param sentinelBlock the sentinelBlock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sentinelBlock,
     * or with status {@code 400 (Bad Request)} if the sentinelBlock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sentinelBlock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sentinel-blocks/{id}")
    public ResponseEntity<SentinelBlock> updateSentinelBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SentinelBlock sentinelBlock
    ) throws URISyntaxException {
        log.debug("REST request to update SentinelBlock : {}, {}", id, sentinelBlock);
        if (sentinelBlock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sentinelBlock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sentinelBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SentinelBlock result = sentinelBlockRepository.save(sentinelBlock);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sentinelBlock.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sentinel-blocks/:id} : Partial updates given fields of an existing sentinelBlock, field will ignore if it is null
     *
     * @param id the id of the sentinelBlock to save.
     * @param sentinelBlock the sentinelBlock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sentinelBlock,
     * or with status {@code 400 (Bad Request)} if the sentinelBlock is not valid,
     * or with status {@code 404 (Not Found)} if the sentinelBlock is not found,
     * or with status {@code 500 (Internal Server Error)} if the sentinelBlock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sentinel-blocks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SentinelBlock> partialUpdateSentinelBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SentinelBlock sentinelBlock
    ) throws URISyntaxException {
        log.debug("REST request to partial update SentinelBlock partially : {}, {}", id, sentinelBlock);
        if (sentinelBlock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sentinelBlock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sentinelBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SentinelBlock> result = sentinelBlockRepository
            .findById(sentinelBlock.getId())
            .map(existingSentinelBlock -> {
                if (sentinelBlock.getBlockType() != null) {
                    existingSentinelBlock.setBlockType(sentinelBlock.getBlockType());
                }
                if (sentinelBlock.getPlaceholder() != null) {
                    existingSentinelBlock.setPlaceholder(sentinelBlock.getPlaceholder());
                }

                return existingSentinelBlock;
            })
            .map(sentinelBlockRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sentinelBlock.getId().toString())
        );
    }

    /**
     * {@code GET  /sentinel-blocks} : get all the sentinelBlocks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sentinelBlocks in body.
     */
    @GetMapping("/sentinel-blocks")
    public List<SentinelBlock> getAllSentinelBlocks() {
        log.debug("REST request to get all SentinelBlocks");
        return sentinelBlockRepository.findAll();
    }

    /**
     * {@code GET  /sentinel-blocks/:id} : get the "id" sentinelBlock.
     *
     * @param id the id of the sentinelBlock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sentinelBlock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sentinel-blocks/{id}")
    public ResponseEntity<SentinelBlock> getSentinelBlock(@PathVariable Long id) {
        log.debug("REST request to get SentinelBlock : {}", id);
        Optional<SentinelBlock> sentinelBlock = sentinelBlockRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sentinelBlock);
    }

    /**
     * {@code DELETE  /sentinel-blocks/:id} : delete the "id" sentinelBlock.
     *
     * @param id the id of the sentinelBlock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sentinel-blocks/{id}")
    public ResponseEntity<Void> deleteSentinelBlock(@PathVariable Long id) {
        log.debug("REST request to delete SentinelBlock : {}", id);
        sentinelBlockRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
