package edu.msg.restaurant.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link edu.msg.restaurant.domain.Mese} entity. This class is used
 * in {@link edu.msg.restaurant.web.rest.MeseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mese?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MeseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter nrLoc;

    private BooleanFilter outdoor;

    private LongFilter restaurantsId;

    private LongFilter reservationId;

    public MeseCriteria() {}

    public MeseCriteria(MeseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nrLoc = other.nrLoc == null ? null : other.nrLoc.copy();
        this.outdoor = other.outdoor == null ? null : other.outdoor.copy();
        this.restaurantsId = other.restaurantsId == null ? null : other.restaurantsId.copy();
        this.reservationId = other.reservationId == null ? null : other.reservationId.copy();
    }

    @Override
    public MeseCriteria copy() {
        return new MeseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNrLoc() {
        return nrLoc;
    }

    public IntegerFilter nrLoc() {
        if (nrLoc == null) {
            nrLoc = new IntegerFilter();
        }
        return nrLoc;
    }

    public void setNrLoc(IntegerFilter nrLoc) {
        this.nrLoc = nrLoc;
    }

    public BooleanFilter getOutdoor() {
        return outdoor;
    }

    public BooleanFilter outdoor() {
        if (outdoor == null) {
            outdoor = new BooleanFilter();
        }
        return outdoor;
    }

    public void setOutdoor(BooleanFilter outdoor) {
        this.outdoor = outdoor;
    }

    public LongFilter getRestaurantsId() {
        return restaurantsId;
    }

    public LongFilter restaurantsId() {
        if (restaurantsId == null) {
            restaurantsId = new LongFilter();
        }
        return restaurantsId;
    }

    public void setRestaurantsId(LongFilter restaurantsId) {
        this.restaurantsId = restaurantsId;
    }

    public LongFilter getReservationId() {
        return reservationId;
    }

    public LongFilter reservationId() {
        if (reservationId == null) {
            reservationId = new LongFilter();
        }
        return reservationId;
    }

    public void setReservationId(LongFilter reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MeseCriteria that = (MeseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nrLoc, that.nrLoc) &&
            Objects.equals(outdoor, that.outdoor) &&
            Objects.equals(restaurantsId, that.restaurantsId) &&
            Objects.equals(reservationId, that.reservationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nrLoc, outdoor, restaurantsId, reservationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nrLoc != null ? "nrLoc=" + nrLoc + ", " : "") +
            (outdoor != null ? "outdoor=" + outdoor + ", " : "") +
            (restaurantsId != null ? "restaurantsId=" + restaurantsId + ", " : "") +
            (reservationId != null ? "reservationId=" + reservationId + ", " : "") +
            "}";
    }
}
