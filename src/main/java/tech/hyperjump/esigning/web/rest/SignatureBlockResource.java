package tech.hyperjump.esigning.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.hyperjump.esigning.domain.SignatureBlock;
import tech.hyperjump.esigning.repository.SignatureBlockRepository;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.SignatureBlock}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SignatureBlockResource {

    private final Logger log = LoggerFactory.getLogger(SignatureBlockResource.class);

    private static final String ENTITY_NAME = "signatureBlock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignatureBlockRepository signatureBlockRepository;

    public SignatureBlockResource(SignatureBlockRepository signatureBlockRepository) {
        this.signatureBlockRepository = signatureBlockRepository;
    }

    /**
     * {@code POST  /signature-blocks} : Create a new signatureBlock.
     *
     * @param signatureBlock the signatureBlock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signatureBlock, or with status {@code 400 (Bad Request)} if the signatureBlock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signature-blocks")
    public ResponseEntity<SignatureBlock> createSignatureBlock(@Valid @RequestBody SignatureBlock signatureBlock)
        throws URISyntaxException {
        log.debug("REST request to save SignatureBlock : {}", signatureBlock);
        if (signatureBlock.getId() != null) {
            throw new BadRequestAlertException("A new signatureBlock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignatureBlock result = signatureBlockRepository.save(signatureBlock);
        return ResponseEntity
            .created(new URI("/api/signature-blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signature-blocks/:id} : Updates an existing signatureBlock.
     *
     * @param id the id of the signatureBlock to save.
     * @param signatureBlock the signatureBlock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signatureBlock,
     * or with status {@code 400 (Bad Request)} if the signatureBlock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signatureBlock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/signature-blocks/{id}")
    public ResponseEntity<SignatureBlock> updateSignatureBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SignatureBlock signatureBlock
    ) throws URISyntaxException {
        log.debug("REST request to update SignatureBlock : {}, {}", id, signatureBlock);
        if (signatureBlock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signatureBlock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signatureBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SignatureBlock result = signatureBlockRepository.save(signatureBlock);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signatureBlock.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /signature-blocks/:id} : Partial updates given fields of an existing signatureBlock, field will ignore if it is null
     *
     * @param id the id of the signatureBlock to save.
     * @param signatureBlock the signatureBlock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signatureBlock,
     * or with status {@code 400 (Bad Request)} if the signatureBlock is not valid,
     * or with status {@code 404 (Not Found)} if the signatureBlock is not found,
     * or with status {@code 500 (Internal Server Error)} if the signatureBlock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/signature-blocks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SignatureBlock> partialUpdateSignatureBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SignatureBlock signatureBlock
    ) throws URISyntaxException {
        log.debug("REST request to partial update SignatureBlock partially : {}, {}", id, signatureBlock);
        if (signatureBlock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signatureBlock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signatureBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SignatureBlock> result = signatureBlockRepository
            .findById(signatureBlock.getId())
            .map(existingSignatureBlock -> {
                if (signatureBlock.getStyling() != null) {
                    existingSignatureBlock.setStyling(signatureBlock.getStyling());
                }
                if (signatureBlock.getPubKey() != null) {
                    existingSignatureBlock.setPubKey(signatureBlock.getPubKey());
                }
                if (signatureBlock.getPubKeyFingerprint() != null) {
                    existingSignatureBlock.setPubKeyFingerprint(signatureBlock.getPubKeyFingerprint());
                }

                return existingSignatureBlock;
            })
            .map(signatureBlockRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signatureBlock.getId().toString())
        );
    }

    /**
     * {@code GET  /signature-blocks} : get all the signatureBlocks.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signatureBlocks in body.
     */
    @GetMapping("/signature-blocks")
    public List<SignatureBlock> getAllSignatureBlocks(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all SignatureBlocks");
        if (eagerload) {
            return signatureBlockRepository.findAllWithEagerRelationships();
        } else {
            return signatureBlockRepository.findAll();
        }
    }

    /**
     * {@code GET  /signature-blocks/:id} : get the "id" signatureBlock.
     *
     * @param id the id of the signatureBlock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signatureBlock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signature-blocks/{id}")
    public ResponseEntity<SignatureBlock> getSignatureBlock(@PathVariable Long id) {
        log.debug("REST request to get SignatureBlock : {}", id);
        Optional<SignatureBlock> signatureBlock = signatureBlockRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(signatureBlock);
    }

    /**
     * {@code DELETE  /signature-blocks/:id} : delete the "id" signatureBlock.
     *
     * @param id the id of the signatureBlock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/signature-blocks/{id}")
    public ResponseEntity<Void> deleteSignatureBlock(@PathVariable Long id) {
        log.debug("REST request to delete SignatureBlock : {}", id);
        signatureBlockRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
