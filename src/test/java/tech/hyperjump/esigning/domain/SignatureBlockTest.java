package tech.hyperjump.esigning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.hyperjump.esigning.web.rest.TestUtil;

class SignatureBlockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignatureBlock.class);
        SignatureBlock signatureBlock1 = new SignatureBlock();
        signatureBlock1.setId(1L);
        SignatureBlock signatureBlock2 = new SignatureBlock();
        signatureBlock2.setId(signatureBlock1.getId());
        assertThat(signatureBlock1).isEqualTo(signatureBlock2);
        signatureBlock2.setId(2L);
        assertThat(signatureBlock1).isNotEqualTo(signatureBlock2);
        signatureBlock1.setId(null);
        assertThat(signatureBlock1).isNotEqualTo(signatureBlock2);
    }
}
