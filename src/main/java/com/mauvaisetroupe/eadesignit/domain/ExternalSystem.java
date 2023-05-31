package com.mauvaisetroupe.eadesignit.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExternalSystem.
 */
@Entity
@Table(name = "external_system")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExternalSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "external_system_id")
    private String externalSystemID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExternalSystem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalSystemID() {
        return this.externalSystemID;
    }

    public ExternalSystem externalSystemID(String externalSystemID) {
        this.setExternalSystemID(externalSystemID);
        return this;
    }

    public void setExternalSystemID(String externalSystemID) {
        this.externalSystemID = externalSystemID;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalSystem)) {
            return false;
        }
        return id != null && id.equals(((ExternalSystem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalSystem{" +
            "id=" + getId() +
            ", externalSystemID='" + getExternalSystemID() + "'" +
            "}";
    }
}
