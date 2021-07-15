package edu.dpit.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "mese")
@Getter
@Setter
public class MeseBE {

    @Id
    @Column(name = "cod_M")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codM;

    @Column(name = "nr_loc")
    private Integer nrLoc;
    private Boolean outdoor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeseBE meseBE = (MeseBE) o;
        return Objects.equals(codM, meseBE.codM) &&
                Objects.equals(nrLoc, meseBE.nrLoc) &&
                Objects.equals(outdoor, meseBE.outdoor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codM, nrLoc, outdoor);
    }
}
