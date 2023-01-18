package tech.hyperjump.esigning.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.hyperjump.esigning.domain.Document;
import tech.hyperjump.esigning.domain.DocumentParticipant;
import tech.hyperjump.esigning.domain.StorageBlob;
import tech.hyperjump.esigning.domain.User;
import tech.hyperjump.esigning.repository.DocumentParticipantRepository;
import tech.hyperjump.esigning.repository.DocumentRepository;
import tech.hyperjump.esigning.repository.StorageBlobRepository;
import tech.hyperjump.esigning.service.FileStorageService;
import tech.hyperjump.esigning.service.UserService;
import tech.hyperjump.esigning.utils.SigningDocument;
import tech.hyperjump.esigning.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.hyperjump.esigning.domain.Document}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DocumentResource {

    @PersistenceContext
    private EntityManager entityManager;

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    @Autowired
    FileStorageService storageService;

    @Autowired
    UserService userService;

    @Autowired
    StorageBlobRepository storageBlobRepository;

    private static final String ENTITY_NAME = "document";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentRepository documentRepository;
    private final DocumentParticipantRepository participantRepository;

    public DocumentResource(DocumentRepository documentRepository, DocumentParticipantRepository participantRepository) {
        this.documentRepository = documentRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * {@code POST  /documents} : Create a new document.
     *
     * @param document the document to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new document, or with status {@code 400 (Bad Request)} if the document has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/documents")
    public ResponseEntity<Document> createDocument(@Valid @RequestBody Document document) throws URISyntaxException {
        log.debug("REST request to save Document : {}", document);
        if (document.getId() != null) {
            throw new BadRequestAlertException("A new document cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Document result = documentRepository.save(document);
        return ResponseEntity
            .created(new URI("/api/documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /documents/:id} : Updates an existing document.
     *
     * @param id the id of the document to save.
     * @param document the document to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated document,
     * or with status {@code 400 (Bad Request)} if the document is not valid,
     * or with status {@code 500 (Internal Server Error)} if the document couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/documents/{id}")
    public ResponseEntity<Document> updateDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Document document
    ) throws URISyntaxException {
        log.debug("REST request to update Document : {}, {}", id, document);
        if (document.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, document.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Document result = documentRepository.save(document);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, document.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /documents/:id} : Partial updates given fields of an existing document, field will ignore if it is null
     *
     * @param id the id of the document to save.
     * @param document the document to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated document,
     * or with status {@code 400 (Bad Request)} if the document is not valid,
     * or with status {@code 404 (Not Found)} if the document is not found,
     * or with status {@code 500 (Internal Server Error)} if the document couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Document> partialUpdateDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Document document
    ) throws URISyntaxException {
        log.debug("REST request to partial update Document partially : {}, {}", id, document);
        if (document.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, document.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Document> result = documentRepository
            .findById(document.getId())
            .map(existingDocument -> {
                if (document.getTitle() != null) {
                    existingDocument.setTitle(document.getTitle());
                }
                if (document.getDescription() != null) {
                    existingDocument.setDescription(document.getDescription());
                }
                if (document.getCreateDate() != null) {
                    existingDocument.setCreateDate(document.getCreateDate());
                }
                if (document.getCreateBy() != null) {
                    existingDocument.setCreateBy(document.getCreateBy());
                }
                if (document.getUpdateDate() != null) {
                    existingDocument.setUpdateDate(document.getUpdateDate());
                }
                if (document.getUpdateBy() != null) {
                    existingDocument.setUpdateBy(document.getUpdateBy());
                }

                return existingDocument;
            })
            .map(documentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, document.getId().toString())
        );
    }

    /**
     * {@code GET  /documents} : get all the documents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documents in body.
     */
    @GetMapping("/documents")
    public List<Document> getAllDocuments() {
        log.debug("REST request to get all Documents");
        return documentRepository.findAll();
    }

    /**
     * {@code GET  /documents/:id} : get the "id" document.
     *
     * @param id the id of the document to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the document, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/documents/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        log.debug("REST request to get Document : {}", id);
        Optional<Document> document = documentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(document);
    }

    /**
     * {@code DELETE  /documents/:id} : delete the "id" document.
     *
     * @param id the id of the document to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        log.debug("REST request to delete Document : {}", id);
        documentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/documents/by_current_user")
    public List<Document> getDocumentsByCurrentUser() {
        log.debug("REST request to get Document by logged-in user");
        List<DocumentParticipant> documentParticipants = participantRepository.findByUserIsCurrentUser();
        List<Document> documents = new ArrayList<>();
        for (DocumentParticipant participant : documentParticipants) {
            Document aDoc = participant.getDocument();
            // Set<StorageBlob> sb = aDoc.getStorages();
            documents.add(aDoc);
        }
        entityManager.clear();
        List<Document> returnDocs = new ArrayList<>();
        for (Document doc : documents) {
            if (doc != null) {
                doc.setParticipants(null);
                returnDocs.add(doc);
            }
        }
        return returnDocs;
    }

    @GetMapping("/documents/download_sign_document/{docid}/{docfilename}/{signimage}")
    @ResponseBody
    public ResponseEntity<Resource> getSignedDoc(
        @PathVariable Long docid,
        @PathVariable String docfilename,
        @PathVariable String signimage
    ) {
        Optional<Document> aDoc = documentRepository.findById(docid);
        Document doc = aDoc.get();
        Set<StorageBlob> blobs = doc.getStorages();
        String srcname = "uploads/".concat(docfilename);
        String signedName = "signed_".concat(docfilename);
        String imageName = signimage;

        User user = userService.getUserWithAuthorities().get();
        String userfullname = user.getFirstName().concat(user.getLastName());

        String filename = SigningDocument.addUserSigning(srcname, signedName, imageName, userfullname);

        new Timer()
            .schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        // here goes your code to delay
                    }
                },
                1000L
            );

        Resource file = storageService.load(filename); // tood
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }
}
