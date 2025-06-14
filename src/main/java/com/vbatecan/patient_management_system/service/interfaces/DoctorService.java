package com.vbatecan.patient_management_system.service.interfaces;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.DoctorDTO;
import com.vbatecan.patient_management_system.model.entities.Doctor;
import com.vbatecan.patient_management_system.model.update.DoctorUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DoctorService {
	Doctor save(DoctorDTO doctorDTO);

	Optional<Doctor> findById(Integer id);

	Optional<Doctor> findByUserAccountId(Integer userAccountId);

	Optional<Doctor> findByEmail(String email);

	Page<Doctor> findAll(Pageable pageable);

	Doctor update(Integer id, DoctorUpdate doctorUpdate) throws ResourceNotFoundException, JsonMappingException;

	void delete(Integer id);
}
