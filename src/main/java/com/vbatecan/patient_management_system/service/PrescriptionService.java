package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PrescriptionDTO;
import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    PrescriptionDTO save(PrescriptionDTO prescriptionDTO);
    Optional<PrescriptionDTO> getPrescriptionById(Integer id);
    List<PrescriptionDTO> getAllPrescriptions();
    List<PrescriptionDTO> getPrescriptionsByAppointmentId(Integer appointmentId);
    PrescriptionDTO update(Integer id, PrescriptionDTO prescriptionDTO);
    void delete(Integer id);
}
