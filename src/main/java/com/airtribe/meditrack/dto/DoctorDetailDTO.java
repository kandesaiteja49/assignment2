package com.airtribe.meditrack.dto;


import com.airtribe.meditrack.enums.Role;
import com.airtribe.meditrack.enums.Specialist;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDetailDTO {


    private String name;


    private String email;

    private String phone;

    private String address;

    private Role role;

    private Double consultationFee;

    private Specialist specialist;

    private String description;

}
