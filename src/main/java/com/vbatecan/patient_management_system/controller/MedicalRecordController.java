package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.MedicalRecordDTO;
import com.vbatecan.patient_management_system.model.entities.MedicalRecord;
import com.vbatecan.patient_management_system.service.interfaces.MedicalRecordService;
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
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Record Management", description = "APIs for managing medical records")
public class MedicalRecordController {

	private final MedicalRecordService medicalRecordService;

	@Operation(summary = "Create a new medical record", description = "Creates a new medical record in the system.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Medical record created successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalRecordDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "404", description = "Associated Patient not found",
			content = @Content(mediaType = "text/plain"))
	})
	@PostMapping
	public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
		MedicalRecord savedMedicalRecord = medicalRecordService.save(medicalRecordDTO);
		return new ResponseEntity<>(convertToDTO(savedMedicalRecord), HttpStatus.CREATED);
	}

	@Operation(summary = "Get a medical record by ID", description = "Retrieves a specific medical record by its unique ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Medical record found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalRecordDTO.class))),
		@ApiResponse(responseCode = "404", description = "Medical record not found",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/{id}")
	public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Integer id) {
		Optional<MedicalRecord> medicalRecordOptional = medicalRecordService.findById(id);
		return medicalRecordOptional
			.map(medicalRecord -> ResponseEntity.ok(convertToDTO(medicalRecord)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get all medical records", description = "Retrieves a paginated list of all medical records.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of medical records",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
	})
	@GetMapping
	public ResponseEntity<Page<MedicalRecordDTO>> getAllMedicalRecords(Pageable pageable) {
		Page<MedicalRecord> medicalRecords = medicalRecordService.findAll(pageable);
		Page<MedicalRecordDTO> medicalRecordDTOs = medicalRecords.map(this::convertToDTO);
		return ResponseEntity.ok(medicalRecordDTOs);
	}

	@Operation(summary = "Get medical records by Patient ID", description = "Retrieves a paginated list of medical records for a specific patient.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of medical records for the patient",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
		@ApiResponse(responseCode = "404", description = "Patient not found",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<Page<MedicalRecordDTO>> getMedicalRecordsByPatientId(@PathVariable Integer patientId, Pageable pageable) {
		Page<MedicalRecord> medicalRecords = medicalRecordService.findByPatientId(patientId, pageable);
		Page<MedicalRecordDTO> medicalRecordDTOs = medicalRecords.map(this::convertToDTO);
		return ResponseEntity.ok(medicalRecordDTOs);
	}

	@Operation(summary = "Update an existing medical record", description = "Updates the details of an existing medical record by its ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Medical record updated successfully",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalRecordDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "404", description = "Medical record or associated Patient not found",
			content = @Content(mediaType = "text/plain"))
	})
	@PutMapping("/{id}")
	public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@PathVariable Integer id, @Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
		MedicalRecord updatedMedicalRecord = medicalRecordService.update(id, medicalRecordDTO);
		return ResponseEntity.ok(convertToDTO(updatedMedicalRecord));
	}

	@Operation(summary = "Delete a medical record", description = "Deletes a medical record by its ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Medical record deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Medical record not found",
			content = @Content(mediaType = "text/plain"))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Integer id) {
		medicalRecordService.delete(id);
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
	
	private MedicalRecordDTO convertToDTO(MedicalRecord medicalRecord) {
		MedicalRecordDTO dto = new MedicalRecordDTO();
		dto.setId(medicalRecord.getId());
		if (medicalRecord.getPatient() != null) {
			dto.setPatientId(medicalRecord.getPatient().getId());
		}
		dto.setRecordDate(medicalRecord.getRecordDate());
		dto.setDescription(medicalRecord.getDescription());
		dto.setFilePath(medicalRecord.getFilePath());
		dto.setCreatedAt(medicalRecord.getCreatedAt());
		dto.setUpdatedAt(medicalRecord.getUpdatedAt());
		return dto;
	}
}
