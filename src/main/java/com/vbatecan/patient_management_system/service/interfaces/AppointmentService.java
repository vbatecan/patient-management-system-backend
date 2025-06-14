package com.vbatecan.patient_management_system.service.interfaces;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.input.AppointmentInput;
import com.vbatecan.patient_management_system.model.update.AppointmentUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppointmentService {
	Appointment save(AppointmentInput appointmentDTO) throws IllegalArgumentException;

	Optional<Appointment> findById(Integer id);

	Page<Appointment> findAll(Pageable pageable);

	Page<Appointment> findByPatientId(Integer patientId, Pageable pageable);

	Page<Appointment> findByDoctorId(Integer doctorId, Pageable pageable);

	Appointment update(Integer id, AppointmentUpdate appointmentUpdate) throws JsonMappingException, JsonMappingException;

	Appointment updateAppointmentStatus(Integer id, String status);

	void delete(Integer id) throws ResourceNotFoundException;
}
