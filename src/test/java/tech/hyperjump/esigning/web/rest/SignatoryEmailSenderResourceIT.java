package tech.hyperjump.esigning.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.hyperjump.esigning.IntegrationTest;
import tech.hyperjump.esigning.service.MailService;

/**
 * Test class for the SignatoryEmailSenderResource REST controller.
 *
 * @see SignatoryEmailSenderResource
 */
@IntegrationTest
class SignatoryEmailSenderResourceIT {

    // private MockMvc restMockMvc;
    // private MailService mailService;

    @BeforeEach
    public void setUp() {
        // MockitoAnnotations.openMocks(this);
        // this.mailService = new MailService(null, null, null, null);

        // SignatoryEmailSenderResource signatoryEmailSenderResource = new SignatoryEmailSenderResource(mailService);
        // restMockMvc = MockMvcBuilders.standaloneSetup(signatoryEmailSenderResource).build();
    }

    /**
     * Test sendInvitation
     */
    @Test
    void testSendInvitation() throws Exception {
        //restMockMvc.perform(post("/api/signatory-email-sender/send-invitation")).andExpect(status().isOk());
    }
}
