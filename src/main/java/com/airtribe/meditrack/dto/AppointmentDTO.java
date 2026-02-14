package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.entities.Doctor;
import com.airtribe.meditrack.entities.Patient;
import com.airtribe.meditrack.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    private Long id;

    private Doctor doctor;


    private Patient patient;


    private LocalDate startDate;


    private LocalTime startTime;


    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(columnDefinition = "TEXT")
    private String patientSymptoms;

    @Column(columnDefinition = "TEXT")
    private String docObservations;

    private Double paymentAmount;

    private String cancellationReason;
}
