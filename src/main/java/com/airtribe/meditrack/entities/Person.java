package com.airtribe.meditrack.entities;


import com.airtribe.meditrack.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
//@MappedSuperclass //use only when using abstract class, otherwise it will create a table for this class and the subclasses will have their own tables with a foreign key to this table, which is not what we want in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
@Inheritance(strategy = InheritanceType.JOINED)
//remember the data we insert through child are directly joined here
//next also if we try to fetch data from this table, it will also fetch data from child tables and join them together, which is not what we want in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
//so we will use JOINED strategy which will create a separate table for each subclass and join them together when we fetch data from the parent table, which is what we want in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
//similary if we fetch data from child table, it will also fetch data from parent table and join them together, which is what we want in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
//totally two table will be created one for person and other for doctor and patient and they will be joined together when we fetch data from person table or doctor/patient table, which is what we want in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
//its all because of the JOINED strategy which will create a separate table for each subclass and join them together when we fetch data from the parent table, which is what we want in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
//and inheritance and jpa will take care of the rest, we just need to define the common fields in the parent class and the specific fields in the child classes, and jpa will take care of the rest, we just need to use the correct annotations and strategies, which is what we have done in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
//IMP:Always save the data to parent table first and then to child table, otherwise it will throw an error since the child table will have a foreign key to the parent table, which is what we want in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
//add data thru child table, it will automatically save data to parent table as well, which is what we want in this case since we want to treat this purely for code reusability, not as an entity like USER for login and authentication
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Person {  //treat this purely for code reusability, not as an entity like USER for login and authentication

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Email(message = "Email should be valid", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
