package edu.dpit.demo.models;

import java.io.Serializable;
import java.util.Objects;


public class RezervareMesePK implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RezervareMesePK)) return false;
        RezervareMesePK that = (RezervareMesePK) o;
        return codRz.equals(that.codRz) &&
                codM.equals(that.codM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codRz, codM);
    }

    public RezervareMesePK() {
    }

    private Long codRz;

    public Long getCodRz() {
        return codRz;
    }

    public void setCodRz(Long codRz) {
        this.codRz = codRz;
    }

    public Long getCodM() {
        return codM;
    }

    public void setCodM(Long codM) {
        this.codM = codM;
    }

    private Long codM;


    public RezervareMesePK(Long codRz, Long codM) {
        this.codRz = codRz;
        this.codM = codM;
    }
}
