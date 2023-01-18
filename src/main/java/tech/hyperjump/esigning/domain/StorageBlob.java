package tech.hyperjump.esigning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StorageBlob.
 */
@Entity
@Table(name = "storage_blob")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StorageBlob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "path")
    private String path;

    @Column(name = "filename")
    private String filename;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "byte_size")
    private Long byteSize;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @OneToMany(mappedBy = "storageBlob")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sblob", "storageBlob" }, allowSetters = true)
    private Set<StorageBlobAttachment> attachments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "participants", "fields", "storages", "audittrails" }, allowSetters = true)
    private Document document;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StorageBlob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public StorageBlob key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return this.path;
    }

    public StorageBlob path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return this.filename;
    }

    public StorageBlob filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return this.contentType;
    }

    public StorageBlob contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public StorageBlob metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Long getByteSize() {
        return this.byteSize;
    }

    public StorageBlob byteSize(Long byteSize) {
        this.setByteSize(byteSize);
        return this;
    }

    public void setByteSize(Long byteSize) {
        this.byteSize = byteSize;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public StorageBlob checksum(String checksum) {
        this.setChecksum(checksum);
        return this;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public StorageBlob createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public StorageBlob createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public StorageBlob updateDate(Instant updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public StorageBlob updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Set<StorageBlobAttachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<StorageBlobAttachment> storageBlobAttachments) {
        if (this.attachments != null) {
            this.attachments.forEach(i -> i.setStorageBlob(null));
        }
        if (storageBlobAttachments != null) {
            storageBlobAttachments.forEach(i -> i.setStorageBlob(this));
        }
        this.attachments = storageBlobAttachments;
    }

    public StorageBlob attachments(Set<StorageBlobAttachment> storageBlobAttachments) {
        this.setAttachments(storageBlobAttachments);
        return this;
    }

    public StorageBlob addAttachments(StorageBlobAttachment storageBlobAttachment) {
        this.attachments.add(storageBlobAttachment);
        storageBlobAttachment.setStorageBlob(this);
        return this;
    }

    public StorageBlob removeAttachments(StorageBlobAttachment storageBlobAttachment) {
        this.attachments.remove(storageBlobAttachment);
        storageBlobAttachment.setStorageBlob(null);
        return this;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public StorageBlob document(Document document) {
        this.setDocument(document);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageBlob)) {
            return false;
        }
        return id != null && id.equals(((StorageBlob) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageBlob{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", path='" + getPath() + "'" +
            ", filename='" + getFilename() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", byteSize=" + getByteSize() +
            ", checksum='" + getChecksum() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            "}";
    }
}
