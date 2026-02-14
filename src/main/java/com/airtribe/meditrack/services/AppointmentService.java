package com.airtribe.meditrack.services;

import com.airtribe.meditrack.billing.Bill;
import com.airtribe.meditrack.dto.AppointmentDTO;
import com.airtribe.meditrack.dto.DocObservationDto;
import com.airtribe.meditrack.dto.PaymentDto;
import com.airtribe.meditrack.entities.*;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.enums.BillType;
import com.airtribe.meditrack.exceptions.AppointmentNotFoundException;
import com.airtribe.meditrack.factory.BillFactory;
import com.airtribe.meditrack.notificationService.NotifySubject;
import com.airtribe.meditrack.notificationService.Observer;
import com.airtribe.meditrack.repositories.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor//this autogenerates a constructor with all the final fields as parameters, which is useful for dependency injection in Spring. It eliminates the need to manually write a constructor to initialize these fields, making the code cleaner and more concise.
public class AppointmentService implements NotifySubject {

    private final ModelMapper modelMapper;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final AppointmentRepo appointmentRepo;
    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;
    private final List<Observer> observers;
    private final BillRep billRepo;

    //create notifyoberver method to notify the observers when an appointment is booked, confirmed, completed or cancelled
    @Override
    public void notifyObserversPrivate(Appointment appointment) {
        for (Observer observer : observers) {
            observer.updateAppointment(appointment);
        }
    }

    @Override
    public void notifyAll(String message) {
        for (Observer observer : observers) {
            observer.updateDoctorMessage(message);
        }
    }

    public AppointmentDTO bookAppointment(Long docid, Long patid, Appointment appointment) {
        Optional<Doctor> doctor = doctorRepo.findById(docid);
        Optional<Patient> patient = patientRepo.findById(patid);
        Appointment appoint;

        if (!doctor.isPresent()) {
            throw new AppointmentNotFoundException("Doctor not found with ID: " + docid);
        } else if (!patient.isPresent()) {
            throw new AppointmentNotFoundException("Patient not found with ID: " + patid);
        }
        if (!isAvailableSlot(docid,
                appointment.getStartDate(),
                appointment.getStartTime(),
                appointment.getEndTime())) {

            throw new AppointmentNotFoundException(
                    "Time slot is not available for the selected doctor. Please choose a different time.");
        }

        appoint = Appointment.builder()
                .doctor(doctor.get())//when you use optional use get() to retrieve the value
                .patient(patient.get())
                .startDate(appointment.getStartDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(AppointmentStatus.PAYMENT_PENDING)
                .patientSymptoms(appointment.getPatientSymptoms())
                .paymentAmount(doctor.get().getConsultationFee())
                .build();

        appointmentRepo.save(appoint);
        notifyObserversPrivate(appoint);

        return modelMapper.map(appoint, AppointmentDTO.class);
    }

    private boolean isAvailableSlot(Long doctorId, @NotNull LocalDate startDate, @NotNull LocalTime startTime, @NotNull LocalTime endTime) {

        return appointmentRepo.findConflictsInAppointment(doctorId, startDate, startTime, endTime).isEmpty();
    }

    public String confirmAppointment(Long appointid, PaymentDto paymentDto) {
        Optional<Appointment> appointment = appointmentRepo.findById(appointid);

        if (appointment.isEmpty()) {
            throw new AppointmentNotFoundException("Appointment not found. Please check the appointment ID."+ appointid);
        }

        if (appointment.get().getDoctor().getConsultationFee() > paymentDto.getPaymentAmount()) {
            throw new AppointmentNotFoundException("Insufficient payment amount. Please pay the full consultation fee.");
        }



        Bill bill = BillFactory.createBill(
                paymentDto.getBillType(),
                appointment.get().getDoctor().getConsultationFee()
        );

        BillSummary summary = BillSummary.builder()
                .appointmentId(appointment.get().getId())
                .baseAmount(bill.getBaseAmount())
                .billType(String.valueOf(paymentDto.getBillType()))
                .patientName(appointment.get().getPatient().getName())
                .doctorName(appointment.get().getDoctor().getName())
                .taxAmount(bill.getTax())
                .finalAmount(bill.getFinalAmount())
                .generatedAt(LocalDateTime.now()).build();


        Boolean paid = paymentService.processPayment(paymentDto.getPaymentType(), paymentDto.getPaymentAmount());
        if (!paid) {
            throw new AppointmentNotFoundException("Payment not found. Please check the appointment ID.");
        }

        Appointment appointment1 = appointment.get();





        appointment1.setPaymentAmount(
                bill.getFinalAmount()
        );
        appointment1.setStatus(AppointmentStatus.SCHEDULED);

        Payment payment = Payment.builder()
                .amount(paymentDto.getPaymentAmount())
                .paymentType(paymentDto.getPaymentType())
                .appointment(appointment1)
                .build();

        paymentRepo.save(payment);
        billRepo.save(summary);
        appointmentRepo.save(appointment1);
        notifyObserversPrivate(appointment1);
        return "Appointment confirmed and payment processed successfully.";
    }

    public String doctorConsultationCompletion(Long AppointmentId, DocObservationDto docObservationDto) {
        Optional<Appointment> appointment = appointmentRepo.findById(AppointmentId);
        if (!appointment.isPresent()) {
            throw new AppointmentNotFoundException("Appointment not found with ID: " + AppointmentId);
        }

            Appointment appointment1 = appointment.get();
            appointment1.setStatus(AppointmentStatus.COMPLETED);
            appointment1.setDocObservations(docObservationDto.getDocObservations());
            appointmentRepo.save(appointment1);

            //notification
            notifyObserversPrivate(appointment1);

            notifyAll(appointment.get().getDoctor().getName()+" has completed the consultation for appointment ID: " + AppointmentId);
            notifyAll(appointment.get().getDoctor().getName()+" is now available for new appointments from "+appointment.get().getEndTime()+" onwards.");
        return "Doctor consultation completed successfully.";
    }

    //Cancel the appointment by sending the appointment id and reason for cancellation
    public String cancelAppointment(Long AppointmentId, String reason) {
        Optional<Appointment> appointment = appointmentRepo.findById(AppointmentId);
        if (!appointment.isPresent()) {
            throw new AppointmentNotFoundException("Appointment not found with ID: " + AppointmentId);
        }

        if(appointment.get().getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppointmentNotFoundException("Cannot cancel a completed appointment.");
        }

            Appointment appointment1 = appointment.get();
            appointment1.setStatus(AppointmentStatus.CANCELLED);
            appointment1.setStartTime(null);
            appointment1.setEndTime(null);
            appointment1.setCancellationReason(reason);
            appointmentRepo.save(appointment1);

            notifyObserversPrivate(appointment1);
            notifyAll(appointment.get().getDoctor().getName()+" is now available for new appointments between "+appointment.get().getStartTime()+" and "+appointment.get().getEndTime());
        return "Appointment cancelled successfully.";
    }

    public AppointmentDTO getAppointmentById(Long id) {
        Optional<Appointment> appointment = appointmentRepo.findById(id);
        if (appointment.isPresent()) {
            return modelMapper.map(appointment.get(), AppointmentDTO.class);
        } else {
            throw new RuntimeException("Appointment not found");
        }
    }

}
