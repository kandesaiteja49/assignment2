package com.airtribe.meditrack.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    private final ChatClient chatClient;

    // Spring AI autoconfigures a ChatClient.Builder for you
    public AiController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
