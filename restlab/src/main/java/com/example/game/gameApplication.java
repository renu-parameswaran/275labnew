package com.example.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class gameApplication {

	public static void main(String[] args) {
		SpringApplication.run(gameApplication.class, args);
	}
}
