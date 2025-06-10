package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import com.vbatecan.patient_management_system.model.Patient; // Added import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientService {
	Patient save(PatientDTO patientDTO); // Changed return type

	Optional<Patient> findById(Integer id); // Changed return type

	Page<Patient> findAll(Pageable pageable); // Changed return type

	Patient update(Integer id, PatientDTO patientDTO); // Changed return type

	void delete(Integer id);
}
