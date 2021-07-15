package edu.dpit.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="users")
@Getter
@Setter
public class UsersBE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_U")
    private Integer codU;

    private String email;
    @Column(name = "nume_prenume")
    private String numePrenume;
    private String telefon;
    private String username;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersBE usersBE = (UsersBE) o;
        return Objects.equals(codU, usersBE.codU) &&
                Objects.equals(email, usersBE.email) &&
                Objects.equals(numePrenume, usersBE.numePrenume) &&
                Objects.equals(telefon, usersBE.telefon) &&
                Objects.equals(username, usersBE.username) &&
                Objects.equals(password, usersBE.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codU, email, numePrenume, telefon, username, password);
    }
}
