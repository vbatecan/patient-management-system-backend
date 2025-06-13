package com.vbatecan.patient_management_system.service.interfaces;

import com.vbatecan.patient_management_system.model.dto.MedicalRecordDTO;
import com.vbatecan.patient_management_system.model.entities.MedicalRecord; // Added import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MedicalRecordService {
	MedicalRecord save(MedicalRecordDTO medicalRecordDTO); // Changed return type

	Optional<MedicalRecord> findById(Integer id); // Changed return type

	Page<MedicalRecord> findAll(Pageable pageable); // Changed return type

	Page<MedicalRecord> findByPatientId(Integer patientId, Pageable pageable); // Changed return type

	MedicalRecord update(Integer id, MedicalRecordDTO medicalRecordDTO); // Changed return type

	void delete(Integer id);
}
