package com.vbatecan.patient_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.PrescriptionDTO;
import com.vbatecan.patient_management_system.model.entities.Prescription;
import com.vbatecan.patient_management_system.service.interfaces.PrescriptionService;
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
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescription Management", description = "APIs for managing prescriptions")
public class PrescriptionController {

	private final PrescriptionService prescriptionService;
	private final ObjectMapper mapper = new ObjectMapper();

	@Operation(summary = "Create a new prescription", description = "Creates a new prescription in the system.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Prescription created successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data (e.g., missing appointmentId)",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "404", description = "Associated Appointment not found",
			content = @Content(mediaType = "text/plain"))
	})
	@PostMapping
	public ResponseEntity<PrescriptionDTO> createPrescription(@Valid @RequestBody PrescriptionDTO prescriptionDTO) {
		Prescription savedPrescription = prescriptionService.save(prescriptionDTO);
		return new ResponseEntity<>(mapper.convertValue(savedPrescription, PrescriptionDTO.class), HttpStatus.CREATED);
	}

	@Operation(summary = "Get a prescription by ID", description = "Retrieves a specific prescription by its unique ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Prescription found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDTO.class))),
		@ApiResponse(responseCode = "404", description = "Prescription not found",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/{id}")
	public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Integer id) {
		Optional<Prescription> prescriptionOptional = prescriptionService.findById(id);
		return prescriptionOptional
			.map(prescription -> ResponseEntity.ok(mapper.convertValue(prescription, PrescriptionDTO.class)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get all prescriptions", description = "Retrieves a paginated list of all prescriptions.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of prescriptions",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
	})
	@GetMapping
	public ResponseEntity<Page<PrescriptionDTO>> getAllPrescriptions(Pageable pageable) {
		Page<Prescription> prescriptions = prescriptionService.findAll(pageable);
		Page<PrescriptionDTO> prescriptionDTOs = prescriptions.map(prescription -> mapper.convertValue(prescription, PrescriptionDTO.class));
		return ResponseEntity.ok(prescriptionDTOs);
	}

	@Operation(summary = "Get prescriptions by Appointment ID", description = "Retrieves a paginated list of prescriptions for a specific appointment.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of prescriptions for the appointment",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
		@ApiResponse(responseCode = "404", description = "Appointment not found",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/appointment/{appointmentId}")
	public ResponseEntity<Page<PrescriptionDTO>> getPrescriptionsByAppointmentId(@PathVariable Integer appointmentId, Pageable pageable) {
		Page<Prescription> prescriptions = prescriptionService.findByAppointmentId(appointmentId, pageable);
		Page<PrescriptionDTO> prescriptionDTOs = prescriptions.map(prescription -> mapper.convertValue(prescription, PrescriptionDTO.class));
		return ResponseEntity.ok(prescriptionDTOs);
	}

	@Operation(summary = "Update an existing prescription", description = "Updates the details of an existing prescription by its ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Prescription updated successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data (e.g., missing appointmentId)",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "404", description = "Prescription or associated Appointment not found",
			content = @Content(mediaType = "text/plain"))
	})
	@PutMapping("/{id}")
	public ResponseEntity<PrescriptionDTO> updatePrescription(@PathVariable Integer id, @Valid @RequestBody PrescriptionDTO prescriptionDTO) {
		Prescription updatedPrescription = prescriptionService.update(id, prescriptionDTO);
		return ResponseEntity.ok(mapper.convertValue(updatedPrescription, PrescriptionDTO.class));
	}

	@Operation(summary = "Delete a prescription", description = "Deletes a prescription by its ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Prescription deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Prescription not found",
			content = @Content(mediaType = "text/plain"))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePrescription(@PathVariable Integer id) {
		prescriptionService.delete(id);
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
}
