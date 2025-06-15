package com.vbatecan.patient_management_system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.MedicalRecordDTO;
import com.vbatecan.patient_management_system.model.entities.MedicalRecord;
import com.vbatecan.patient_management_system.model.entities.Patient;
import com.vbatecan.patient_management_system.repository.MedicalRecordRepository;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.service.interfaces.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

	private final MedicalRecordRepository medicalRecordRepository;
	private final PatientRepository patientRepository;
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public MedicalRecord save(MedicalRecordDTO medicalRecordDTO) {
		MedicalRecord medicalRecord = mapper.convertValue(medicalRecordDTO, MedicalRecord.class);
		medicalRecord.setCreatedAt(LocalDateTime.now());
		medicalRecord.setUpdatedAt(LocalDateTime.now());
		return medicalRecordRepository.save(medicalRecord);
	}

	@Override
	public Optional<MedicalRecord> findById(Integer id) {
		return medicalRecordRepository.findById(id);
	}

	@Override
	public Page<MedicalRecord> findAll(Pageable pageable) {
		return medicalRecordRepository.findAll(pageable);
	}

	@Override
	public Page<MedicalRecord> findByPatientId(Integer patientId, Pageable pageable) {
		if ( !patientRepository.existsById(patientId) ) {
			throw new ResourceNotFoundException("Patient not found with id: " + patientId);
		}
		return medicalRecordRepository.findByPatientId(patientId, pageable);
	}

	@Override
	@Transactional
	public MedicalRecord update(Integer id, MedicalRecordDTO medicalRecordDTO) {
		// TODO: use mapper updateValue function.
		MedicalRecord existingMedicalRecord = medicalRecordRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + id));

		existingMedicalRecord.setRecordDate(medicalRecordDTO.getRecordDate());
		existingMedicalRecord.setDescription(medicalRecordDTO.getDescription());
		existingMedicalRecord.setFilePath(medicalRecordDTO.getFilePath());
		existingMedicalRecord.setUpdatedAt(LocalDateTime.now());

		if ( medicalRecordDTO.getPatientId() != null ) {
			// Check if patientId has changed
			if (existingMedicalRecord.getPatient() == null || !existingMedicalRecord.getPatient().getId().equals(medicalRecordDTO.getPatientId())) {
				Patient patient = patientRepository.findById(medicalRecordDTO.getPatientId())
					.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + medicalRecordDTO.getPatientId()));
				existingMedicalRecord.setPatient(patient);
			}
		} else {
			throw new IllegalArgumentException("Patient ID cannot be null for updating medical record's patient.");
		}

		return medicalRecordRepository.save(existingMedicalRecord);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if ( !medicalRecordRepository.existsById(id) ) {
			throw new ResourceNotFoundException("MedicalRecord not found with id: " + id);
		}
		medicalRecordRepository.deleteById(id);
	}
}
