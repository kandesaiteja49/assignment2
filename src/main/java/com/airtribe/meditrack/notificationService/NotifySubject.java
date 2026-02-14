package com.airtribe.meditrack.notificationService;

import com.airtribe.meditrack.entities.Appointment;
import com.airtribe.meditrack.entities.Patient;

public interface NotifySubject {
    void notifyObserversPrivate(Appointment appointment);

    void notifyAll(String message);
}
