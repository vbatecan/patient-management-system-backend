package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    PatientDTO save(PatientDTO patientDTO);
    Optional<PatientDTO> findById(Integer id);
    List<PatientDTO> findAll();
    PatientDTO update(Integer id, PatientDTO patientDTO);
    void delete(Integer id);
}
