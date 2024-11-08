package com.exchangeify.authorisation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exchangeify.authorisation.model.User;
import com.exchangeify.authorisation.repository.userRepository;
import com.exchangeify.authorisation.service.authService;

@Service
public class authServiceImpl implements authService{

    @Autowired
    private userRepository myUserRepository;

    @Override
    public User save(User entity) {
        User savedUser =  myUserRepository.save(entity);
        return savedUser;
    }

    @Override
    public List<User> findAll() {
        return myUserRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return myUserRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(String id) {
        myUserRepository.deleteById(id);
    }
    
}
