package com.roman.learn.spring_ai;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LocationServerTool {

    RestClient restClient = RestClient.create();

    @Tool(name = "locationTool", description = "Get location adres geodata of the object or neighbourhood")
    public String getLocation(
            @ToolParam(description = "query from user request") String q
    ) {

        return restClient
                .get()
                .uri("https://api.pdok.nl/bzk/locatieserver/search/v3_1/free?q={query}&bq=type:adres", q)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
    }
}
