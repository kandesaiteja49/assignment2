package com.airtribe.meditrack.notificationService;

import com.airtribe.meditrack.entities.Appointment;
import com.airtribe.meditrack.entities.Patient;
import com.airtribe.meditrack.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientNotificationObserver implements Observer {

    @Autowired
    private PatientRepo patientRepo;

    @Override
    public void updateAppointment(Appointment appointment) {

        Patient patient=patientRepo.findById(appointment.getPatient().getId()).orElseThrow(() -> new RuntimeException("Patient not found"));

        System.out.println("PATIENT NOTIFICATION");
        System.out.println("Patient: " + appointment.getPatient().getName());
        System.out.println("Patient ID: " + appointment.getPatient().getId());
        System.out.println("Appointment ID: " + appointment.getId());
        System.out.println("Doctor: " + appointment.getDoctor().getName());
        System.out.println("Doctor ID: " + appointment.getDoctor().getId());
        System.out.println("Time: " + appointment.getStartTime()+" to "+appointment.getEndTime());
        System.out.println("Appointment Status:"+appointment.getStatus());
    }

    @Override
    public void updateDoctorMessage(String message) {
        System.out.println("Doctor NOTIFICATION");
        System.out.println("Message: " + message);
    }
}
