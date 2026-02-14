package com.airtribe.meditrack.services;

import com.airtribe.meditrack.dto.DoctorAppointmentCount;
import com.airtribe.meditrack.dto.DoctorDto;
import com.airtribe.meditrack.entities.Doctor;
import com.airtribe.meditrack.enums.Specialist;
import com.airtribe.meditrack.repositories.DoctorRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DoctorService {
    private final DoctorRepo doctorRepo;
    private final ModelMapper modelMapper;
    private final EmbeddingModel embeddingModel;
    // Manual constructor to use @Qualifier
    public DoctorService(
            DoctorRepo doctorRepo,
            ModelMapper modelMapper,
            @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        this.doctorRepo = doctorRepo;
        this.modelMapper = modelMapper;
        this.embeddingModel = embeddingModel;
    }

    public DoctorDto getDocById(Integer id) {
        Optional<Doctor> doctor = doctorRepo.findById(id.longValue());

        if (doctor.isPresent()) {
            return modelMapper.map(doctor.get(), DoctorDto.class);
        }
        return null;

    }

    public List<DoctorDto> getAllDoctors() {

        List<Doctor> doctors = doctorRepo.findAll();

        if (doctors.isEmpty()) {
           return new ArrayList<>();
        }
        return doctors.stream()
                .map(d -> modelMapper.map(d, DoctorDto.class))
                .collect(Collectors.toList());
    }



    public List<DoctorDto> recommendDoctors(String symptoms) {

        float[] embedding = embeddingModel.embed(symptoms);

        // Format: [1.234,5.678,9.012] without spaces
        String vector = Arrays.toString(embedding)
                .replace(" ", "");  // Remove spaces between numbers

        return doctorRepo.findTop5Similar(vector)
                .stream()
                .map(d -> modelMapper.map(d, DoctorDto.class))
                .toList();

    }


    public List<DoctorDto> searchBySpecialization(Specialist specialist) {
        List<Doctor> doctors = doctorRepo.findBySpecialist(specialist);

        if (doctors.isEmpty()) {
            return new ArrayList<>();
        }
        return doctors.stream()
                .map(d -> modelMapper.map(d, DoctorDto.class))
                .collect(Collectors.toList());
    }

    //apointments per doctor using using streams analytics
    //just appointments per doctor id and name sorted by count desc
    //using streams only
    public List<Map<String, Object>> getDoctorsWithAppointmentCounts() {
        List<Object[]> results = doctorRepo.findDoctorsWithAppointmentCounts();

        return results.stream()
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", row[0]);
                    map.put("name", row[1]);
                    map.put("appointmentCount", row[2]);
                    return map;
                })
                .sorted((m1, m2) -> Long.compare((Long) m2.get("appointmentCount"), (Long) m1.get("appointmentCount")))
                .collect(Collectors.toList());
    }

    public List<DoctorAppointmentCount> getAppointmentsPerDoctor() {
        List<Doctor> doctors = doctorRepo.findAll();

        return doctors.stream()
                .map(d -> new DoctorAppointmentCount(d.getId(), d.getName(), (long) d.getAppointments().size()))
                .sorted(Comparator.comparingLong(DoctorAppointmentCount::getAppointmentCount).reversed())
                .collect(Collectors.toList());

    }

    //average fee per doctor
    public List<Map<String, Object>> getAverageFeePerDoctor() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("name", d.getName());
            double averageFee = d.getAppointments().stream()
                    .mapToDouble(a -> a.getPaymentAmount() != null ? a.getPaymentAmount() : 0.0)
                    .average()
                    .orElse(0.0);
            map.put("averageFee", averageFee);
            return map;
        }).collect(Collectors.toList());
    }


    public List<Map<String, Object>> getAverageFeePerDoctorSpecialization() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("Specialization", d.getSpecialist());
            double averageFee = d.getAppointments().stream()
                    .mapToDouble(a -> a.getPaymentAmount() != null ? a.getPaymentAmount() : 0.0)
                    .average()
                    .orElse(0.0);
            map.put("averageFee", averageFee);
            return map;
        })//Sort by specialization alphabetically
                .sorted(Comparator.comparing(
                        m -> ((Specialist) m.get("Specialization")).name()
                ))

                .collect(Collectors.toList());
    }
}
