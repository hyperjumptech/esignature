package tech.hyperjump.esigning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ContentField.
 */
@Entity
@Table(name = "content_field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "content_type")
    private String contentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "bbox")
    private String bbox;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "participants", "fields", "storages", "audittrails" }, allowSetters = true)
    private Document document;

    @ManyToOne
    private User signatory;

    @OneToMany(mappedBy = "contentField")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "contentField" }, allowSetters = true)
    private Set<SignatureBlock> signatureBlocks = new HashSet<>();

    @OneToMany(mappedBy = "contentField")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contentField" }, allowSetters = true)
    private Set<SentinelBlock> sentinelBlocks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContentField id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return this.contentType;
    }

    public ContentField contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBbox() {
        return this.bbox;
    }

    public ContentField bbox(String bbox) {
        this.setBbox(bbox);
        return this;
    }

    public void setBbox(String bbox) {
        this.bbox = bbox;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public ContentField createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public ContentField createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public ContentField updateDate(Instant updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public ContentField updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public ContentField document(Document document) {
        this.setDocument(document);
        return this;
    }

    public User getSignatory() {
        return this.signatory;
    }

    public void setSignatory(User user) {
        this.signatory = user;
    }

    public ContentField signatory(User user) {
        this.setSignatory(user);
        return this;
    }

    public Set<SignatureBlock> getSignatureBlocks() {
        return this.signatureBlocks;
    }

    public void setSignatureBlocks(Set<SignatureBlock> signatureBlocks) {
        if (this.signatureBlocks != null) {
            this.signatureBlocks.forEach(i -> i.setContentField(null));
        }
        if (signatureBlocks != null) {
            signatureBlocks.forEach(i -> i.setContentField(this));
        }
        this.signatureBlocks = signatureBlocks;
    }

    public ContentField signatureBlocks(Set<SignatureBlock> signatureBlocks) {
        this.setSignatureBlocks(signatureBlocks);
        return this;
    }

    public ContentField addSignatureBlock(SignatureBlock signatureBlock) {
        this.signatureBlocks.add(signatureBlock);
        signatureBlock.setContentField(this);
        return this;
    }

    public ContentField removeSignatureBlock(SignatureBlock signatureBlock) {
        this.signatureBlocks.remove(signatureBlock);
        signatureBlock.setContentField(null);
        return this;
    }

    public Set<SentinelBlock> getSentinelBlocks() {
        return this.sentinelBlocks;
    }

    public void setSentinelBlocks(Set<SentinelBlock> sentinelBlocks) {
        if (this.sentinelBlocks != null) {
            this.sentinelBlocks.forEach(i -> i.setContentField(null));
        }
        if (sentinelBlocks != null) {
            sentinelBlocks.forEach(i -> i.setContentField(this));
        }
        this.sentinelBlocks = sentinelBlocks;
    }

    public ContentField sentinelBlocks(Set<SentinelBlock> sentinelBlocks) {
        this.setSentinelBlocks(sentinelBlocks);
        return this;
    }

    public ContentField addSentinelBlock(SentinelBlock sentinelBlock) {
        this.sentinelBlocks.add(sentinelBlock);
        sentinelBlock.setContentField(this);
        return this;
    }

    public ContentField removeSentinelBlock(SentinelBlock sentinelBlock) {
        this.sentinelBlocks.remove(sentinelBlock);
        sentinelBlock.setContentField(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentField)) {
            return false;
        }
        return id != null && id.equals(((ContentField) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentField{" +
            "id=" + getId() +
            ", contentType='" + getContentType() + "'" +
            ", bbox='" + getBbox() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            "}";
    }
}
