package com.airtribe.meditrack.dto;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocObservationDto {

    @Column(columnDefinition = "TEXT")
    private String docObservations;
}
