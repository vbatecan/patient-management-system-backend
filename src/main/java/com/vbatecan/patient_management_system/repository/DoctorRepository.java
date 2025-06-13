package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
	Optional<Doctor> findByUserAccountId(Integer userAccountId);

	Optional<Doctor> findByEmail(String email);
}
