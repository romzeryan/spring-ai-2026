package com.roman.learn;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiApplication_06 {

  public static void main(String[] args) {
    SpringApplication.run(SpringAiApplication_06.class, args);
  }

  @Bean
  public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
    return ChatClient.create(ollamaChatModel);
  }

  @Bean
  CommandLineRunner cli(ChatClient ollamaChatClient) {
    return args -> {
      String response = ollamaChatClient.prompt("How are you today?").call().content();

      System.out.println("Response: " + response);
    };
  }

}
