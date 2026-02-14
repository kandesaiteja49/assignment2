package com.airtribe.meditrack.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private String billType;
    private double baseAmount;
    private double taxAmount;
    private double finalAmount;
    private LocalDateTime generatedAt;


}