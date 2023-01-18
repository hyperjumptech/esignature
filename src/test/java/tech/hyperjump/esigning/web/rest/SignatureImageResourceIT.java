package tech.hyperjump.esigning.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.hyperjump.esigning.IntegrationTest;

/**
 * Test class for the SignatureImageResource REST controller.
 *
 * @see SignatureImageResource
 */
@IntegrationTest
class SignatureImageResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        SignatureImageResource signatureImageResource = new SignatureImageResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(signatureImageResource).build();
    }

    /**
     * Test saveSignature
     */
    @Test
    void testSaveSignature() throws Exception {
        restMockMvc.perform(post("/api/signature-image/save-signature")).andExpect(status().isOk());
    }
}
