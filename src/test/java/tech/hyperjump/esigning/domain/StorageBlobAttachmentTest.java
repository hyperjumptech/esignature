package tech.hyperjump.esigning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.hyperjump.esigning.web.rest.TestUtil;

class StorageBlobAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageBlobAttachment.class);
        StorageBlobAttachment storageBlobAttachment1 = new StorageBlobAttachment();
        storageBlobAttachment1.setId(1L);
        StorageBlobAttachment storageBlobAttachment2 = new StorageBlobAttachment();
        storageBlobAttachment2.setId(storageBlobAttachment1.getId());
        assertThat(storageBlobAttachment1).isEqualTo(storageBlobAttachment2);
        storageBlobAttachment2.setId(2L);
        assertThat(storageBlobAttachment1).isNotEqualTo(storageBlobAttachment2);
        storageBlobAttachment1.setId(null);
        assertThat(storageBlobAttachment1).isNotEqualTo(storageBlobAttachment2);
    }
}
