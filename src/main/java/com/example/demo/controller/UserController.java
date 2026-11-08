package com.example.demo.controller;

import com.example.demo.Entity.User;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> listerUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User afficherUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User ajouterUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public User modifierUser(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerUser(@PathVariable Long id) {
        userService.delete(id);
    }
}