package com.exchangeify.authorisation.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface OAuth2Service {

    ResponseEntity<?> authenticateRequest(OAuth2AuthenticationToken authentication);
    
}
