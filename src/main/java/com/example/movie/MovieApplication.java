package com.example.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


//@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
//로그인페이지 비활성화

@SpringBootApplication
@EnableJpaAuditing
public class MovieApplication {
	public static void main(String[] args) {
		SpringApplication.run(MovieApplication.class, args);
	}

}
