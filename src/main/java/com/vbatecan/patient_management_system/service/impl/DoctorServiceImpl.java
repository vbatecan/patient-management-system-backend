package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.DoctorDTO;
import com.vbatecan.patient_management_system.enums.Role;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Doctor;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.DoctorRepository;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.DoctorService;
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

	@Override
	@Transactional
	public Doctor save(DoctorDTO doctorDTO) {
		Doctor doctor = convertToEntity(doctorDTO);

		// Ensure the associated UserAccount has the DOCTOR role
		if (doctor.getUserAccount().getRole() != Role.DOCTOR) {
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
	public Doctor update(Integer id, DoctorDTO doctorDTO) {
		Doctor existingDoctor = doctorRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

		existingDoctor.setFirstName(doctorDTO.getFirstName());
		existingDoctor.setLastName(doctorDTO.getLastName());
		existingDoctor.setSpecialty(doctorDTO.getSpecialty());
		existingDoctor.setContactNumber(doctorDTO.getContactNumber());
		existingDoctor.setEmail(doctorDTO.getEmail());
		existingDoctor.setUpdatedAt(LocalDateTime.now());

		if ( doctorDTO.getUserAccountId() != null ) {
			UserAccount userAccount = userAccountRepository.findById(doctorDTO.getUserAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + doctorDTO.getUserAccountId()));
			if ( userAccount.getRole() != Role.DOCTOR ) {
				throw new IllegalArgumentException("UserAccount provided for Doctor must have DOCTOR role.");
			}
			existingDoctor.setUserAccount(userAccount);
		} else {
			throw new IllegalArgumentException("UserAccountId cannot be null when updating a Doctor.");
		}

		return doctorRepository.save(existingDoctor);
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

	private Doctor convertToEntity(DoctorDTO doctorDTO) {
		Doctor doctor = new Doctor();
		if ( doctorDTO.getUserAccountId() == null ) {
			throw new IllegalArgumentException("UserAccountId is required for a Doctor entity.");
		}

		UserAccount userAccount = userAccountRepository.findById(doctorDTO.getUserAccountId())
			.orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + doctorDTO.getUserAccountId()));
		doctor.setUserAccount(userAccount);

		doctor.setFirstName(doctorDTO.getFirstName());
		doctor.setLastName(doctorDTO.getLastName());
		doctor.setSpecialty(doctorDTO.getSpecialty());
		doctor.setContactNumber(doctorDTO.getContactNumber());
		doctor.setEmail(doctorDTO.getEmail());
		return doctor;
	}
}
