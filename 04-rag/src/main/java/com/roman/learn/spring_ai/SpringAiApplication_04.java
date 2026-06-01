package com.roman.learn.spring_ai;

import com.roman.learn.common.MyLoggingAdvisor;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@SpringBootApplication
public class SpringAiApplication_04 {

    @Value("classpath:rag-data")
    Resource resource;

    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication_04.class, args);
    }

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    public CommandLineRunner cli(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        return args -> {

            loadDataToVectorStore(vectorStore);

            ChatClient chatClient = chatClientBuilder
                    .defaultAdvisors(MyLoggingAdvisor.builder().build())
                    .build();

            String content = chatClient.prompt("When are DUO examens for R.Y. Panov?")
                    .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                    .call()
                    .content();

            System.out.println("Answer: \n" + content);
        };
    }

    private void loadDataToVectorStore(VectorStore vectorStore) {
        if (resource.exists()) {
            try {
                List<Document> docs = new ArrayList<>();
                for (File file : requireNonNull(resource.getFile().listFiles())) {
                    System.out.println("Loading file: " + file.getName() + " to vector store");

                    if (file.getName().toLowerCase().endsWith(".pdf")) {
                        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(new FileSystemResource(file));
                        docs.addAll(TokenTextSplitter.builder().build().split(pdfReader.read()));
                    } else {
                        String content = IOUtils.toString(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
                        Document doc = new Document(file.getName(), content, Collections.emptyMap());
                        docs.addAll(TokenTextSplitter.builder().build().split(doc));
                    }
                }
                vectorStore.add(docs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
