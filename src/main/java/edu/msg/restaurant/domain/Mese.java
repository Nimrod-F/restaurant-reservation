package edu.msg.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Mese.
 */
@Entity
@Table(name = "mese")
public class Mese implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nr_loc")
    private Integer nrLoc;

    @Column(name = "outdoor")
    private Boolean outdoor;

    @OneToMany(mappedBy = "mese")
    @JsonIgnoreProperties(value = { "mese" }, allowSetters = true)
    private Set<Restaurants> restaurants = new HashSet<>();

    @ManyToMany(mappedBy = "mese")
    @JsonIgnoreProperties(value = { "mese" }, allowSetters = true)
    private Set<Reservation> reservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mese id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getNrLoc() {
        return this.nrLoc;
    }

    public Mese nrLoc(Integer nrLoc) {
        this.nrLoc = nrLoc;
        return this;
    }

    public void setNrLoc(Integer nrLoc) {
        this.nrLoc = nrLoc;
    }

    public Boolean getOutdoor() {
        return this.outdoor;
    }

    public Mese outdoor(Boolean outdoor) {
        this.outdoor = outdoor;
        return this;
    }

    public void setOutdoor(Boolean outdoor) {
        this.outdoor = outdoor;
    }

    public Set<Restaurants> getRestaurants() {
        return this.restaurants;
    }

    public Mese restaurants(Set<Restaurants> restaurants) {
        this.setRestaurants(restaurants);
        return this;
    }

    public Mese addRestaurants(Restaurants restaurants) {
        this.restaurants.add(restaurants);
        restaurants.setMese(this);
        return this;
    }

    public Mese removeRestaurants(Restaurants restaurants) {
        this.restaurants.remove(restaurants);
        restaurants.setMese(null);
        return this;
    }

    public void setRestaurants(Set<Restaurants> restaurants) {
        if (this.restaurants != null) {
            this.restaurants.forEach(i -> i.setMese(null));
        }
        if (restaurants != null) {
            restaurants.forEach(i -> i.setMese(this));
        }
        this.restaurants = restaurants;
    }

    public Set<Reservation> getReservations() {
        return this.reservations;
    }

    public Mese reservations(Set<Reservation> reservations) {
        this.setReservations(reservations);
        return this;
    }

    public Mese addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.getMese().add(this);
        return this;
    }

    public Mese removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.getMese().remove(this);
        return this;
    }

    public void setReservations(Set<Reservation> reservations) {
        if (this.reservations != null) {
            this.reservations.forEach(i -> i.removeMese(this));
        }
        if (reservations != null) {
            reservations.forEach(i -> i.addMese(this));
        }
        this.reservations = reservations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mese)) {
            return false;
        }
        return id != null && id.equals(((Mese) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mese{" +
            "id=" + getId() +
            ", nrLoc=" + getNrLoc() +
            ", outdoor='" + getOutdoor() + "'" +
            "}";
    }
}
