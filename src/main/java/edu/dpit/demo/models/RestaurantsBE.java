package edu.dpit.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
public class RestaurantsBE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_RS")
    private Long codRs;

    private String location;

    private String name;

    private String description;

    @Lob
    private byte[] image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantsBE that = (RestaurantsBE) o;
        return Objects.equals(codRs, that.codRs) &&
                Objects.equals(location, that.location) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Arrays.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(codRs, location, name, description);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}
