package com.vbatecan.patient_management_system.service.interfaces;

import com.vbatecan.patient_management_system.model.dto.MedicalRecordDTO;
import com.vbatecan.patient_management_system.model.entities.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MedicalRecordService {
	MedicalRecord save(MedicalRecordDTO medicalRecordDTO);

	Optional<MedicalRecord> findById(Integer id);

	Page<MedicalRecord> findAll(Pageable pageable);

	Page<MedicalRecord> findByPatientId(Integer patientId, Pageable pageable);

	MedicalRecord update(Integer id, MedicalRecordDTO medicalRecordDTO);

	void delete(Integer id);
}
