package com.exchangeify.authorisation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeify.authorisation.dto.JwtResponseDTO;
import com.exchangeify.authorisation.dto.LoginRequestDTO;
import com.exchangeify.authorisation.service.authService;
import com.exchangeify.authorisation.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class OAuth2Controller {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);



    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private authService myAuthService; 

    @Autowired
    private AuthenticationManager authenticationManager;

   @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("credential");
        
        try {
            Claims claims = jwtUtil.verifyToken(token);
            String email = claims.get("email", String.class);
            String name = claims.get("name", String.class);
            String picture = claims.get("picture",String.class);
            Map<String, String> response = new HashMap<>();
            response.put("name", name);
            response.put("email", email);
            response.put("picture", picture);
            response.put("auth_provider","google");

            return ResponseEntity.ok(response);
            // Process the claims as needed
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        // Authentication logic here
        logger.info("Login request received for email: " + request.getEmailId());
        logger.info("Login request received for password: " + request.getPassword());
        logger.info("Login request received for request: " + request);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmailId(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponseDTO(jwt));
    }
}

