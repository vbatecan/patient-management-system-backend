package com.vbatecan.patient_management_system.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.input.AppointmentInput;
import com.vbatecan.patient_management_system.model.update.AppointmentUpdate;
import com.vbatecan.patient_management_system.repository.AppointmentRepository;
import com.vbatecan.patient_management_system.repository.DoctorRepository;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;
	private final DoctorRepository doctorRepository;
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public Appointment save(AppointmentInput appointmentInput) throws IllegalArgumentException {
		// TODO: Implement by creating Input class
		Appointment appointment = mapper.convertValue(appointmentInput, Appointment.class);

		// TODO: The doctor is the currently logged in user.
//		appointment.setDoctor(securityContext.getUserPrincipal().getDoctor())
		appointment.setCreatedAt(LocalDateTime.now());
		appointment.setUpdatedAt(LocalDateTime.now());
		return appointmentRepository.save(appointment);
	}

	@Override
	public Optional<Appointment> findById(Integer id) {
		return appointmentRepository.findById(id);
	}

	@Override
	public Page<Appointment> findAll(Pageable pageable) {
		return appointmentRepository.findAll(pageable);
	}

	@Override
	public Page<Appointment> findByPatientId(Integer patientId, Pageable pageable) throws ResourceNotFoundException {
		if ( !patientRepository.existsById(patientId) ) {
			throw new ResourceNotFoundException("Patient not found with id: " + patientId);
		}
		return appointmentRepository.findByPatientId(patientId, pageable);
	}

	@Override
	public Page<Appointment> findByDoctorId(Integer doctorId, Pageable pageable) throws ResourceNotFoundException {
		if ( !doctorRepository.existsById(doctorId) ) {
			throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
		}
		return appointmentRepository.findByDoctorId(doctorId, pageable);
	}

	@Override
	@Transactional
	public Appointment update(Integer id, AppointmentUpdate appointmentUpdate) throws ResourceNotFoundException, JsonMappingException {
		// TODO: Appointment can only be updated by doctor of the patient.
		Appointment appointment = appointmentRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

		Appointment updatedAppointment = mapper.updateValue(appointment, appointmentUpdate);
		updatedAppointment.setUpdatedAt(LocalDateTime.now());
		return appointmentRepository.save(updatedAppointment);
	}

	@Override
	@Transactional
	public Appointment updateAppointmentStatus(Integer id, String status) throws ResourceNotFoundException {
		Appointment existingAppointment = appointmentRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
		existingAppointment.setStatus(status);
		existingAppointment.setUpdatedAt(LocalDateTime.now());
		return appointmentRepository.save(existingAppointment);
	}


	@Override
	@Transactional
	public void delete(Integer id) throws ResourceNotFoundException {
		if ( !appointmentRepository.existsById(id) ) {
			throw new ResourceNotFoundException("Appointment not found with id: " + id);
		}
		appointmentRepository.deleteById(id);
	}
}
