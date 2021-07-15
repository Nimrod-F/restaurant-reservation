package edu.dpit.demo.services;

import edu.dpit.demo.models.UsersBE;
import edu.dpit.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UsersBE> findAll() {

        return (List<UsersBE>) userRepository.findAll();
    }
}
