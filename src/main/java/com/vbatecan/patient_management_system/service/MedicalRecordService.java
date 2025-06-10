package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.MedicalRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MedicalRecordService {
	MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO);

	Optional<MedicalRecordDTO> findById(Integer id);

	Page<MedicalRecordDTO> findAll(Pageable pageable);

	Page<MedicalRecordDTO> findByPatientId(Integer patientId, Pageable pageable);

	MedicalRecordDTO update(Integer id, MedicalRecordDTO medicalRecordDTO);

	void delete(Integer id);
}
