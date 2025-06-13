package com.vbatecan.patient_management_system.service.interfaces;

import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.filter.AppointmentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppointmentService {
	Appointment save(AppointmentDTO appointmentDTO);

	Optional<Appointment> findById(Integer id);

	Page<Appointment> findAll(Pageable pageable);

	Page<Appointment> findByPatientId(Integer patientId, Pageable pageable);

	Page<Appointment> findByDoctorId(Integer doctorId, Pageable pageable);

	Appointment update(Integer id, AppointmentDTO appointmentDTO);

	Appointment updateAppointmentStatus(Integer id, String status);

	void delete(Integer id) throws ResourceNotFoundException;
}
