package edu.dpit.demo.services;

import edu.dpit.demo.models.UsersBE;
import edu.dpit.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UsersBE> findAll() {

        return (List<UsersBE>) userRepository.findAll();
    }

    public UsersBE save(UsersBE saveUser) {
        return findUserByEmail(saveUser)? null: userRepository.save(saveUser);
    }

    private boolean findUserByEmail(UsersBE saveUser) {
        Optional<UsersBE> checkIfExist = userRepository.findByEmail(saveUser.getEmail());
        return checkIfExist.isPresent();
    }

    public void delete(UsersBE user){
        if (findUserByEmail(user)){
            userRepository.delete(user);
        }
    }

    public UsersBE update(UsersBE user){
        return findUserByEmail(user)? userRepository.save(user): null;

    }
}
