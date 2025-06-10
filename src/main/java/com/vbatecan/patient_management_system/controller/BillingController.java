package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.dto.BillingDTO;
import com.vbatecan.patient_management_system.model.Billing;
import com.vbatecan.patient_management_system.service.BillingService;
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
public class BillingController {

	private final BillingService billingService;

	@PostMapping
	public ResponseEntity<?> createBilling(@RequestBody BillingDTO billingDTO) {
		try {
			Billing savedBilling = billingService.save(billingDTO);
			return new ResponseEntity<>(convertToDTO(savedBilling), HttpStatus.CREATED);
		} catch (java.lang.IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<BillingDTO> getBillingById(@PathVariable Integer id) {
		Optional<Billing> billingOptional = billingService.findById(id);
		return billingOptional.map(billing -> ResponseEntity.ok(convertToDTO(billing)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<Page<BillingDTO>> getAllBillings(Pageable pageable) {
		Page<Billing> billings = billingService.findAll(pageable);
		Page<BillingDTO> billingDTOs = billings.map(this::convertToDTO);
		return ResponseEntity.ok(billingDTOs);
	}

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<Page<BillingDTO>> getBillingsByPatientId(@PathVariable Integer patientId, Pageable pageable) {
		Page<Billing> billings = billingService.findByPatientId(patientId, pageable);
		Page<BillingDTO> billingDTOs = billings.map(this::convertToDTO);
		return ResponseEntity.ok(billingDTOs);
	}

	@GetMapping("/appointment/{appointmentId}")
	public ResponseEntity<Page<BillingDTO>> getBillingsByAppointmentId(@PathVariable Integer appointmentId, Pageable pageable) {
		Page<Billing> billings = billingService.findByAppointmentId(appointmentId, pageable);
		Page<BillingDTO> billingDTOs = billings.map(this::convertToDTO);
		return ResponseEntity.ok(billingDTOs);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BillingDTO> updateBilling(@PathVariable Integer id, @RequestBody BillingDTO billingDTO) {
		Billing updatedBilling = billingService.update(id, billingDTO);
		return ResponseEntity.ok(convertToDTO(updatedBilling));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<BillingDTO> updateBillingStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusUpdate) {
		String status = statusUpdate.get("status");
		if (status == null || status.trim().isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Billing updatedBilling = billingService.updateBillingStatus(id, status);
		return ResponseEntity.ok(convertToDTO(updatedBilling));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBilling(@PathVariable Integer id) {
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
