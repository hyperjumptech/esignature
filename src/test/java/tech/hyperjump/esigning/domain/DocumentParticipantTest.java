package tech.hyperjump.esigning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.hyperjump.esigning.web.rest.TestUtil;

class DocumentParticipantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentParticipant.class);
        DocumentParticipant documentParticipant1 = new DocumentParticipant();
        documentParticipant1.setId(1L);
        DocumentParticipant documentParticipant2 = new DocumentParticipant();
        documentParticipant2.setId(documentParticipant1.getId());
        assertThat(documentParticipant1).isEqualTo(documentParticipant2);
        documentParticipant2.setId(2L);
        assertThat(documentParticipant1).isNotEqualTo(documentParticipant2);
        documentParticipant1.setId(null);
        assertThat(documentParticipant1).isNotEqualTo(documentParticipant2);
    }
}
