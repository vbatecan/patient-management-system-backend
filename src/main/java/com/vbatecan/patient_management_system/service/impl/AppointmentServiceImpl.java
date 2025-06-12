package com.vbatecan.patient_management_system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Appointment;
import com.vbatecan.patient_management_system.model.Doctor;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.repository.AppointmentRepository;
import com.vbatecan.patient_management_system.repository.DoctorRepository;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;
	private final DoctorRepository doctorRepository;
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public Appointment save(@Valid AppointmentDTO appointmentDTO) {
		Appointment appointment = mapper.convertValue(appointmentDTO, Appointment.class);
		appointment.setCreatedAt(LocalDateTime.now());
		appointment.setUpdatedAt(LocalDateTime.now());
		if ( appointmentDTO.getStatus() != null ) {
			appointment.setStatus(appointmentDTO.getStatus());
		}

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
	public Appointment update(Integer id, AppointmentDTO appointmentDTO) throws ResourceNotFoundException {
		Appointment existingAppointment = appointmentRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

		if ( appointmentDTO.getPatient().getId() != null ) {
			Patient patient = patientRepository.findById(appointmentDTO.getPatient().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointmentDTO.getPatient().getId()));
			existingAppointment.setPatient(patient);
		}
		if ( appointmentDTO.getDoctor().getId() != null ) {
			Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctor().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointmentDTO.getDoctor().getId()));
			existingAppointment.setDoctor(doctor);
		}
		existingAppointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
		existingAppointment.setReason(appointmentDTO.getReason());
		if ( appointmentDTO.getStatus() != null ) {
			existingAppointment.setStatus(appointmentDTO.getStatus());
		}
		existingAppointment.setUpdatedAt(LocalDateTime.now());

		return appointmentRepository.save(existingAppointment);
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
