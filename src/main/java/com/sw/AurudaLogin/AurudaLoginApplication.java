package com.sw.AurudaLogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication

public class AurudaLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(AurudaLoginApplication.class, args);
	}

}
