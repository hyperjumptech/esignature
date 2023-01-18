package tech.hyperjump.esigning.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.hyperjump.esigning.IntegrationTest;

/**
 * Test class for the FileUploadResource REST controller.
 *
 * @see FileUploadResource
 */
@IntegrationTest
class FileUploadResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        FileUploadResource fileUploadResource = new FileUploadResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(fileUploadResource).build();
    }

    /**
     * Test upload
     */
    @Test
    void testUpload() throws Exception {
        // restMockMvc.perform(post("/api/file-upload/upload")).andExpect(status().isOk());
    }

    /**
     * Test download
     */
    @Test
    void testDownload() throws Exception {
        // restMockMvc.perform(get("/api/file-upload/download")).andExpect(status().isOk());
    }
}
