package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.entities.Doctor;
import com.airtribe.meditrack.enums.Specialist;
import com.airtribe.meditrack.repositories.DoctorRepo;
import com.airtribe.meditrack.services.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.ai.ollama.api.OllamaOptions.*;

@RestController
@RequestMapping("/smart")
public class SmartAppointmentController {

    private final ChatClient ollamaChatClient;
    private final DoctorRepo doctorRepo;
    private final SlotService slotService;

    public SmartAppointmentController(@Qualifier("ollamaChatClient")  ChatClient ollamaChatClient, DoctorRepo doctorRepo, SlotService slotService) {
        this.ollamaChatClient = ollamaChatClient;
        this.doctorRepo = doctorRepo;
        this.slotService = slotService;
    }

    @GetMapping("/suggest-slots")
    public Map<String, Object> suggestSlots(
            @RequestParam String symptoms,
            @RequestParam LocalDate date
    ) {

        String systemPrompt = """
You are a medical triage classifier.

Return ONLY ONE WORD.
Return ONLY the ENUM value exactly as written.
ALL CAPS.
NO explanation.
NO sentence.
NO punctuation.

Valid values:
CARDIOLOGIST, DERMATOLOGIST, NEUROLOGIST, PEDIATRICIAN,
ORTHOPEDIC, GYNECOLOGIST, PSYCHIATRIST, ENDOCRINOLOGIST,
ONCOLOGIST, GASTROENTEROLOGIST, PULMONOLOGIST,
RHEUMATOLOGIST, UROLOGIST, HEMATOLOGIST,
INFECTIOUS_DISEASE_SPECIALIST, ALLERGIST,
IMMUNOLOGIST, NEPHROLOGIST, OTOLARYNGOLOGIST,
OPHTHALMOLOGIST, DENTIST, GENERAL_PRACTITIONER

Strictly follow instructions.
One word only.
Return ONLY ONE WORD from the list.
Example input: "I have chest pain and shortness of breath"
Example output: "CARDIOLOGIST"
example input: "I have a skin rash and itching"
example output: "DERMATOLOGIST"
example input: "I have frequent headaches and dizziness"
example output: "NEUROLOGIST"
example input: "My child has a fever and cough"
example output: "PEDIATRICIAN"
""";

        // ---- AI Specialist Detection ----
        String specialistStr = ollamaChatClient.prompt(systemPrompt)
                .user(symptoms)
                .options(OllamaOptions.builder()
                        .temperature(0.0)
                        .build()) // Deterministic output
                .call()
                .content()
                .trim();

        Specialist specialist = Specialist.valueOf(specialistStr);

        // ---- Find Doctors ----
        List<Doctor> doctors =
                doctorRepo.findBySpecialistAndIsAvailableTrue(specialist);

        // ---- Build Response ----
        List<Map<String,Object>> result = new ArrayList<>();

        for (Doctor d : doctors) {

            List<LocalTime> slots =
                    slotService.getAvailableSlots(d.getId(), date);

            Map<String,Object> docInfo = new HashMap<>();
            docInfo.put("doctorId", d.getId());
            docInfo.put("doctorName", d.getName());
            docInfo.put("specialist", d.getSpecialist());
            docInfo.put("availableSlots", slots);

            result.add(docInfo);
        }

        return Map.of(
                "specialist", specialist,
                "doctors", result
        );
    }
}

