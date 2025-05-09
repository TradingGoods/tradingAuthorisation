package com.exchangeify.authorisation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletException;

@RestController
@RequestMapping("/api/test")
public class testController {
    
    @GetMapping("/hello")
    public String hello() throws ServletException {
        return "Hello, authenticated user!";
    }
}
