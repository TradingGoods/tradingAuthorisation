package com.exchangeify.authorisation.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.exchangeify.authorisation.model.UserEntity;
import com.exchangeify.authorisation.security.CustomUserDetailService;
import com.exchangeify.authorisation.service.authService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {    
        @Autowired
        private JwtUtil jwtUtil;
    
        @Autowired
        private CustomUserDetailService customUserDetailService;
    
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws ServletException, IOException {
            final String authorizationHeader = request.getHeader("Authorization");
    
            String username = null;
            String jwt = null;
    
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.getUsernameFromToken(jwt);
            }
    
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.customUserDetailService.loadUserByUsername(username);
                if (null != jwt && jwtUtil.validateToken(jwt, userDetails.getUsername(), userDetails.getPassword())) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            chain.doFilter(request, response);
        }
}