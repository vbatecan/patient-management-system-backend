package com.vbatecan.patient_management_system.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.dto.PatientDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	private final PatientRepository patientRepository;
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public Optional<Patient> save(PatientDTO patientDTO) {
		Patient patient = mapper.convertValue(patientDTO, Patient.class);
		patient.setCreatedAt(LocalDateTime.now());
		patient.setUpdatedAt(LocalDateTime.now());
		return Optional.of(patientRepository.save(patient));
	}

	@Override
	public Optional<Patient> findById(Integer id) {
		return patientRepository.findById(id);
	}

	@Override
	public Page<Patient> findAll(Pageable pageable) {
		return patientRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Optional<Patient> update(Integer id, PatientDTO patientDTO) throws ResourceNotFoundException, JsonMappingException {
		Patient existingPatient = patientRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

		Patient updatedPatient = mapper.updateValue(existingPatient, patientDTO);
		return Optional.of(patientRepository.save(updatedPatient));
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if ( !patientRepository.existsById(id) ) {
			throw new ResourceNotFoundException("Patient not found with id: " + id);
		}
		patientRepository.deleteById(id);
	}
}
