package tech.hyperjump.esigning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StorageBlobAttachment.
 */
@Entity
@Table(name = "storage_blob_attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StorageBlobAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "attachment_name")
    private String attachmentName;

    @Column(name = "record_type")
    private String recordType;

    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "attachments", "document" }, allowSetters = true)
    private StorageBlob sblob;

    @ManyToOne
    @JsonIgnoreProperties(value = { "attachments", "document" }, allowSetters = true)
    private StorageBlob storageBlob;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StorageBlobAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentName() {
        return this.attachmentName;
    }

    public StorageBlobAttachment attachmentName(String attachmentName) {
        this.setAttachmentName(attachmentName);
        return this;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getRecordType() {
        return this.recordType;
    }

    public StorageBlobAttachment recordType(String recordType) {
        this.setRecordType(recordType);
        return this;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Long getRecordId() {
        return this.recordId;
    }

    public StorageBlobAttachment recordId(Long recordId) {
        this.setRecordId(recordId);
        return this;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public StorageBlob getSblob() {
        return this.sblob;
    }

    public void setSblob(StorageBlob storageBlob) {
        this.sblob = storageBlob;
    }

    public StorageBlobAttachment sblob(StorageBlob storageBlob) {
        this.setSblob(storageBlob);
        return this;
    }

    public StorageBlob getStorageBlob() {
        return this.storageBlob;
    }

    public void setStorageBlob(StorageBlob storageBlob) {
        this.storageBlob = storageBlob;
    }

    public StorageBlobAttachment storageBlob(StorageBlob storageBlob) {
        this.setStorageBlob(storageBlob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageBlobAttachment)) {
            return false;
        }
        return id != null && id.equals(((StorageBlobAttachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageBlobAttachment{" +
            "id=" + getId() +
            ", attachmentName='" + getAttachmentName() + "'" +
            ", recordType='" + getRecordType() + "'" +
            ", recordId=" + getRecordId() +
            "}";
    }
}
