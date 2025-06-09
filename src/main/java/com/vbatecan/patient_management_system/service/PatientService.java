package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import java.util.List;

public interface PatientService {
    PatientDTO createPatient(PatientDTO patientDto);
    PatientDTO getPatientById(Integer id);
    List<PatientDTO> getAllPatients();
    PatientDTO updatePatient(Integer id, PatientDTO patientDto);
    void deletePatient(Integer id);
    List<PatientDTO> searchPatients(String keyword);
} 