package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
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
	private final UserAccountRepository userAccountRepository;

	@Override
	@Transactional
	public Patient save(PatientDTO patientDTO) {
		Patient patient = convertToEntity(patientDTO);
		patient.setCreatedAt(LocalDateTime.now());
		patient.setUpdatedAt(LocalDateTime.now());
		return patientRepository.save(patient);
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
	public Patient update(Integer id, PatientDTO patientDTO) {
		Patient existingPatient = patientRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

		existingPatient.setFirstName(patientDTO.getFirstName());
		existingPatient.setLastName(patientDTO.getLastName());
		existingPatient.setDateOfBirth(patientDTO.getDateOfBirth());
		existingPatient.setGender(patientDTO.getGender());
		existingPatient.setContactNumber(patientDTO.getContactNumber());
		existingPatient.setEmail(patientDTO.getEmail());
		existingPatient.setAddress(patientDTO.getAddress());
		existingPatient.setEmergencyContact(patientDTO.getEmergencyContact());
		existingPatient.setUpdatedAt(LocalDateTime.now());

		if ( patientDTO.getUserAccountId() != null ) {
			// Check if userAccountId has changed or was null
			if (existingPatient.getUserAccount() == null || !existingPatient.getUserAccount().getId().equals(patientDTO.getUserAccountId())) {
				UserAccount userAccount = userAccountRepository.findById(patientDTO.getUserAccountId())
					.orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + patientDTO.getUserAccountId()));
				existingPatient.setUserAccount(userAccount);
			}
		} else {
			// If userAccountId is explicitly null in DTO, disassociate UserAccount
			existingPatient.setUserAccount(null);
		}

		return patientRepository.save(existingPatient);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if ( !patientRepository.existsById(id) ) {
			throw new ResourceNotFoundException("Patient not found with id: " + id);
		}
		patientRepository.deleteById(id);
	}

	// This DTO conversion method is kept for potential internal use or by other layers,
	// but it's not used by the public methods of this service anymore.
	private PatientDTO convertToDTO(Patient patient) {
		PatientDTO dto = new PatientDTO();
		dto.setId(patient.getId());
		if ( patient.getUserAccount() != null ) {
			dto.setUserAccountId(patient.getUserAccount().getId());
		}
		dto.setFirstName(patient.getFirstName());
		dto.setLastName(patient.getLastName());
		dto.setDateOfBirth(patient.getDateOfBirth());
		dto.setGender(patient.getGender());
		dto.setContactNumber(patient.getContactNumber());
		dto.setEmail(patient.getEmail());
		dto.setAddress(patient.getAddress());
		dto.setEmergencyContact(patient.getEmergencyContact());
		dto.setCreatedAt(patient.getCreatedAt());
		dto.setUpdatedAt(patient.getUpdatedAt());
		return dto;
	}

	private Patient convertToEntity(PatientDTO patientDTO) {
		Patient patient = new Patient();
		if ( patientDTO.getUserAccountId() != null ) {
			UserAccount userAccount = userAccountRepository.findById(patientDTO.getUserAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + patientDTO.getUserAccountId()));
			patient.setUserAccount(userAccount);
		}
		// If userAccountId is null in DTO, patient.userAccount remains null by default.

		patient.setFirstName(patientDTO.getFirstName());
		patient.setLastName(patientDTO.getLastName());
		patient.setDateOfBirth(patientDTO.getDateOfBirth());
		patient.setGender(patientDTO.getGender());
		patient.setContactNumber(patientDTO.getContactNumber());
		patient.setEmail(patientDTO.getEmail());
		patient.setAddress(patientDTO.getAddress());
		patient.setEmergencyContact(patientDTO.getEmergencyContact());
		// Timestamps (createdAt, updatedAt) are handled in save/update methods.
		return patient;
	}
}
