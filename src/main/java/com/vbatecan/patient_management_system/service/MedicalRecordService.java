package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.MedicalRecordDTO;
import java.util.List;

public interface MedicalRecordService {
    MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecordDto);
    MedicalRecordDTO getMedicalRecordById(Integer id);
    List<MedicalRecordDTO> getAllMedicalRecords();
    MedicalRecordDTO updateMedicalRecord(Integer id, MedicalRecordDTO medicalRecordDto);
    void deleteMedicalRecord(Integer id);
    List<MedicalRecordDTO> getMedicalRecordsByPatientId(Integer patientId);
} 