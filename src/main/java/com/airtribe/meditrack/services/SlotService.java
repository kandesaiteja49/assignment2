package com.airtribe.meditrack.services;

import com.airtribe.meditrack.Constants.DoctorWorkingHours;
import com.airtribe.meditrack.entities.Appointment;
import com.airtribe.meditrack.repositories.AppointmentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final AppointmentRepo appointmentRepo;

    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {

        List<Appointment> booked =
                appointmentRepo.findByDoctorIdAndStartDate(doctorId, date);

        Set<LocalTime> bookedTimes = booked.stream()
                .map(Appointment::getStartTime)
                .collect(Collectors.toSet());

        List<LocalTime> slots = new ArrayList<>();

        LocalTime t = DoctorWorkingHours.START;

        while (t.isBefore(DoctorWorkingHours.END)) {

            boolean inBreak =
                    !t.isBefore(LocalTime.of(13,0)) &&
                            t.isBefore(LocalTime.of(14,0));

            if (!inBreak && !bookedTimes.contains(t)) {
                slots.add(t);
            }

            t = t.plusMinutes(DoctorWorkingHours.SLOT_MINUTES);
        }

        return slots;
    }
}

