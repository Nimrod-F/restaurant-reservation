package edu.dpit.demo.controllers;

import edu.dpit.demo.models.UsersBE;
import edu.dpit.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UsersBE> allUsers() {

        return userService.findAll();
    }

    @PostMapping("/user")
    public UsersBE saveUser(@RequestBody UsersBE user) {
        return userService.save(user);
    }

    @DeleteMapping("/user/delete")
    public void deleteUser(@RequestBody UsersBE user) {
        userService.delete(user);
    }

    @PutMapping("/user/{id}")
    public UsersBE updateUser(@RequestBody UsersBE user, @PathVariable("id") Long id) {
        user.setCodU(id);
        return userService.update(user);
    }

}
