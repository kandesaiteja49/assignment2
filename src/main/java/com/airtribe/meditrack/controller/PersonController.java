package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.dto.DoctorDetailDTO;
import com.airtribe.meditrack.dto.PatientDetailDTO;
import com.airtribe.meditrack.entities.Doctor;
import com.airtribe.meditrack.entities.Patient;
import com.airtribe.meditrack.entities.Person;
import com.airtribe.meditrack.repositories.PersonRepository;
import com.airtribe.meditrack.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping

public class PersonController {

    private final PersonService personService;
    private final PersonRepository personRepository;

    private final EmbeddingModel embeddingModel;

    // Manual constructor to resolve the ambiguity
    public PersonController(
            PersonService personService, PersonRepository personRepository,
            @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        this.personService = personService;
        this.personRepository = personRepository;
        this.embeddingModel = embeddingModel;
    }

    @PostMapping("/register/doc")
    ResponseEntity<Person> createDoc(@RequestBody DoctorDetailDTO dto) {

        return ResponseEntity.ok(personService.createDoctor(dto));
    }

    @PostMapping("/register/patient")
    ResponseEntity<Person> createPatient(@RequestBody PatientDetailDTO dto) {

       return ResponseEntity.ok(personService.createPatient(dto));

    }


    //admin only
    @PostMapping("/register/addAllDoc")
    ResponseEntity<String> createDoc(@RequestBody List<DoctorDetailDTO> dto) {

        return ResponseEntity.ok(personService.addAllDoc(dto));
    }

}
