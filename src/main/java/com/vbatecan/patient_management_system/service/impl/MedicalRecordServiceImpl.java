package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.MedicalRecordDTO;
import com.vbatecan.patient_management_system.model.MedicalRecord;
import com.vbatecan.patient_management_system.repository.MedicalRecordRepository;
import com.vbatecan.patient_management_system.service.MedicalRecordService;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto) {
        MedicalRecord record = toEntity(dto);
        MedicalRecord saved = medicalRecordRepository.save(record);
        return toDto(saved);
    }

    @Override
    public MedicalRecordDTO getMedicalRecordById(Integer id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + id));
        return toDto(record);
    }

    @Override
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(Integer id, MedicalRecordDTO dto) {
        MedicalRecord record = medicalRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + id));
        record.setRecordDate(dto.getRecordDate());
        record.setDescription(dto.getDescription());
        record.setFilePath(dto.getFilePath());
        record.setUpdatedAt(dto.getUpdatedAt());
        MedicalRecord updated = medicalRecordRepository.save(record);
        return toDto(updated);
    }

    @Override
    public void deleteMedicalRecord(Integer id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("MedicalRecord not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByPatientId(Integer patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private MedicalRecordDTO toDto(MedicalRecord record) {
        return new MedicalRecordDTO(
            record.getId(),
            record.getPatient() != null ? record.getPatient().getId() : null,
            record.getRecordDate(),
            record.getDescription(),
            record.getFilePath(),
            record.getCreatedAt(),
            record.getUpdatedAt()
        );
    }

    private MedicalRecord toEntity(MedicalRecordDTO dto) {
        MedicalRecord record = new MedicalRecord();
        record.setId(dto.getId());
        // Set patient if needed
        record.setRecordDate(dto.getRecordDate());
        record.setDescription(dto.getDescription());
        record.setFilePath(dto.getFilePath());
        record.setCreatedAt(dto.getCreatedAt());
        record.setUpdatedAt(dto.getUpdatedAt());
        return record;
    }
} 