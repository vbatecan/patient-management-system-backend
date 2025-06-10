package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PrescriptionDTO;
import com.vbatecan.patient_management_system.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PrescriptionService {
    Prescription save(PrescriptionDTO prescriptionDTO);

    Optional<Prescription> findById(Integer id);

    Page<Prescription> findAll(Pageable pageable);

    Page<Prescription> findByAppointmentId(Integer appointmentId, Pageable pageable);

    Prescription update(Integer id, PrescriptionDTO prescriptionDTO);

    void delete(Integer id);
}
