package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.PrescriptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PrescriptionService {
	PrescriptionDTO save(PrescriptionDTO prescriptionDTO);

	Optional<PrescriptionDTO> findById(Integer id);

	Page<PrescriptionDTO> findAll(Pageable pageable);

	Page<PrescriptionDTO> findByAppointmentId(Integer appointmentId, Pageable pageable);

	PrescriptionDTO update(Integer id, PrescriptionDTO prescriptionDTO);

	void delete(Integer id);
}
