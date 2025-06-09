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
    public MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO) { // Renamed from createMedicalRecord
        MedicalRecord medicalRecord = convertToEntity(medicalRecordDTO);
        medicalRecord.setCreatedAt(LocalDateTime.now());
        medicalRecord.setUpdatedAt(LocalDateTime.now());
        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);
        return convertToDTO(savedMedicalRecord);
    }

    @Override
    public Optional<MedicalRecordDTO> getMedicalRecordById(Integer id) {