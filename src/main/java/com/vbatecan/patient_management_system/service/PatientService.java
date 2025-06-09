package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PatientService {
    PatientDTO save(PatientDTO patientDTO);
    Optional<PatientDTO> findById(Integer id);
    Page<PatientDTO> findAll(Pageable pageable);
    PatientDTO update(Integer id, PatientDTO patientDTO);
    void delete(Integer id);
}
