package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.entities.BillSummary;
import com.airtribe.meditrack.repositories.BillRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillSummaryController {

    @Autowired
    private BillRep billRep1;

    @GetMapping("/bill-summary/{appointmentId}")
    ResponseEntity<BillSummary> getBillSummary(@PathVariable Long appointmentId) {
        return billRep1.findByAppointmentId(appointmentId);
    }
}
