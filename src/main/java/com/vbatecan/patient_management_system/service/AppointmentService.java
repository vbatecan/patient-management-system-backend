package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppointmentService {
	AppointmentDTO save(AppointmentDTO appointmentDTO);

	Optional<AppointmentDTO> findById(Integer id);

	Page<AppointmentDTO> findAll(Pageable pageable);

	Page<AppointmentDTO> findByPatientId(Integer patientId, Pageable pageable);

	Page<AppointmentDTO> findByDoctorId(Integer doctorId, Pageable pageable);

	AppointmentDTO update(Integer id, AppointmentDTO appointmentDTO);

	AppointmentDTO updateAppointmentStatus(Integer id, String status);

	void delete(Integer id);
}
