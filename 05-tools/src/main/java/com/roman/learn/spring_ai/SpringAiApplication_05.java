package com.roman.learn.spring_ai;

import com.roman.learn.common.MyLoggingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiApplication_05 {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication_05.class, args);
    }

    @Bean
    public CommandLineRunner cli(ChatClient.Builder chatClientBuilder) {
        return args -> {

            ChatClient chatClient = chatClientBuilder
                    .defaultAdvisors(MyLoggingAdvisor.builder().build())
                    .defaultTools(new LocationServerTool())
                    .build();

            String content = chatClient.prompt("Find geodata of the center of Hilversum, Netherlands.")
                    .advisors(ToolCallAdvisor.builder().build())
                    .call()
                    .content();

            System.out.println("Answer: \n" + content);
        };
    }

}
