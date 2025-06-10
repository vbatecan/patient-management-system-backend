package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.service.PatientService;
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
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "APIs for managing patients")
public class PatientController {

	private final PatientService patientService;

	@Operation(summary = "Create a new patient", description = "Creates a new patient record in the system.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Patient created successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "404", description = "Associated UserAccount not found",
			content = @Content(mediaType = "text/plain"))
	})
	@PostMapping
	public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
		Patient savedPatient = patientService.save(patientDTO);
		return new ResponseEntity<>(convertToDTO(savedPatient), HttpStatus.CREATED);
	}

	@Operation(summary = "Get a patient by ID", description = "Retrieves a specific patient by their unique ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Patient found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
		@ApiResponse(responseCode = "404", description = "Patient not found",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/{id}")
	public ResponseEntity<PatientDTO> getPatientById(@PathVariable Integer id) {
		Optional<Patient> patientOptional = patientService.findById(id);
		return patientOptional
			.map(patient -> ResponseEntity.ok(convertToDTO(patient)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get all patients", description = "Retrieves a paginated list of all patients.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of patients",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
	})
	@GetMapping
	public ResponseEntity<Page<PatientDTO>> getAllPatients(Pageable pageable) {
		Page<Patient> patients = patientService.findAll(pageable);
		Page<PatientDTO> patientDTOs = patients.map(this::convertToDTO);
		return ResponseEntity.ok(patientDTOs);
	}

	@Operation(summary = "Update an existing patient", description = "Updates the details of an existing patient by their ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Patient updated successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "404", description = "Patient or associated UserAccount not found",
			content = @Content(mediaType = "text/plain"))
	})
	@PutMapping("/{id}")
	public ResponseEntity<PatientDTO> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientDTO patientDTO) {
		Patient updatedPatient = patientService.update(id, patientDTO);
		return ResponseEntity.ok(convertToDTO(updatedPatient));
	}

	@Operation(summary = "Delete a patient", description = "Deletes a patient by their ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Patient not found",
			content = @Content(mediaType = "text/plain"))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
		patientService.delete(id);
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
	
	private PatientDTO convertToDTO(Patient patient) {
		PatientDTO dto = new PatientDTO();
		dto.setId(patient.getId());
		if (patient.getUserAccount() != null) {
			dto.setUserAccountId(patient.getUserAccount().getId());
		}
		dto.setFirstName(patient.getFirstName());
		dto.setLastName(patient.getLastName());
		dto.setDateOfBirth(patient.getDateOfBirth());
		dto.setGender(patient.getGender());
		dto.setContactNumber(patient.getContactNumber());
		dto.setEmail(patient.getEmail());
		dto.setAddress(patient.getAddress());
		dto.setEmergencyContact(patient.getEmergencyContact());
		dto.setCreatedAt(patient.getCreatedAt());
		dto.setUpdatedAt(patient.getUpdatedAt());
		return dto;
	}
}
