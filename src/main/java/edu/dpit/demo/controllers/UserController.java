package edu.dpit.demo.controllers;

import edu.dpit.demo.models.UsersBE;
import edu.dpit.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UsersBE> allUsers() {

        return userService.findAll();
    }

}
