package tech.hyperjump.esigning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A SignatureBlock.
 */
@Entity
@Table(name = "signature_block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignatureBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "styling")
    private String styling;

    @NotNull
    @Column(name = "pub_key", nullable = false)
    private String pubKey;

    @NotNull
    @Column(name = "pub_key_fingerprint", nullable = false)
    private String pubKeyFingerprint;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "document", "signatory", "signatureBlocks", "sentinelBlocks" }, allowSetters = true)
    private ContentField contentField;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SignatureBlock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStyling() {
        return this.styling;
    }

    public SignatureBlock styling(String styling) {
        this.setStyling(styling);
        return this;
    }

    public void setStyling(String styling) {
        this.styling = styling;
    }

    public String getPubKey() {
        return this.pubKey;
    }

    public SignatureBlock pubKey(String pubKey) {
        this.setPubKey(pubKey);
        return this;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPubKeyFingerprint() {
        return this.pubKeyFingerprint;
    }

    public SignatureBlock pubKeyFingerprint(String pubKeyFingerprint) {
        this.setPubKeyFingerprint(pubKeyFingerprint);
        return this;
    }

    public void setPubKeyFingerprint(String pubKeyFingerprint) {
        this.pubKeyFingerprint = pubKeyFingerprint;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SignatureBlock user(User user) {
        this.setUser(user);
        return this;
    }

    public ContentField getContentField() {
        return this.contentField;
    }

    public void setContentField(ContentField contentField) {
        this.contentField = contentField;
    }

    public SignatureBlock contentField(ContentField contentField) {
        this.setContentField(contentField);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignatureBlock)) {
            return false;
        }
        return id != null && id.equals(((SignatureBlock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignatureBlock{" +
            "id=" + getId() +
            ", styling='" + getStyling() + "'" +
            ", pubKey='" + getPubKey() + "'" +
            ", pubKeyFingerprint='" + getPubKeyFingerprint() + "'" +
            "}";
    }
}
