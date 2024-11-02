package com.exchangeify.authorisation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeify.authorisation.model.User;
import com.exchangeify.authorisation.service.authService;

@RestController
@RequestMapping("/user")
public class authController {

    @Autowired
    private authService myAuthService; 

    @PostMapping
    public User createEntity(@RequestBody User entity) {
        return myAuthService.save(entity);
    }

    @GetMapping
    public List<User> getAllEntities() {
        return myAuthService.findAll();
    }

    @GetMapping("/{id}")
    public User getEntityById(@PathVariable String id) {
        return myAuthService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEntityById(@PathVariable String id) {
        myAuthService.deleteById(id);
    }
}
