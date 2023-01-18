package tech.hyperjump.esigning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SentinelBlock.
 */
@Entity
@Table(name = "sentinel_block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SentinelBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "block_type")
    private String blockType;

    @Column(name = "placeholder")
    private String placeholder;

    @ManyToOne
    @JsonIgnoreProperties(value = { "document", "signatory", "signatureBlocks", "sentinelBlocks" }, allowSetters = true)
    private ContentField contentField;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SentinelBlock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlockType() {
        return this.blockType;
    }

    public SentinelBlock blockType(String blockType) {
        this.setBlockType(blockType);
        return this;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public SentinelBlock placeholder(String placeholder) {
        this.setPlaceholder(placeholder);
        return this;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public ContentField getContentField() {
        return this.contentField;
    }

    public void setContentField(ContentField contentField) {
        this.contentField = contentField;
    }

    public SentinelBlock contentField(ContentField contentField) {
        this.setContentField(contentField);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SentinelBlock)) {
            return false;
        }
        return id != null && id.equals(((SentinelBlock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SentinelBlock{" +
            "id=" + getId() +
            ", blockType='" + getBlockType() + "'" +
            ", placeholder='" + getPlaceholder() + "'" +
            "}";
    }
}
