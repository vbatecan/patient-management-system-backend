package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.MedicalRecordDTO;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {
    MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecordDTO);
    Optional<MedicalRecordDTO> getMedicalRecordById(Integer id);
    List<MedicalRecordDTO> getAllMedicalRecords();
    List<MedicalRecordDTO> getMedicalRecordsByPatientId(Integer patientId);
    MedicalRecordDTO updateMedicalRecord(Integer id, MedicalRecordDTO medicalRecordDTO);
    void deleteMedicalRecord(Integer id);
}
