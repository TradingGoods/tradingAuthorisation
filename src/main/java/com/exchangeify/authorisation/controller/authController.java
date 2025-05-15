package com.exchangeify.authorisation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeify.authorisation.dto.RegisterDTO;
import com.exchangeify.authorisation.service.authService;

@RestController
@RequestMapping("/api/auth")
public class authController {

    @Autowired
    private authService myAuthService; 

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO userInput) {
        ResponseEntity<String> response = myAuthService.registerUser(userInput);
        
        return response;
        
    }
}
