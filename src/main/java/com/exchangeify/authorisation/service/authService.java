package com.exchangeify.authorisation.service;

import java.util.List;


import com.exchangeify.authorisation.model.User;

public interface authService {
    

    public User save(User entity);

    public List<User> findAll();
    public User findById(String id);
    public void deleteById(String id);
}
