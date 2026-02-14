package com.airtribe.meditrack.controller;
import com.airtribe.meditrack.dto.DoctorAppointmentCount;
import com.airtribe.meditrack.dto.DoctorDto;
import com.airtribe.meditrack.entities.Doctor;
import com.airtribe.meditrack.enums.Specialist;
import com.airtribe.meditrack.repositories.DoctorRepo;
import com.airtribe.meditrack.services.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorRepo doctorRepo;
    private final ModelMapper modelMapper;

    private final DoctorService doctorService;

    @GetMapping("/doctor/{id}")
    public ResponseEntity<DoctorDto> getDocByID(@PathVariable Integer id) {

        return ResponseEntity.ok(doctorService.getDocById(id));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDto>> getAllDoc() {

        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<DoctorDto>> recommend(@RequestParam String symptoms) {
        return ResponseEntity.ok(doctorService.recommendDoctors(symptoms));
    }


    @GetMapping("/searchBySpecialization")
    public ResponseEntity<List<DoctorDto>> searchDoctors(@RequestParam Specialist specialist) {
        return new ResponseEntity<>(doctorService.searchBySpecialization(specialist), HttpStatus.OK);
    }

    @GetMapping("getAppointmentCountByDoctor")
    public ResponseEntity<List<DoctorAppointmentCount>> getAppointmentCountByDoctor() {
        return new ResponseEntity<>(doctorService.getAppointmentsPerDoctor(), HttpStatus.OK);
    }

    @GetMapping("getAverageFeeByDoctor")
    public ResponseEntity<List<Map<String, Object>>> getAverageFeePerDoctor() {
        return new ResponseEntity<>(doctorService.getAverageFeePerDoctor(), HttpStatus.OK);
    }

    @GetMapping("getAverageFeeBySpecialization")
    public ResponseEntity<List<Map<String, Object>>> getAverageFeePerDoctorSpecialization() {
        return new ResponseEntity<>(doctorService.getAverageFeePerDoctorSpecialization(), HttpStatus.OK);
    }
}
