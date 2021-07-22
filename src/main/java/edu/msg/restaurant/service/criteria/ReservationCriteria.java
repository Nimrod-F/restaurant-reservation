package edu.msg.restaurant.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link edu.msg.restaurant.domain.Reservation} entity. This class is used
 * in {@link edu.msg.restaurant.web.rest.ReservationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reservations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReservationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter scrumiera;

    private IntegerFilter nrPers;

    private InstantFilter startDatetime;

    private InstantFilter endDateime;

    private LongFilter meseId;

    public ReservationCriteria() {}

    public ReservationCriteria(ReservationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.scrumiera = other.scrumiera == null ? null : other.scrumiera.copy();
        this.nrPers = other.nrPers == null ? null : other.nrPers.copy();
        this.startDatetime = other.startDatetime == null ? null : other.startDatetime.copy();
        this.endDateime = other.endDateime == null ? null : other.endDateime.copy();
        this.meseId = other.meseId == null ? null : other.meseId.copy();
    }

    @Override
    public ReservationCriteria copy() {
        return new ReservationCriteria(this);
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

    public BooleanFilter getScrumiera() {
        return scrumiera;
    }

    public BooleanFilter scrumiera() {
        if (scrumiera == null) {
            scrumiera = new BooleanFilter();
        }
        return scrumiera;
    }

    public void setScrumiera(BooleanFilter scrumiera) {
        this.scrumiera = scrumiera;
    }

    public IntegerFilter getNrPers() {
        return nrPers;
    }

    public IntegerFilter nrPers() {
        if (nrPers == null) {
            nrPers = new IntegerFilter();
        }
        return nrPers;
    }

    public void setNrPers(IntegerFilter nrPers) {
        this.nrPers = nrPers;
    }

    public InstantFilter getStartDatetime() {
        return startDatetime;
    }

    public InstantFilter startDatetime() {
        if (startDatetime == null) {
            startDatetime = new InstantFilter();
        }
        return startDatetime;
    }

    public void setStartDatetime(InstantFilter startDatetime) {
        this.startDatetime = startDatetime;
    }

    public InstantFilter getEndDateime() {
        return endDateime;
    }

    public InstantFilter endDateime() {
        if (endDateime == null) {
            endDateime = new InstantFilter();
        }
        return endDateime;
    }

    public void setEndDateime(InstantFilter endDateime) {
        this.endDateime = endDateime;
    }

    public LongFilter getMeseId() {
        return meseId;
    }

    public LongFilter meseId() {
        if (meseId == null) {
            meseId = new LongFilter();
        }
        return meseId;
    }

    public void setMeseId(LongFilter meseId) {
        this.meseId = meseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationCriteria that = (ReservationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(scrumiera, that.scrumiera) &&
            Objects.equals(nrPers, that.nrPers) &&
            Objects.equals(startDatetime, that.startDatetime) &&
            Objects.equals(endDateime, that.endDateime) &&
            Objects.equals(meseId, that.meseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scrumiera, nrPers, startDatetime, endDateime, meseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (scrumiera != null ? "scrumiera=" + scrumiera + ", " : "") +
            (nrPers != null ? "nrPers=" + nrPers + ", " : "") +
            (startDatetime != null ? "startDatetime=" + startDatetime + ", " : "") +
            (endDateime != null ? "endDateime=" + endDateime + ", " : "") +
            (meseId != null ? "meseId=" + meseId + ", " : "") +
            "}";
    }
}
