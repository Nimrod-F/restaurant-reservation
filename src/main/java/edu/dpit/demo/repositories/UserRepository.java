package edu.dpit.demo.repositories;

import edu.dpit.demo.models.UsersBE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UsersBE, Long> {
    Optional<UsersBE> findByEmail(String email);
}
