package tech.hyperjump.esigning.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A TextBlock.
 */
@Entity
@Table(name = "text_block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TextBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "text_type")
    private String textType;

    @Column(name = "body")
    private String body;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "styling")
    private String styling;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TextBlock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextType() {
        return this.textType;
    }

    public TextBlock textType(String textType) {
        this.setTextType(textType);
        return this;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public String getBody() {
        return this.body;
    }

    public TextBlock body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStyling() {
        return this.styling;
    }

    public TextBlock styling(String styling) {
        this.setStyling(styling);
        return this;
    }

    public void setStyling(String styling) {
        this.styling = styling;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TextBlock user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextBlock)) {
            return false;
        }
        return id != null && id.equals(((TextBlock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TextBlock{" +
            "id=" + getId() +
            ", textType='" + getTextType() + "'" +
            ", body='" + getBody() + "'" +
            ", styling='" + getStyling() + "'" +
            "}";
    }
}
