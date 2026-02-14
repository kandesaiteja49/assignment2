package com.airtribe.meditrack.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OllamaHealthCheckController {

    private final ChatModel ollamaChatModel;
    private final EmbeddingModel ollamaEmbeddingModel;

    // Use Constructor Injection with @Qualifier to specify Ollama beans
    public OllamaHealthCheckController(
            @Qualifier("ollamaChatModel") ChatModel chatModel,
            @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        this.ollamaChatModel = chatModel;
        this.ollamaEmbeddingModel = embeddingModel;
    }

    @GetMapping("/test-ollama-health")
    public ResponseEntity<Map<String, Object>> testOllamaHealth() {
        Map<String, Object> response = new HashMap<>();

        // 1. Test Chat Model
        try {
            // Updated call method for newer Spring AI versions
            String chatResponse = ollamaChatModel.call("Say 'Hello'");
            response.put("chatModelStatus", "Working ✅");
            response.put("chatResponse", chatResponse);
        } catch (Exception e) {
            response.put("chatModelStatus", "Failed ❌");
            response.put("chatError", e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        // 2. Test Embedding Model
        try {
            float[] embedding = ollamaEmbeddingModel.embed("test");
            response.put("embeddingModelStatus", "Working ✅");
            response.put("embeddingSize", embedding.length);
        } catch (Exception e) {
            response.put("embeddingModelStatus", "Failed ❌");
            response.put("embeddingError", e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}