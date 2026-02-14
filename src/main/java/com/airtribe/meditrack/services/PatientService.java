package com.airtribe.meditrack.services;

import com.airtribe.meditrack.dto.PatientDto;
import com.airtribe.meditrack.entities.Patient;
import com.airtribe.meditrack.repositories.DoctorRepo;
import com.airtribe.meditrack.repositories.PatientRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepo patientRepo;
    private final ModelMapper modelMapper;

    public PatientDto getPatientById(Long id) {


        Optional<Patient> patient = patientRepo.findById(id);

        if (!patient.isPresent()) {
            throw new RuntimeException("Patient not found");
        }
            return modelMapper.map(patient.get(), PatientDto.class);
    }

    public List<PatientDto> getAllPatient() {


        List<Patient> patient = patientRepo.findAll();

        if (patient.isEmpty()) {
            throw new RuntimeException("No patient found");
        }


            return patient.stream()
                    .map(p->modelMapper.map(p,PatientDto.class))
                    .collect(Collectors.toList());
    }
}
