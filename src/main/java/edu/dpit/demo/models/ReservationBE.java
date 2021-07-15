package edu.dpit.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class ReservationBE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_RZ")
    private Long codRz;

    private Boolean scrumiera;

    @Column(name = "nr_pers")
    private Integer nrPers;

    @OneToOne
    @JoinColumn(name = "cod_U", referencedColumnName = "cod_U")
    private UsersBE user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationBE that = (ReservationBE) o;
        return Objects.equals(codRz, that.codRz) &&
                Objects.equals(scrumiera, that.scrumiera) &&
                Objects.equals(nrPers, that.nrPers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codRz, scrumiera, nrPers);
    }
}
