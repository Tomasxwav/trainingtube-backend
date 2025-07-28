package com.traini.traini_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TrainiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainiBackendApplication.class, args);
	}

}
