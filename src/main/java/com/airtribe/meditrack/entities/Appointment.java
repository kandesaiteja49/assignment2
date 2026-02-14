package com.airtribe.meditrack.entities;

import com.airtribe.meditrack.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    Patient patient;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status=AppointmentStatus.PAYMENT_PENDING;

    @Column(columnDefinition = "TEXT")
    private String patientSymptoms;

    @Column(columnDefinition = "TEXT")
    private String docObservations;

    private Double paymentAmount;

    private String cancellationReason;
}
