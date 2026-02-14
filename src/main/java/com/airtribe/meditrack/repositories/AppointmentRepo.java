package com.airtribe.meditrack.repositories;

import com.airtribe.meditrack.entities.Appointment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
    @Query("""
            select a from Appointment a
            where a.doctor.id = :docId and a.startDate = :startDate
            and ((:startTime<a.endTime) and (:endTime >a.startTime))
            """)
    List<Appointment> findConflictsInAppointment(Long docId,@NotNull LocalDate startDate, @NotNull LocalTime startTime, @NotNull LocalTime endTime);


    List<Appointment> findByDoctorIdAndStartDate(Long doctorId, LocalDate date);
}
