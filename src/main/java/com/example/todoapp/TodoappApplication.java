package com.example.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Main Entry
// including 3 annotation
// 1. @Configuration
// 2. @EnableAutoConfiguration
// 3. @ComponentScane
@SpringBootApplication
public class TodoappApplication {
	// main method: start
	public static void main(String[] args) {
		// Launch spring Boot Application
		SpringApplication.run(TodoappApplication.class, args);
	}
}