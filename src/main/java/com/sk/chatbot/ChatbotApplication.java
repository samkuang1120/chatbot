package com.sk.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.sk.chatbot.repository")
public class ChatbotApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChatbotApplication.class, args);
	}

}
