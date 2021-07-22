package edu.msg.restaurant.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link edu.msg.restaurant.domain.Restaurants} entity.
 */
public class RestaurantsDTO implements Serializable {

    private Long id;

    private String location;

    private String name;

    private String description;

    @Lob
    private byte[] image;

    private String imageContentType;
    private MeseDTO mese;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public MeseDTO getMese() {
        return mese;
    }

    public void setMese(MeseDTO mese) {
        this.mese = mese;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantsDTO)) {
            return false;
        }

        RestaurantsDTO restaurantsDTO = (RestaurantsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurantsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantsDTO{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", mese=" + getMese() +
            "}";
    }
}
