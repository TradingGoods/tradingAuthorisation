package com.exchangeify.authorisation.service;

import org.springframework.http.ResponseEntity;

import com.exchangeify.authorisation.dto.RegisterDTO;

public interface authService {
    

   public ResponseEntity<String> registerUser(RegisterDTO userDetails);
}
