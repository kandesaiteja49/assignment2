package com.airtribe.meditrack.services;

import com.airtribe.meditrack.dto.DoctorDetailDTO;
import com.airtribe.meditrack.dto.PatientDetailDTO;
import com.airtribe.meditrack.entities.Doctor;
import com.airtribe.meditrack.entities.Patient;
import com.airtribe.meditrack.entities.Person;
import com.airtribe.meditrack.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PersonService {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final EmbeddingModel embeddingModel;
    public PersonService(PersonRepository personRepository, ModelMapper modelMapper,  @Qualifier("ollamaEmbeddingModel")EmbeddingModel embeddingModel) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.embeddingModel = embeddingModel;
    }

    public Person createDoctor(DoctorDetailDTO dto) {

        Doctor doctor = Doctor.builder()
                .name(dto.getName())            // goes to doctor table
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(dto.getRole())
                .consultationFee(dto.getConsultationFee())
                .specialist(dto.getSpecialist())
                .description(dto.getDescription())
                .isAvailable(true)
                .build();

        // Generate embedding - added String.valueOf to prevent NullPointer if specialist is missing
        String textToEmbed = String.valueOf(doctor.getSpecialist()) + " " + doctor.getDescription();
        float[] vector = embeddingModel.embed(textToEmbed);

        Person person = personRepository.save(doctor);
        return person;
    }


    public Person createPatient(PatientDetailDTO dto) {
        Patient patient = Patient.builder()
                .name(dto.getName())            // goes to doctor table
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(dto.getRole())
                .age(dto.getAge())
                .build();

        Person person = personRepository.save(patient);

        return person;
    }

    public String addAllDoc(List<DoctorDetailDTO> docdto) {

        docdto.stream().forEach(dto -> {
            Doctor doctor = Doctor.builder()
                    .name(dto.getName())            // goes to doctor table
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .address(dto.getAddress())
                    .role(dto.getRole())
                    .consultationFee(dto.getConsultationFee())
                    .specialist(dto.getSpecialist())
                    .description(dto.getDescription())
                    .isAvailable(true)
                    .build();

            // Generate embedding - added String.valueOf to prevent NullPointer if specialist is missing
            String textToEmbed = String.valueOf(doctor.getSpecialist()) + " " + doctor.getDescription();
            float[] vector = embeddingModel.embed(textToEmbed);

            personRepository.save(doctor);
        });

        Long count=personRepository.findAll().stream().count();

        return "All doctors added successfully. Total doctors in the system: " + count;
    }
}
