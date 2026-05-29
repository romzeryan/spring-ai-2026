package com.roman.learn.spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiApplication_01 {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiApplication_01.class, args);
	}

	@Bean
	public CommandLineRunner cli(ChatClient.Builder chatClientBuilder) {
		return args -> {
			ChatClient chatClient = chatClientBuilder
					.defaultAdvisors()
					.build();

			String answer = chatClient.prompt()
					.system("You are a general knowledge assistant. Answer concise. Don't assume")
					.user("tell me a joke")
					.call()
					.content();

			System.out.println("Answer: \n" + answer);
		};
	}
}
