package com.example.springbootpetproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringBootPetProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPetProjectApplication.class, args);
	}

}
