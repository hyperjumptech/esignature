package tech.hyperjump.esigning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.hyperjump.esigning.web.rest.TestUtil;

class StorageBlobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageBlob.class);
        StorageBlob storageBlob1 = new StorageBlob();
        storageBlob1.setId(1L);
        StorageBlob storageBlob2 = new StorageBlob();
        storageBlob2.setId(storageBlob1.getId());
        assertThat(storageBlob1).isEqualTo(storageBlob2);
        storageBlob2.setId(2L);
        assertThat(storageBlob1).isNotEqualTo(storageBlob2);
        storageBlob1.setId(null);
        assertThat(storageBlob1).isNotEqualTo(storageBlob2);
    }
}
