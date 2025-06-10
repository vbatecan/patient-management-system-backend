package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Appointment;
import com.vbatecan.patient_management_system.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;

	@PostMapping
	public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
		Appointment savedAppointment = appointmentService.save(appointmentDTO);
		return new ResponseEntity<>(convertToDTO(savedAppointment), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Integer id) {
		Optional<Appointment> appointmentOptional = appointmentService.findById(id);
		return appointmentOptional.map(appointment -> ResponseEntity.ok(convertToDTO(appointment)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<Page<AppointmentDTO>> getAllAppointments(Pageable pageable) {
		Page<Appointment> appointments = appointmentService.findAll(pageable);
		Page<AppointmentDTO> appointmentDTOs = appointments.map(this::convertToDTO);
		return ResponseEntity.ok(appointmentDTOs);
	}

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByPatientId(@PathVariable Integer patientId, Pageable pageable) {
		Page<Appointment> appointments = appointmentService.findByPatientId(patientId, pageable);
		Page<AppointmentDTO> appointmentDTOs = appointments.map(this::convertToDTO);
		return ResponseEntity.ok(appointmentDTOs);
	}

	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByDoctorId(@PathVariable Integer doctorId, Pageable pageable) {
		Page<Appointment> appointments = appointmentService.findByDoctorId(doctorId, pageable);
		Page<AppointmentDTO> appointmentDTOs = appointments.map(this::convertToDTO);
		return ResponseEntity.ok(appointmentDTOs);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Integer id, @RequestBody AppointmentDTO appointmentDTO) {
		try {
			Appointment updatedAppointment = appointmentService.update(id, appointmentDTO);
			return ResponseEntity.ok(convertToDTO(updatedAppointment));
		} catch ( ResourceNotFoundException e ) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<AppointmentDTO> updateAppointmentStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusUpdate) {
		String status = statusUpdate.get("status");
		if ( status == null || status.trim().isEmpty() ) {
			return ResponseEntity.badRequest().build();
		}
		try {
			Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
			return ResponseEntity.ok(convertToDTO(updatedAppointment));
		} catch ( ResourceNotFoundException e ) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
		try {
			appointmentService.delete(id);
			return ResponseEntity.noContent().build();
		} catch ( ResourceNotFoundException e ) {
			return ResponseEntity.notFound().build();
		}
	}
	
	private AppointmentDTO convertToDTO(Appointment appointment) {
		AppointmentDTO dto = new AppointmentDTO();
		
		dto.setId(appointment.getId());
		
		if (appointment.getPatient() != null) {
			dto.setPatientId(appointment.getPatient().getId());
		}
		
		if (appointment.getDoctor() != null) {
			dto.setDoctorId(appointment.getDoctor().getId());
		}
		
		dto.setAppointmentDate(appointment.getAppointmentDate());
		dto.setReason(appointment.getReason());
		dto.setStatus(appointment.getStatus());
		dto.setCreatedAt(appointment.getCreatedAt());
		dto.setUpdatedAt(appointment.getUpdatedAt());
		return dto;
	}
}
