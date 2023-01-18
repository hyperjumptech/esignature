package tech.hyperjump.esigning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.hyperjump.esigning.web.rest.TestUtil;

class SentinelBlockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SentinelBlock.class);
        SentinelBlock sentinelBlock1 = new SentinelBlock();
        sentinelBlock1.setId(1L);
        SentinelBlock sentinelBlock2 = new SentinelBlock();
        sentinelBlock2.setId(sentinelBlock1.getId());
        assertThat(sentinelBlock1).isEqualTo(sentinelBlock2);
        sentinelBlock2.setId(2L);
        assertThat(sentinelBlock1).isNotEqualTo(sentinelBlock2);
        sentinelBlock1.setId(null);
        assertThat(sentinelBlock1).isNotEqualTo(sentinelBlock2);
    }
}
