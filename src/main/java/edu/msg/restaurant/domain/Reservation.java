package edu.msg.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scrumiera")
    private Boolean scrumiera;

    @Column(name = "nr_pers")
    private Integer nrPers;

    @Column(name = "start_datetime")
    private Instant startDatetime;

    @Column(name = "end_dateime")
    private Instant endDateime;

    @ManyToMany
    @JoinTable(
        name = "rel_reservation__mese",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "mese_id")
    )
    @JsonIgnoreProperties(value = { "restaurants", "reservations" }, allowSetters = true)
    private Set<Mese> mese = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getScrumiera() {
        return this.scrumiera;
    }

    public Reservation scrumiera(Boolean scrumiera) {
        this.scrumiera = scrumiera;
        return this;
    }

    public void setScrumiera(Boolean scrumiera) {
        this.scrumiera = scrumiera;
    }

    public Integer getNrPers() {
        return this.nrPers;
    }

    public Reservation nrPers(Integer nrPers) {
        this.nrPers = nrPers;
        return this;
    }

    public void setNrPers(Integer nrPers) {
        this.nrPers = nrPers;
    }

    public Instant getStartDatetime() {
        return this.startDatetime;
    }

    public Reservation startDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
        return this;
    }

    public void setStartDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Instant getEndDateime() {
        return this.endDateime;
    }

    public Reservation endDateime(Instant endDateime) {
        this.endDateime = endDateime;
        return this;
    }

    public void setEndDateime(Instant endDateime) {
        this.endDateime = endDateime;
    }

    public Set<Mese> getMese() {
        return this.mese;
    }

    public Reservation mese(Set<Mese> mese) {
        this.setMese(mese);
        return this;
    }

    public Reservation addMese(Mese mese) {
        this.mese.add(mese);
        mese.getReservations().add(this);
        return this;
    }

    public Reservation removeMese(Mese mese) {
        this.mese.remove(mese);
        mese.getReservations().remove(this);
        return this;
    }

    public void setMese(Set<Mese> mese) {
        this.mese = mese;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", scrumiera='" + getScrumiera() + "'" +
            ", nrPers=" + getNrPers() +
            ", startDatetime='" + getStartDatetime() + "'" +
            ", endDateime='" + getEndDateime() + "'" +
            "}";
    }
}
