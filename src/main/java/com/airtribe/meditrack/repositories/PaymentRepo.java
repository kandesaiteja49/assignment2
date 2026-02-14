package com.airtribe.meditrack.repositories;

import com.airtribe.meditrack.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepo extends JpaRepository<Payment,Long> {
}
