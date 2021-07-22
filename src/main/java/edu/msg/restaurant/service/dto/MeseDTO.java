package edu.msg.restaurant.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link edu.msg.restaurant.domain.Mese} entity.
 */
public class MeseDTO implements Serializable {

    private Long id;

    private Integer nrLoc;

    private Boolean outdoor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNrLoc() {
        return nrLoc;
    }

    public void setNrLoc(Integer nrLoc) {
        this.nrLoc = nrLoc;
    }

    public Boolean getOutdoor() {
        return outdoor;
    }

    public void setOutdoor(Boolean outdoor) {
        this.outdoor = outdoor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeseDTO)) {
            return false;
        }

        MeseDTO meseDTO = (MeseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, meseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeseDTO{" +
            "id=" + getId() +
            ", nrLoc=" + getNrLoc() +
            ", outdoor='" + getOutdoor() + "'" +
            "}";
    }
}
