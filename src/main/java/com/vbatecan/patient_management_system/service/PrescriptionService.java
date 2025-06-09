package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PrescriptionDTO;
import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO);
    Optional<PrescriptionDTO> getPrescriptionById(Integer id);
    List<PrescriptionDTO> getAllPrescriptions();
    List<PrescriptionDTO> getPrescriptionsByAppointmentId(Integer appointmentId);
    PrescriptionDTO updatePrescription(Integer id, PrescriptionDTO prescriptionDTO);
    void deletePrescription(Integer id);
}
