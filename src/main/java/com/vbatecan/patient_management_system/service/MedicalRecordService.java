package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.MedicalRecordDTO;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {
    MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO); // Renamed from createMedicalRecord
    Optional<MedicalRecordDTO> getMedicalRecordById(Integer id);
    List<MedicalRecordDTO> getAllMedicalRecords();
    List<MedicalRecordDTO> getMedicalRecordsByPatientId(Integer patientId);
    MedicalRecordDTO update(Integer id, MedicalRecordDTO medicalRecordDTO); // Renamed from updateMedicalRecord
    void delete(Integer id); // Renamed from deleteMedicalRecord
}
