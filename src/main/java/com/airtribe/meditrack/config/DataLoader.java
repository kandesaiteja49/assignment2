package com.airtribe.meditrack.config;

import com.airtribe.meditrack.entities.Doctor;
import com.airtribe.meditrack.enums.Role;
import com.airtribe.meditrack.enums.Specialist;
import com.airtribe.meditrack.repositories.PersonRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final EmbeddingModel embeddingModel;

    // Use constructor injection with @Qualifier to avoid the "2 beans found" error
    public DataLoader(PersonRepository personRepository,
                      @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        this.personRepository = personRepository;
        this.embeddingModel = embeddingModel;
    }

    @Override
    public void run(String... args) {

        if(personRepository.count() > 0) return;

        // Doctor 1
        Doctor doctor1 = Doctor.builder()
                .name("Dr. Sharma")
                .email("sharma@meditrack.com")
                .phone("9876543210")
                .address("Delhi")
                .role(Role.DOCTOR)
                .consultationFee(800.0)
                .specialist(Specialist.CARDIOLOGIST)
                .description("Heart specialist with 10 years experience in surgery")
                .isAvailable(true)
                .build();

        // Generate and set embedding
        doctor1.setEmbedding(embeddingModel.embed(doctor1.getSpecialist() + " " + doctor1.getDescription()));
        personRepository.save(doctor1);

        // Doctor 2
        Doctor doctor2 = Doctor.builder()
                .name("Dr. Mehta")
                .email("mehta@meditrack.com")
                .phone("9876543211")
                .address("Mumbai")
                .role(Role.DOCTOR)
                .consultationFee(600.0)
                .specialist(Specialist.DERMATOLOGIST)
                .description("Skin specialist focused on acne and laser treatment")
                .isAvailable(true)
                .build();

        // Generate and set embedding
        doctor2.setEmbedding(embeddingModel.embed(doctor2.getSpecialist() + " " + doctor2.getDescription()));
        personRepository.save(doctor2);

        System.out.println("Doctors with embeddings loaded successfully 🚀");
    }
}