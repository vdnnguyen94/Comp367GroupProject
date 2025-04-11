package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Comp367Project {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		String port = dotenv.get("APP_PORT", "5173");
		String stage = dotenv.get("STAGE", "NOT LOADED");
		System.out.println("Current Stage: " + stage);
		System.setProperty("server.port", port);
		SpringApplication.run(Comp367Project.class, args);
		System.out.println("Spring Boot WEB running on PORT " + port);
	}

}
