package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    PatientDTO createPatient(PatientDTO patientDTO);
    Optional<PatientDTO> getPatientById(Integer id);
    List<PatientDTO> getAllPatients();
    PatientDTO updatePatient(Integer id, PatientDTO patientDTO);
    void deletePatient(Integer id);
}
