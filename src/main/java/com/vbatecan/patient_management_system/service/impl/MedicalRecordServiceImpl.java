package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.model.dto.MedicalRecordDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
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

	@Override
	@Transactional
	public MedicalRecord save(MedicalRecordDTO medicalRecordDTO) {
		MedicalRecord medicalRecord = convertToEntity(medicalRecordDTO);
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

	// This DTO conversion method is kept for potential internal use or by other layers,
	// but it's not used by the public methods of this service anymore.
	private MedicalRecordDTO convertToDTO(MedicalRecord medicalRecord) {
		MedicalRecordDTO dto = new MedicalRecordDTO();
		dto.setId(medicalRecord.getId());
		if ( medicalRecord.getPatient() != null ) {
			dto.setPatientId(medicalRecord.getPatient().getId());
		}
		dto.setRecordDate(medicalRecord.getRecordDate());
		dto.setDescription(medicalRecord.getDescription());
		dto.setFilePath(medicalRecord.getFilePath());
		dto.setCreatedAt(medicalRecord.getCreatedAt());
		dto.setUpdatedAt(medicalRecord.getUpdatedAt());
		return dto;
	}

	private MedicalRecord convertToEntity(MedicalRecordDTO medicalRecordDTO) {
		MedicalRecord medicalRecord = new MedicalRecord();
		if ( medicalRecordDTO.getPatientId() == null ) {
			throw new IllegalArgumentException("Patient ID cannot be null for a new medical record.");
		}
		Patient patient = patientRepository.findById(medicalRecordDTO.getPatientId())
			.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + medicalRecordDTO.getPatientId()));
		medicalRecord.setPatient(patient);
		medicalRecord.setRecordDate(medicalRecordDTO.getRecordDate());
		medicalRecord.setDescription(medicalRecordDTO.getDescription());
		medicalRecord.setFilePath(medicalRecordDTO.getFilePath());
		// Timestamps (createdAt, updatedAt) are handled in save/update methods.
		return medicalRecord;
	}
}
