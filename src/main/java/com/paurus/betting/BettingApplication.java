package com.paurus.betting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BettingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BettingApplication.class, args);
	}

}
