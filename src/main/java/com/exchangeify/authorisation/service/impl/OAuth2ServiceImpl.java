package com.exchangeify.authorisation.service.impl;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.exchangeify.authorisation.model.AuthenticationResponse;
import com.exchangeify.authorisation.model.User;
import com.exchangeify.authorisation.repository.userRepository;
import com.exchangeify.authorisation.service.OAuth2Service;
import com.exchangeify.authorisation.utils.JwtUtil;

import org.apache.commons.lang3.StringUtils;

@Service
public class OAuth2ServiceImpl implements OAuth2Service{
    
        @Autowired
        private userRepository userRepository;

        @Autowired
        private JwtUtil jwtUtil;

        @Override
        public ResponseEntity<?> authenticateRequest(OAuth2AuthenticationToken authentication) {
            String email = authentication.getPrincipal().getAttribute("email");
            Optional<User> userOptional = userRepository.findByEmail(email);

            // if (!userOptional.isPresent()) {
            //     // Register new user if necessary
            //     User newUser = new User();
            //     newUser.setEmail(email);
            //     newUser.setUsername(authentication.getPrincipal().getAttribute("name"));
            //     newUser.setAuthProvider("GOOGLE");
            //     userRepository.save(newUser);
            // }

            // Issue JWT Token
            return ResponseEntity.ok(this.createAuthenticationToken(email, ""));
        }

        public AuthenticationResponse createAuthenticationToken(String username, String password) {
            // Logic to authenticate user and generate JWT
            String jwtToken = null;
            if(StringUtils.isNotBlank(password)){
                jwtToken = jwtUtil.generateToken(username);
            }
            jwtToken = jwtUtil.generateToken(username);

            return new AuthenticationResponse(jwtToken);
        }


}
