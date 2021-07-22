package edu.msg.restaurant.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link edu.msg.restaurant.domain.Reservation} entity.
 */
public class ReservationDTO implements Serializable {

    private Long id;

    private Boolean scrumiera;

    private Integer nrPers;

    private Instant startDatetime;

    private Instant endDateime;

    private Set<MeseDTO> mese = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getScrumiera() {
        return scrumiera;
    }

    public void setScrumiera(Boolean scrumiera) {
        this.scrumiera = scrumiera;
    }

    public Integer getNrPers() {
        return nrPers;
    }

    public void setNrPers(Integer nrPers) {
        this.nrPers = nrPers;
    }

    public Instant getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Instant getEndDateime() {
        return endDateime;
    }

    public void setEndDateime(Instant endDateime) {
        this.endDateime = endDateime;
    }

    public Set<MeseDTO> getMese() {
        return mese;
    }

    public void setMese(Set<MeseDTO> mese) {
        this.mese = mese;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationDTO)) {
            return false;
        }

        ReservationDTO reservationDTO = (ReservationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reservationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationDTO{" +
            "id=" + getId() +
            ", scrumiera='" + getScrumiera() + "'" +
            ", nrPers=" + getNrPers() +
            ", startDatetime='" + getStartDatetime() + "'" +
            ", endDateime='" + getEndDateime() + "'" +
            ", mese=" + getMese() +
            "}";
    }
}
