package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.model.dto.BillingDTO;
import com.vbatecan.patient_management_system.model.entities.Billing;
import com.vbatecan.patient_management_system.service.interfaces.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/billings")
@RequiredArgsConstructor
@Tag(name = "Billing Management", description = "APIs for managing patient billings and payments")
public class BillingController {

	private final BillingService billingService;

	@Operation(summary = "Create a new billing", description = "Creates a new billing record with the provided details")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Billing created successfully", 
				content = @Content(schema = @Schema(implementation = BillingDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data"),
		@ApiResponse(responseCode = "404", description = "Patient or appointment not found")
	})
	@PostMapping
	public ResponseEntity<?> createBilling(@RequestBody BillingDTO billingDTO) {
		try {
			Billing savedBilling = billingService.save(billingDTO);
			return new ResponseEntity<>(convertToDTO(savedBilling), HttpStatus.CREATED);
		} catch (java.lang.IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Get billing by ID", description = "Returns a billing record based on its ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Billing found", 
				content = @Content(schema = @Schema(implementation = BillingDTO.class))),
		@ApiResponse(responseCode = "404", description = "Billing not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<BillingDTO> getBillingById(
			@Parameter(description = "ID of the billing", required = true) @PathVariable Integer id) {
		Optional<Billing> billingOptional = billingService.findById(id);
		return billingOptional.map(billing -> ResponseEntity.ok(convertToDTO(billing)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get all billings", description = "Returns a paginated list of all billing records")
	@ApiResponse(responseCode = "200", description = "List of billings retrieved successfully")
	@GetMapping
	public ResponseEntity<Page<BillingDTO>> getAllBillings(
			@Parameter(description = "Pagination parameters") Pageable pageable) {
		Page<Billing> billings = billingService.findAll(pageable);
		Page<BillingDTO> billingDTOs = billings.map(this::convertToDTO);
		return ResponseEntity.ok(billingDTOs);
	}

	@Operation(summary = "Get billings by patient ID", description = "Returns a paginated list of all billing records for a specific patient")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "List of billings retrieved successfully"),
		@ApiResponse(responseCode = "404", description = "Patient not found")
	})
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<Page<BillingDTO>> getBillingsByPatientId(
			@Parameter(description = "ID of the patient", required = true) @PathVariable Integer patientId, 
			@Parameter(description = "Pagination parameters") Pageable pageable) {
		Page<Billing> billings = billingService.findByPatientId(patientId, pageable);
		Page<BillingDTO> billingDTOs = billings.map(this::convertToDTO);
		return ResponseEntity.ok(billingDTOs);
	}

	@Operation(summary = "Get billings by appointment ID", description = "Returns a paginated list of all billing records for a specific appointment")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "List of billings retrieved successfully"),
		@ApiResponse(responseCode = "404", description = "Appointment not found")
	})
	@GetMapping("/appointment/{appointmentId}")
	public ResponseEntity<Page<BillingDTO>> getBillingsByAppointmentId(
			@Parameter(description = "ID of the appointment", required = true) @PathVariable Integer appointmentId, 
			@Parameter(description = "Pagination parameters") Pageable pageable) {
		Page<Billing> billings = billingService.findByAppointmentId(appointmentId, pageable);
		Page<BillingDTO> billingDTOs = billings.map(this::convertToDTO);
		return ResponseEntity.ok(billingDTOs);
	}

	@Operation(summary = "Update a billing", description = "Updates an existing billing record with the provided details")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Billing updated successfully", 
				content = @Content(schema = @Schema(implementation = BillingDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data"),
		@ApiResponse(responseCode = "404", description = "Billing not found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<BillingDTO> updateBilling(
			@Parameter(description = "ID of the billing to update", required = true) @PathVariable Integer id, 
			@RequestBody BillingDTO billingDTO) {
		Billing updatedBilling = billingService.update(id, billingDTO);
		return ResponseEntity.ok(convertToDTO(updatedBilling));
	}

	@Operation(summary = "Update billing status", description = "Updates the payment status of an existing billing record")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Billing status updated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid status provided"),
		@ApiResponse(responseCode = "404", description = "Billing not found")
	})
	@PatchMapping("/{id}/status")
	public ResponseEntity<BillingDTO> updateBillingStatus(
			@Parameter(description = "ID of the billing", required = true) @PathVariable Integer id, 
			@RequestBody Map<String, String> statusUpdate) {
		String status = statusUpdate.get("status");
		if (status == null || status.trim().isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Billing updatedBilling = billingService.updateBillingStatus(id, status);
		return ResponseEntity.ok(convertToDTO(updatedBilling));
	}

	@Operation(summary = "Delete a billing", description = "Deletes a billing record by its ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Billing deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Billing not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBilling(
			@Parameter(description = "ID of the billing to delete", required = true) @PathVariable Integer id) {
		billingService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	private BillingDTO convertToDTO(Billing billing) {
		BillingDTO dto = new BillingDTO();
		dto.setId(billing.getId());
		if (billing.getPatient() != null) {
			dto.setPatientId(billing.getPatient().getId());
		}
		if (billing.getAppointment() != null) {
			dto.setAppointmentId(billing.getAppointment().getId());
		}
		dto.setAmount(billing.getAmount());
		dto.setStatus(billing.getStatus());
		dto.setBillingDate(billing.getBillingDate());
		dto.setCreatedAt(billing.getCreatedAt());
		dto.setUpdatedAt(billing.getUpdatedAt());
		return dto;
	}
}
