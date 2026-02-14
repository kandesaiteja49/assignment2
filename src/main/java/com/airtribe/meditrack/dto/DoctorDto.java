package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.enums.Role;
import com.airtribe.meditrack.enums.Specialist;
import lombok.Data;

@Data
public class DoctorDto {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private Role role;

    private Specialist specialist;

    private String description;

    private String consultationFee;
}
