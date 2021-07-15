package edu.dpit.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rezervare-mese")
@Getter
@Setter
@IdClass(RezervareMesePK.class)
public class RezervareMeseBE {

    @Id
    @Column(name = "cod_RZ")
    private Long codRz;

    @Id
    @Column(name = "cod_M")
    private Long codM;

    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

//    @ManyToOne
//    @JoinColumn(name = "cod_RZ", referencedColumnName = "cod_RZ")
//    private ReservationBE reservation;
//
//    @ManyToOne
//    @JoinColumn(name = "cod_M", referencedColumnName = "cod_M")
//    private MeseBE masa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RezervareMeseBE that = (RezervareMeseBE) o;
        return Objects.equals(codM, that.codM) && Objects.equals(codRz, that.codRz) &&
                Objects.equals(startDatetime, that.startDatetime) &&
                Objects.equals(endDatetime, that.endDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codM, codRz, startDatetime, endDatetime);
    }
}
