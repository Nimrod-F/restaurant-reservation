package edu.dpit.demo.repositories;

import edu.dpit.demo.models.UsersBE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UsersBE, Integer> {

}
