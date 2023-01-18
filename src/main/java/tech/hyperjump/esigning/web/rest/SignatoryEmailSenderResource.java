package tech.hyperjump.esigning.web.rest;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hyperjump.esigning.domain.Document;
import tech.hyperjump.esigning.domain.DocumentParticipant;
import tech.hyperjump.esigning.domain.User;
import tech.hyperjump.esigning.repository.DocumentParticipantRepository;
import tech.hyperjump.esigning.repository.DocumentRepository;
import tech.hyperjump.esigning.service.MailService;
import tech.jhipster.web.util.HeaderUtil;

/**
 * SignatoryEmailSenderResource controller
 */
@RestController
@RequestMapping("/api/signatory-email-sender")
public class SignatoryEmailSenderResource {

    private final Logger log = LoggerFactory.getLogger(SignatoryEmailSenderResource.class);
    private final MailService mailService;
    private final DocumentRepository documentRepository;
    private final DocumentParticipantRepository documentParticipantRepository;

    private static final String ENTITY_NAME = "email";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public SignatoryEmailSenderResource(
        MailService mailService,
        DocumentRepository documentRepository,
        DocumentParticipantRepository documentParticipantRepository
    ) {
        this.mailService = mailService;
        this.documentRepository = documentRepository;
        this.documentParticipantRepository = documentParticipantRepository;
    }

    /**
     * POST sendInvitation
     */
    @PostMapping("/send-invitation/{id}")
    public ResponseEntity<String> sendInvitation(@PathVariable Long id) {
        Optional<Document> document = documentRepository.findById(id);
        Document doc = document.get();
        List<DocumentParticipant> allParticipantList = documentParticipantRepository.findAll();
        for (DocumentParticipant documentParticipant : allParticipantList) {
            Document docu = documentParticipant.getDocument();
            User user = documentParticipant.getUser();
            if (docu.getId().equals(id)) {
                this.mailService.sendSigningEmail(user, doc);
            }
        }
        // for (DocumentParticipant documentParticipant : participants) {
        //     User user = documentParticipant.getUser();
        //     this.mailService.sendSigningEmail(user, doc);
        // }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doc.getId().toString()))
            .body("Invitation to signatories was sent");
    }
}
