package com.airtribe.meditrack.dto;

import lombok.Data;

@Data
public class PatientDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String age;
    private String role;
}

