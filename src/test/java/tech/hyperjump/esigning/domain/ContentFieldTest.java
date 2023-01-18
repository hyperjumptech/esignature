package tech.hyperjump.esigning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.hyperjump.esigning.web.rest.TestUtil;

class ContentFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentField.class);
        ContentField contentField1 = new ContentField();
        contentField1.setId(1L);
        ContentField contentField2 = new ContentField();
        contentField2.setId(contentField1.getId());
        assertThat(contentField1).isEqualTo(contentField2);
        contentField2.setId(2L);
        assertThat(contentField1).isNotEqualTo(contentField2);
        contentField1.setId(null);
        assertThat(contentField1).isNotEqualTo(contentField2);
    }
}
