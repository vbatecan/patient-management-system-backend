package com.vbatecan.patient_management_system.service.interfaces;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.vbatecan.patient_management_system.model.dto.PatientDTO;
import com.vbatecan.patient_management_system.model.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientService {
	Optional<Patient> save(PatientDTO patientDTO);

	Optional<Patient> findById(Integer id);

	Page<Patient> findAll(Pageable pageable);

	Optional<Patient> update(Integer id, PatientDTO patientDTO) throws JsonMappingException;

	void delete(Integer id);
}
