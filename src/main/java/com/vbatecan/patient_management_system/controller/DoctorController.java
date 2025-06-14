package com.vbatecan.patient_management_system.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.DoctorDTO;
import com.vbatecan.patient_management_system.model.responses.MessageResponse;
import com.vbatecan.patient_management_system.model.entities.Doctor;
import com.vbatecan.patient_management_system.model.update.DoctorUpdate;
import com.vbatecan.patient_management_system.service.interfaces.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Management", description = "APIs for managing doctors")
public class DoctorController {

	private final DoctorService doctorService;
	private final ObjectMapper mapper;

	@Operation(summary = "Create a new doctor", description = "Creates a new doctor record in the system.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Doctor created successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data",
			content = @Content(mediaType = "text/plain"))
	})
	@PostMapping
	public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
		Doctor savedDoctor = doctorService.save(doctorDTO);
		return new ResponseEntity<>(mapper.convertValue(savedDoctor, DoctorDTO.class), HttpStatus.CREATED);
	}

	@Operation(summary = "Get a doctor by ID", description = "Retrieves a specific doctor by their unique ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Doctor found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
		@ApiResponse(responseCode = "404", description = "Doctor not found",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/{id}")
	public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Integer id) {
		Optional<Doctor> doctorOptional = doctorService.findById(id);
		return doctorOptional
			.map(doctor -> ResponseEntity.ok(mapper.convertValue(doctor, DoctorDTO.class)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get a doctor by User Account ID", description = "Retrieves a doctor associated with a specific User Account ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Doctor found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
		@ApiResponse(responseCode = "404", description = "Doctor not found for the given User Account ID",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/user-account/{userAccountId}")
	public ResponseEntity<DoctorDTO> getDoctorByUserAccountId(@PathVariable Integer userAccountId) {
		Optional<Doctor> doctorOptional = doctorService.findByUserAccountId(userAccountId);
		return doctorOptional
			.map(doctor -> ResponseEntity.ok(mapper.convertValue(doctor, DoctorDTO.class)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get a doctor by email", description = "Retrieves a doctor by their email address.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Doctor found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
		@ApiResponse(responseCode = "404", description = "Doctor not found with the given email",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/email/{email}")
	public ResponseEntity<DoctorDTO> getDoctorByEmail(@PathVariable String email) {
		Optional<Doctor> doctorOptional = doctorService.findByEmail(email);
		return doctorOptional
			.map(doctor -> ResponseEntity.ok(mapper.convertValue(doctor, DoctorDTO.class)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get all doctors", description = "Retrieves a paginated list of all doctors.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of doctors",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
	})
	@GetMapping
	public ResponseEntity<Page<DoctorDTO>> getAllDoctors(Pageable pageable) {
		Page<Doctor> doctors = doctorService.findAll(pageable);
		Page<DoctorDTO> doctorDTOs = doctors.map(doctor -> mapper.convertValue(doctor, DoctorDTO.class));
		return ResponseEntity.ok(doctorDTOs);
	}

	@Operation(summary = "Update an existing doctor", description = "Updates the details of an existing doctor by their ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Doctor updated successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "404", description = "Doctor not found",
			content = @Content(mediaType = "text/plain"))
	})
	@PutMapping("/{id}")
	public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Integer id, @Valid @RequestBody DoctorUpdate doctorUpdate) throws JsonMappingException {
		Doctor updatedDoctor = doctorService.update(id, doctorUpdate);
		return ResponseEntity.ok(mapper.convertValue(updatedDoctor, DoctorDTO.class));
	}

	@Operation(summary = "Delete a doctor", description = "Deletes a doctor by their ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Doctor deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Doctor not found",
			content = @Content(mediaType = "text/plain"))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDoctor(@PathVariable Integer id) {
		doctorService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	// Region: Exception Handlers
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<MessageResponse> handleJsonMappingException(JsonMappingException ex) {
		return ResponseEntity.badRequest().body(new MessageResponse(
			"Malformed JSON data, please try again with proper json structure.",
			false
		));
	}
}
