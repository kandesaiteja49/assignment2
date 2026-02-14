package com.airtribe.meditrack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Patient extends Person{

        @Length(min = 1, max = 50,message = "min length is 50")
        private String name;

        @Range(min = 1, max = 50)
        private Integer age;

        @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
        @JsonIgnore
        List<Appointment> appointments;

}


