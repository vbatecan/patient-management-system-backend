package com.vbatecan.patient_management_system.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.model.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.responses.MessageResponse;
import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.input.AppointmentInput;
import com.vbatecan.patient_management_system.model.update.AppointmentUpdate;
import com.vbatecan.patient_management_system.service.interfaces.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management", description = "APIs for managing patient appointments")
public class AppointmentController {

	private final AppointmentService appointmentService;
	private final ObjectMapper mapper = new ObjectMapper();

	@Operation(summary = "Create a new appointment", description = "Creates a new appointment with the provided details")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Appointment created successfully",
			content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data"),
		@ApiResponse(responseCode = "404", description = "Patient or doctor not found")
	})
	@PostMapping
	public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentInput appointmentDTO) {
		Appointment savedAppointment = appointmentService.save(appointmentDTO);
		return new ResponseEntity<>(mapper.convertValue(savedAppointment, AppointmentDTO.class), HttpStatus.CREATED);
	}

	@Operation(summary = "Get appointment by ID", description = "Returns an appointment based on its ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Appointment found",
			content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
		@ApiResponse(responseCode = "404", description = "Appointment not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentDTO> getAppointmentById(
		@Parameter(description = "ID of the appointment", required = true) @PathVariable Integer id) {
		Optional<Appointment> appointmentOptional = appointmentService.findById(id);
		return appointmentOptional.map(appointment -> ResponseEntity.ok(mapper.convertValue(appointmentOptional.get(), AppointmentDTO.class)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get all appointments", description = "Returns a paginated list of all appointments")
	@ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully")
	@GetMapping
	public ResponseEntity<Page<AppointmentDTO>> getAllAppointments(
		@Parameter(description = "Pagination parameters") Pageable pageable) {
		Page<Appointment> appointments = appointmentService.findAll(pageable);
		Page<AppointmentDTO> appointmentDTOs = appointments.map(appointment -> mapper.convertValue(appointment, AppointmentDTO.class));
		return ResponseEntity.ok(appointmentDTOs);
	}

	@Operation(summary = "Get appointments by patient ID", description = "Returns a paginated list of all appointments for a specific patient")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
		@ApiResponse(responseCode = "404", description = "Patient not found")
	})
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByPatientId(
		@Parameter(description = "ID of the patient", required = true) @PathVariable Integer patientId,
		@Parameter(description = "Pagination parameters") Pageable pageable) {
		Page<Appointment> appointments = appointmentService.findByPatientId(patientId, pageable);
		Page<AppointmentDTO> appointmentDTOs = appointments.map(appointment -> mapper.convertValue(appointment, AppointmentDTO.class));
		return ResponseEntity.ok(appointmentDTOs);
	}

	@Operation(summary = "Get appointments by doctor ID", description = "Returns a paginated list of all appointments for a specific doctor")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully"),
		@ApiResponse(responseCode = "404", description = "Doctor not found")
	})
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByDoctorId(
		@Parameter(description = "ID of the doctor", required = true) @PathVariable Integer doctorId,
		@Parameter(description = "Pagination parameters") Pageable pageable) {
		Page<Appointment> appointments = appointmentService.findByDoctorId(doctorId, pageable);
		Page<AppointmentDTO> appointmentDTOs = appointments.map(appointment -> mapper.convertValue(appointment, AppointmentDTO.class));
		return ResponseEntity.ok(appointmentDTOs);
	}

	@Operation(summary = "Update an appointment", description = "Updates an existing appointment with the provided details")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Appointment updated successfully",
			content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data"),
		@ApiResponse(responseCode = "404", description = "Appointment not found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<?> updateAppointment(
		@Parameter(description = "ID of the appointment to update", required = true) @PathVariable Integer id,
		@RequestBody @Valid AppointmentUpdate appointmentUpdate) throws JsonMappingException {
		try {
			Appointment updatedAppointment = appointmentService.update(id, appointmentUpdate);
			return ResponseEntity.ok(mapper.convertValue(updatedAppointment, AppointmentDTO.class));
		} catch ( ResourceNotFoundException e ) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Update appointment status", description = "Updates the status of an existing appointment")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Appointment status updated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid status provided"),
		@ApiResponse(responseCode = "404", description = "Appointment not found")
	})
	@PatchMapping("/{id}/status")
	public ResponseEntity<AppointmentDTO> updateAppointmentStatus(
		@Parameter(description = "ID of the appointment", required = true) @PathVariable Integer id,
		@RequestBody Map<String, String> statusUpdate) {
		String status = statusUpdate.get("status");
		if ( status == null || status.trim().isEmpty() ) {
			return ResponseEntity.badRequest().build();
		}

		try {
			Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
			return ResponseEntity.ok(mapper.convertValue(updatedAppointment, AppointmentDTO.class));
		} catch ( ResourceNotFoundException e ) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Delete an appointment", description = "Deletes an appointment by its ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Appointment deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Appointment not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAppointment(
		@Parameter(description = "ID of the appointment to delete", required = true) @PathVariable Integer id) {
		try {
			appointmentService.delete(id);
			return ResponseEntity.noContent().build();
		} catch ( ResourceNotFoundException e ) {
			return ResponseEntity.notFound().build();
		}
	}

	// Region: Exception Handler
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<MessageResponse> handleJsonMappingException(JsonMappingException e) {
		return ResponseEntity.badRequest().body(new MessageResponse(
			"Malformed JSON data, please try again with proper json structure.",
			false
		));
	}
}
