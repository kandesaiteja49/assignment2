package com.airtribe.meditrack.notificationService;

import com.airtribe.meditrack.entities.Appointment;

public interface Observer {
    void updateAppointment(Appointment appointment);

    void updateDoctorMessage(String message);
}
