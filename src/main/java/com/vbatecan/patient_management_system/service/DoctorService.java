package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.DoctorDTO;
import com.vbatecan.patient_management_system.model.Doctor; // Added import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DoctorService {
	Doctor save(DoctorDTO doctorDTO); // Changed return type

	Optional<Doctor> findById(Integer id); // Changed return type

	Optional<Doctor> findByUserAccountId(Integer userAccountId); // Changed return type

	Optional<Doctor> findByEmail(String email); // Changed return type

	Page<Doctor> findAll(Pageable pageable); // Changed return type

	Doctor update(Integer id, DoctorDTO doctorDTO); // Changed return type

	void delete(Integer id);
}
