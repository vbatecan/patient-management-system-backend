package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.DoctorDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Doctor;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.DoctorRepository;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public DoctorDTO save(DoctorDTO doctorDTO) {
		if ( doctorDTO.getUserAccountId() != null ) {
			UserAccount ua = userAccountRepository.findById(doctorDTO.getUserAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + doctorDTO.getUserAccountId()));
			if ( ua.getRole() != UserAccount.Role.DOCTOR ) {
				throw new IllegalArgumentException("UserAccount provided for Doctor must have DOCTOR role.");
			}
		} else {
			throw new IllegalArgumentException("UserAccountId is required to create a Doctor.");
		}

		Doctor doctor = convertToEntity(doctorDTO);
		doctor.setCreatedAt(LocalDateTime.now());
		doctor.setUpdatedAt(LocalDateTime.now());
		Doctor savedDoctor = doctorRepository.save(doctor);
		return convertToDTO(savedDoctor);
	}

	@Override
	public Optional<DoctorDTO> findById(Integer id) {
		return doctorRepository.findById(id).map(this::convertToDTO);
	}

	@Override
	public Optional<DoctorDTO> findByUserAccountId(Integer userAccountId) {
		return doctorRepository.findByUserAccountId(userAccountId).map(this::convertToDTO);
	}

	@Override
	public Optional<DoctorDTO> findByEmail(String email) {
		return doctorRepository.findByEmail(email).map(this::convertToDTO);
	}


	@Override
	public Page<DoctorDTO> findAll(Pageable pageable) {
		return doctorRepository.findAll(pageable).map(this::convertToDTO);
	}

	@Override
	@Transactional
	public DoctorDTO update(Integer id, DoctorDTO doctorDTO) {
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
			if ( userAccount.getRole() != UserAccount.Role.DOCTOR ) {
				throw new IllegalArgumentException("UserAccount provided for Doctor must have DOCTOR role.");
			}
			existingDoctor.setUserAccount(userAccount);
		} else {
			throw new IllegalArgumentException("UserAccountId cannot be null when updating a Doctor.");
		}


		Doctor updatedDoctor = doctorRepository.save(existingDoctor);
		return convertToDTO(updatedDoctor);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if ( !doctorRepository.existsById(id) ) {
			throw new ResourceNotFoundException("Doctor not found with id: " + id);
		}
		doctorRepository.deleteById(id);
	}

	private DoctorDTO convertToDTO(Doctor doctor) {
		DoctorDTO dto = new DoctorDTO();
		dto.setId(doctor.getId());
		if ( doctor.getUserAccount() != null ) {
			dto.setUserAccountId(doctor.getUserAccount().getId());
		}
		dto.setFirstName(doctor.getFirstName());
		dto.setLastName(doctor.getLastName());
		dto.setSpecialty(doctor.getSpecialty());
		dto.setContactNumber(doctor.getContactNumber());
		dto.setEmail(doctor.getEmail());
		dto.setCreatedAt(doctor.getCreatedAt());
		dto.setUpdatedAt(doctor.getUpdatedAt());
		return dto;
	}

	private Doctor convertToEntity(DoctorDTO doctorDTO) {
		Doctor doctor = new Doctor();
		if ( doctorDTO.getUserAccountId() == null ) {
			throw new IllegalArgumentException("UserAccountId is required for a Doctor.");
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
