package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.dto.PatientDto;
import com.airtribe.meditrack.entities.Patient;
import com.airtribe.meditrack.repositories.PatientRepo;
import com.airtribe.meditrack.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PatientController {

    private final PatientRepo patientRepo;
    private final ModelMapper modelMapper;

    private final PatientService patientService;

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatientsByID(@PathVariable Long id) {

        return ResponseEntity.ok(patientService.getPatientById(id));

    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>> getAllPatient() {


       return ResponseEntity.ok(patientService.getAllPatient());
    }

}
