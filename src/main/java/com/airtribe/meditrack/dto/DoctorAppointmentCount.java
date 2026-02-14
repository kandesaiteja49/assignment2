package com.airtribe.meditrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAppointmentCount {
    private Long doctorId;
    private String doctorName;
    private Long appointmentCount;
}
