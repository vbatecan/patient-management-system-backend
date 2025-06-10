package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Appointment; // Added import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppointmentService {
	Appointment save(AppointmentDTO appointmentDTO); // Changed return type

	Optional<Appointment> findById(Integer id); // Changed return type

	Page<Appointment> findAll(Pageable pageable); // Changed return type

	Page<Appointment> findByPatientId(Integer patientId, Pageable pageable); // Changed return type

	Page<Appointment> findByDoctorId(Integer doctorId, Pageable pageable); // Changed return type

	Appointment update(Integer id, AppointmentDTO appointmentDTO); // Changed return type

	Appointment updateAppointmentStatus(Integer id, String status); // Changed return type

	void delete(Integer id) throws ResourceNotFoundException;
}
