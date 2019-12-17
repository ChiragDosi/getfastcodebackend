package com.nfinity.example179;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Example179Application {

	public static void main(String[] args) {
		SpringApplication.run(Example179Application.class, args);
	}

}

