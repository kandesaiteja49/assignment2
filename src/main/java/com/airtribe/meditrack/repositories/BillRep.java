package com.airtribe.meditrack.repositories;

import com.airtribe.meditrack.entities.BillSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface BillRep extends JpaRepository<BillSummary, Integer> {
    ResponseEntity<BillSummary> findByAppointmentId(Long appointmentId);
}
