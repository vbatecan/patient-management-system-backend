package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.MedicalRecordDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.MedicalRecord;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.repository.MedicalRecordRepository;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository; // For linking Patient

    @Override
    @Transactional
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord medicalRecord = convertToEntity(medicalRecordDTO);
        medicalRecord.setCreatedAt(LocalDateTime.now());
        medicalRecord.setUpdatedAt(LocalDateTime.now());
        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);
        return convertToDTO(savedMedicalRecord);
    }

    @Override
    public Optional<MedicalRecordDTO> getMedicalRecordById(Integer id) {
        return medicalRecordRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByPatientId(Integer patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalRecordDTO updateMedicalRecord(Integer id, MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord existingMedicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + id));

        existingMedicalRecord.setRecordDate(medicalRecordDTO.getRecordDate());
        existingMedicalRecord.setDescription(medicalRecordDTO.getDescription());
        existingMedicalRecord.setFilePath(medicalRecordDTO.getFilePath());
        existingMedicalRecord.setUpdatedAt(LocalDateTime.now());

        if (medicalRecordDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(medicalRecordDTO.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + medicalRecordDTO.getPatientId()));
            existingMedicalRecord.setPatient(patient);
        } else {
            // Depending on business logic, you might want to prevent unsetting the patient
            // or throw an error if patientId is required for update.
            // For now, allowing it to be potentially unlinked if DTO has null patientId
            // and the original record had one. Or, ensure patientId is always present in DTO for update.
            // Let's assume patientId in DTO is the source of truth for the relation.
             throw new IllegalArgumentException("Patient ID cannot be null for updating medical record's patient.");
        }


        MedicalRecord updatedMedicalRecord = medicalRecordRepository.save(existingMedicalRecord);
        return convertToDTO(updatedMedicalRecord);
    }

    @Override
    @Transactional
    public void deleteMedicalRecord(Integer id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("MedicalRecord not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }

    private MedicalRecordDTO convertToDTO(MedicalRecord medicalRecord) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(medicalRecord.getId());
        if (medicalRecord.getPatient() != null) {
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
        if (medicalRecordDTO.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID cannot be null for a new medical record.");
        }
        Patient patient = patientRepository.findById(medicalRecordDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + medicalRecordDTO.getPatientId()));
        medicalRecord.setPatient(patient);
        medicalRecord.setRecordDate(medicalRecordDTO.getRecordDate());
        medicalRecord.setDescription(medicalRecordDTO.getDescription());
        medicalRecord.setFilePath(medicalRecordDTO.getFilePath());
        // createdAt and updatedAt are set in the service method
        return medicalRecord;
    }
}
