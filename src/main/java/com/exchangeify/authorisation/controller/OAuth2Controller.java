package com.exchangeify.authorisation.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeify.authorisation.model.User;
import com.exchangeify.authorisation.repository.userRepository;
import com.exchangeify.authorisation.service.OAuth2Service;
import com.exchangeify.authorisation.utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class OAuth2Controller {



    @Autowired
    private OAuth2Service OAuth2Service;

    @GetMapping("/google-login")
    public ResponseEntity<?> googleLogin(OAuth2AuthenticationToken authentication) {

        return this.OAuth2Service.authenticateRequest(authentication);
    }
}

