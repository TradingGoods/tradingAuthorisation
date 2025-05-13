package com.exchangeify.authorisation.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exchangeify.authorisation.dto.RegisterDTO;
import com.exchangeify.authorisation.model.RoleEntity;
import com.exchangeify.authorisation.model.UserEntity;
import com.exchangeify.authorisation.repository.RoleRepository;
import com.exchangeify.authorisation.repository.UserRepository;
import com.exchangeify.authorisation.service.authService;

@Service
public class authServiceImpl implements authService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> registerUser(RegisterDTO userInput){
        if(userRepository.existsByEmailId(userInput.getEmailId())){
            return new ResponseEntity<>("User already exist",HttpStatus.BAD_REQUEST);
        }
        try{
            Date currentDate = new Date();
            UserEntity user = new UserEntity();
            user.setEmailId(userInput.getEmailId());
            user.setCreatedDate(currentDate.toString());
            user.setAuthProvider("manual");
            user.setFirstName(userInput.getFirstName());
            user.setLastName(userInput.getLastName());
            user.setIsValid("false");
            user.setPhoneNumber(userInput.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(userInput.getPassword()));

            RoleEntity roles = roleRepository.findByName("USER").get();
            user.setRoles(Collections.singletonList(roles));
            userRepository.save(user);
            return new ResponseEntity<>("user registered", HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>("Error in registration",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
