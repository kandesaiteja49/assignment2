package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDetailDTO {


    private String email;


    private String phone;



    private String address;


    private Role role;


    private String name;

    
    private Integer age;

}
