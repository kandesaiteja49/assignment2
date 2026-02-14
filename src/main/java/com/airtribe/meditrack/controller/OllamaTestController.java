package com.airtribe.meditrack.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaTestController {

    private final ChatClient ollamaChatClient;

    // We use the constructor to inject the specific "ollamaChatClient" bean
    public OllamaTestController(@Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    @GetMapping("/ai-suggest-specialist")
    public String testOllama(@RequestParam(defaultValue = "Hello, who are you?") String symptoms) {
        // Use the pre-injected client directly

        String systemPrompt = """
You are a medical triage assistant.
Based on the user symptoms, suggest ONLY ONE doctor specialist type even multiple if required.
and also suggest the most likely diagnosis based on the symptoms.

Return ONLY one value from this list:
CARDIOLOGIST, DERMATOLOGIST, NEUROLOGIST, PEDIATRICIAN, ORTHOPEDIC,
GYNECOLOGIST, PSYCHIATRIST, ENDOCRINOLOGIST, ONCOLOGIST,
GASTROENTEROLOGIST, PULMONOLOGIST, RHEUMATOLOGIST, UROLOGIST,
HEMATOLOGIST, INFECTIOUS_DISEASE_SPECIALIST, ALLERGIST,
IMMUNOLOGIST, NEPHROLOGIST, OTOLARYNGOLOGIST, OPHTHALMOLOGIST,
DENTIST, GENERAL_PRACTITIONER

Return the enum word. and explain your reasoning in 4 line paragraph.
""";

        return ollamaChatClient.prompt(systemPrompt)
                .user(symptoms)
                .call()
                .content();
    }
}