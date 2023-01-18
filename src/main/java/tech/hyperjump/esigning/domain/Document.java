package tech.hyperjump.esigning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @OneToMany(mappedBy = "document", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "document" }, allowSetters = true)
    private Set<DocumentParticipant> participants = new HashSet<>();

    @OneToMany(mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "document", "signatory", "signatureBlocks", "sentinelBlocks" }, allowSetters = true)
    private Set<ContentField> fields = new HashSet<>();

    @OneToMany(mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attachments", "document" }, allowSetters = true)
    private Set<StorageBlob> storages = new HashSet<>();

    @OneToMany(mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "document", "user" }, allowSetters = true)
    private Set<AuditTrail> audittrails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Document id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Document title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Document description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Document createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Document createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public Document updateDate(Instant updateDate) {
        this.setUpdateDate(updateDate);
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public Document updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Set<DocumentParticipant> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<DocumentParticipant> documentParticipants) {
        if (this.participants != null) {
            this.participants.forEach(i -> i.setDocument(null));
        }
        if (documentParticipants != null) {
            documentParticipants.forEach(i -> i.setDocument(this));
        }
        this.participants = documentParticipants;
    }

    public Document participants(Set<DocumentParticipant> documentParticipants) {
        this.setParticipants(documentParticipants);
        return this;
    }

    public Document addParticipants(DocumentParticipant documentParticipant) {
        this.participants.add(documentParticipant);
        documentParticipant.setDocument(this);
        return this;
    }

    public Document removeParticipants(DocumentParticipant documentParticipant) {
        this.participants.remove(documentParticipant);
        documentParticipant.setDocument(null);
        return this;
    }

    public Set<ContentField> getFields() {
        return this.fields;
    }

    public void setFields(Set<ContentField> contentFields) {
        if (this.fields != null) {
            this.fields.forEach(i -> i.setDocument(null));
        }
        if (contentFields != null) {
            contentFields.forEach(i -> i.setDocument(this));
        }
        this.fields = contentFields;
    }

    public Document fields(Set<ContentField> contentFields) {
        this.setFields(contentFields);
        return this;
    }

    public Document addFields(ContentField contentField) {
        this.fields.add(contentField);
        contentField.setDocument(this);
        return this;
    }

    public Document removeFields(ContentField contentField) {
        this.fields.remove(contentField);
        contentField.setDocument(null);
        return this;
    }

    public Set<StorageBlob> getStorages() {
        return this.storages;
    }

    public void setStorages(Set<StorageBlob> storageBlobs) {
        if (this.storages != null) {
            this.storages.forEach(i -> i.setDocument(null));
        }
        if (storageBlobs != null) {
            storageBlobs.forEach(i -> i.setDocument(this));
        }
        this.storages = storageBlobs;
    }

    public Document storages(Set<StorageBlob> storageBlobs) {
        this.setStorages(storageBlobs);
        return this;
    }

    public Document addStorages(StorageBlob storageBlob) {
        this.storages.add(storageBlob);
        storageBlob.setDocument(this);
        return this;
    }

    public Document removeStorages(StorageBlob storageBlob) {
        this.storages.remove(storageBlob);
        storageBlob.setDocument(null);
        return this;
    }

    public Set<AuditTrail> getAudittrails() {
        return this.audittrails;
    }

    public void setAudittrails(Set<AuditTrail> auditTrails) {
        if (this.audittrails != null) {
            this.audittrails.forEach(i -> i.setDocument(null));
        }
        if (auditTrails != null) {
            auditTrails.forEach(i -> i.setDocument(this));
        }
        this.audittrails = auditTrails;
    }

    public Document audittrails(Set<AuditTrail> auditTrails) {
        this.setAudittrails(auditTrails);
        return this;
    }

    public Document addAudittrails(AuditTrail auditTrail) {
        this.audittrails.add(auditTrail);
        auditTrail.setDocument(this);
        return this;
    }

    public Document removeAudittrails(AuditTrail auditTrail) {
        this.audittrails.remove(auditTrail);
        auditTrail.setDocument(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
                "id=" + getId() +
                ", title='" + getTitle() + "'" +
                ", description='" + getDescription() + "'" +
                ", createDate='" + getCreateDate() + "'" +
                ", createBy='" + getCreateBy() + "'" +
                ", updateDate='" + getUpdateDate() + "'" +
                ", updateBy='" + getUpdateBy() + "'" +
                "}";
    }
}
