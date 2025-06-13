package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.entities.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
	Page<MedicalRecord> findByPatientId(Integer patientId, Pageable pageable);
}
