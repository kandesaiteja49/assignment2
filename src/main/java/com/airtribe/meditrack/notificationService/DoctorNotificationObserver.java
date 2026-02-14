package com.airtribe.meditrack.notificationService;

import com.airtribe.meditrack.entities.Appointment;
import com.airtribe.meditrack.entities.Doctor;
import com.airtribe.meditrack.repositories.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DoctorNotificationObserver implements Observer {

    @Autowired
    private DoctorRepo doctorRepo;

    @Override
    public void updateAppointment(Appointment appointment) {

        Doctor doctor = doctorRepo.findById(appointment.getDoctor().getId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
        System.out.println("DOCTOR NOTIFICATION");
        System.out.println("Doctor: " + doctor.getName());
        System.out.println("Doctor ID: " + doctor.getId());
        System.out.println("Appointment ID: " + appointment.getId());
        System.out.println("Patient: " + appointment.getPatient().getName());
        System.out.println("Patient: " + appointment.getPatient().getId());
        System.out.println("Time: " + appointment.getStartTime()+" to "+appointment.getEndTime());
        //appotment.getStatus() can be used to send different notifications for different status of appointment
        System.out.println("Appointment Status:"+appointment.getStatus());
    }

    @Override
    public void updateDoctorMessage(String message) {
        System.out.println("Doctor NOTIFICATION");
        System.out.println("Message: " + message);
    }
}
