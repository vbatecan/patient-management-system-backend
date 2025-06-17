package com.vbatecan.patient_management_system.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.model.enums.Role;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.DoctorDTO;
import com.vbatecan.patient_management_system.model.entities.Doctor;
import com.vbatecan.patient_management_system.model.update.DoctorUpdate;
import com.vbatecan.patient_management_system.repository.DoctorRepository;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.interfaces.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

	private final DoctorRepository doctorRepository;
	private final UserAccountRepository userAccountRepository;
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public Doctor save(DoctorDTO doctorDTO) throws IllegalArgumentException {
		Doctor doctor = mapper.convertValue(doctorDTO, Doctor.class);

		// Ensure the associated UserAccount has the DOCTOR role
		if ( doctor.getUserAccount().getRole() != Role.DOCTOR ) {
			throw new IllegalArgumentException("UserAccount provided for Doctor must have DOCTOR role.");
		}

		doctor.setCreatedAt(LocalDateTime.now());
		doctor.setUpdatedAt(LocalDateTime.now());
		return doctorRepository.save(doctor);
	}

	@Override
	public Optional<Doctor> findById(Integer id) {
		return doctorRepository.findById(id);
	}

	@Override
	public Optional<Doctor> findByUserAccountId(Integer userAccountId) {
		return doctorRepository.findByUserAccountId(userAccountId);
	}

	@Override
	public Optional<Doctor> findByEmail(String email) {
		return doctorRepository.findByEmail(email);
	}


	@Override
	public Page<Doctor> findAll(Pageable pageable) {
		return doctorRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Doctor update(Integer id, DoctorUpdate doctorUpdate) throws ResourceNotFoundException, JsonMappingException {
		Doctor existingDoctor = doctorRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

		// TODO: Doctor can only update themselves or the administrator.
		Doctor updatedDoctor = mapper.updateValue(existingDoctor, doctorUpdate);
		updatedDoctor.setUpdatedAt(LocalDateTime.now());
		return doctorRepository.save(updatedDoctor);
	}

	@Override
	@Transactional
	@Async
	public void delete(Integer id) {
		if ( !doctorRepository.existsById(id) ) {
			throw new ResourceNotFoundException("Doctor not found with id: " + id);
		}
		doctorRepository.deleteById(id);
	}
}
