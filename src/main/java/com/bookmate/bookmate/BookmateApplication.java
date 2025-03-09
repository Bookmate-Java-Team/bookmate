package com.bookmate.bookmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmateApplication.class, args);
	}

}
