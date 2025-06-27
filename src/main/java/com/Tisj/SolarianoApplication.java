package com.Tisj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SolarianoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SolarianoApplication.class, args);
	}

}
