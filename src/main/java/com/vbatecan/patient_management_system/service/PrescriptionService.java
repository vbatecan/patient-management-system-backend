package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PrescriptionDTO;
import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    PrescriptionDTO save(PrescriptionDTO prescriptionDTO);
    Optional<PrescriptionDTO> findById(Integer id); // Renamed from getPrescriptionById
    List<PrescriptionDTO> findAll(); // Renamed from getAllPrescriptions
    List<PrescriptionDTO> findByAppointmentId(Integer appointmentId); // Renamed from getPrescriptionsByAppointmentId
    PrescriptionDTO update(Integer id, PrescriptionDTO prescriptionDTO);
    void delete(Integer id);
}
