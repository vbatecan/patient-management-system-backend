package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.MedicalRecordDTO;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {
    MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO);
    Optional<MedicalRecordDTO> findById(Integer id); // Renamed from getMedicalRecordById
    List<MedicalRecordDTO> findAll(); // Renamed from getAllMedicalRecords
    List<MedicalRecordDTO> findByPatientId(Integer patientId); // Renamed from getMedicalRecordsByPatientId
    MedicalRecordDTO update(Integer id, MedicalRecordDTO medicalRecordDTO);
    void delete(Integer id);
}
