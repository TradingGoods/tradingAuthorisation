package com.exchangeify.authorisation.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangeify.authorisation.service.OAuth2Service;
import com.exchangeify.authorisation.utils.JwtUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/auth")
public class OAuth2Controller {



    @Autowired
    private JwtUtil jwtUtil;

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
}

