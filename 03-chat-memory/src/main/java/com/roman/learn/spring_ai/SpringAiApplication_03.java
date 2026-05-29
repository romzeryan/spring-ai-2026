package com.roman.learn.spring_ai;

import com.roman.learn.common.MyLoggingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiApplication_03 {

	private static final String CONVERSATION_ID = "roman-cli";

	public static void main(String[] args) {
		SpringApplication.run(SpringAiApplication_03.class, args);
	}

	@Bean
	public CommandLineRunner cli(ChatClient.Builder chatClientBuilder) {
		return args -> {
			// Chat memory
			MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
					.maxMessages(10)
					.build();

			ChatClient chatClient = chatClientBuilder
					.defaultAdvisors(MyLoggingAdvisor.builder().build())
					.defaultAdvisors(advisors -> advisors
							.param(ChatMemory.CONVERSATION_ID, CONVERSATION_ID)
							.advisors(MessageChatMemoryAdvisor.builder(chatMemory).build()))
					.build();


			chatClient.prompt("My name is Roman. How are you doing today?")
					.call()
					.content();

			chatClient.prompt("What is my name?")
					.call()
					.content();

		};
	}
}
