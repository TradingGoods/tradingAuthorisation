package com.exchangeify.authorisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.exchangeify.authorisation")
public class AuthorisationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorisationApplication.class, args);
	}

}
