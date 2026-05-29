package com.roman.learn.spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
public class SpringAiApplication_02 {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiApplication_02.class, args);
	}

	@Bean
	public CommandLineRunner cli(ChatClient.Builder chatClientBuilder) {
		return args -> {
			ChatClient chatClient = chatClientBuilder
					.defaultAdvisors()
					.build();

			record F1Winners(Map<Integer, String> yearToNameMap) {}
			F1Winners answer = chatClient.prompt()
					.system("You are a general knowledge assistant. Answer concise. Don't assume")
					.user("List the F1 winners for each year since 2010. Return only the year and the name of the winner.")
					.call()
					.entity(F1Winners.class);

			System.out.println("Answer: \n" + answer);
		};
	}
}
