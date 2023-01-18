package tech.hyperjump.esigning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AuditTrail.
 */
@Entity
@Table(name = "audit_trail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditTrail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "activity")
    private String activity;

    @Column(name = "description")
    private String description;

    @Column(name = "ipaddress")
    private String ipaddress;

    @Column(name = "time")
    private Instant time;

    @ManyToOne
    @JsonIgnoreProperties(value = { "participants", "fields", "storages", "audittrails" }, allowSetters = true)
    private Document document;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AuditTrail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivity() {
        return this.activity;
    }

    public AuditTrail activity(String activity) {
        this.setActivity(activity);
        return this;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return this.description;
    }

    public AuditTrail description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpaddress() {
        return this.ipaddress;
    }

    public AuditTrail ipaddress(String ipaddress) {
        this.setIpaddress(ipaddress);
        return this;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Instant getTime() {
        return this.time;
    }

    public AuditTrail time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public AuditTrail document(Document document) {
        this.setDocument(document);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuditTrail user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditTrail)) {
            return false;
        }
        return id != null && id.equals(((AuditTrail) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditTrail{" +
            "id=" + getId() +
            ", activity='" + getActivity() + "'" +
            ", description='" + getDescription() + "'" +
            ", ipaddress='" + getIpaddress() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }
}
