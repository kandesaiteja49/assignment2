package com.airtribe.meditrack.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AiConfig {

    /**
     * This manually defines the Builder that your AiController is looking for.
     * We use the OpenAiChatModel (Groq) as the default engine for this builder.
     */
    @Bean
    public ChatClient.Builder chatClientBuilder(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel);
    }

    // Bean for Groq (Cloud)
    @Bean
    @Primary
    public ChatClient groqChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    // Bean for Ollama (Local)
    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel).build();
    }


}