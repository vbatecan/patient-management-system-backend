package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.dto.BillingDTO;
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
            BillingDTO savedBilling = billingService.save(billingDTO);
            return new ResponseEntity<>(savedBilling, HttpStatus.CREATED);
        } catch (java.lang.IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingDTO> getBillingById(@PathVariable Integer id) {
        Optional<BillingDTO> billingDTO = billingService.findById(id);
        return billingDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<BillingDTO>> getAllBillings(Pageable pageable) {
        Page<BillingDTO> billings = billingService.findAll(pageable);
        return ResponseEntity.ok(billings);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<BillingDTO>> getBillingsByPatientId(@PathVariable Integer patientId, Pageable pageable) {
        Page<BillingDTO> billings = billingService.findByPatientId(patientId, pageable);
        return ResponseEntity.ok(billings);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Page<BillingDTO>> getBillingsByAppointmentId(@PathVariable Integer appointmentId, Pageable pageable) {
        Page<BillingDTO> billings = billingService.findByAppointmentId(appointmentId, pageable);
        return ResponseEntity.ok(billings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingDTO> updateBilling(@PathVariable Integer id, @RequestBody BillingDTO billingDTO) {
        BillingDTO updatedBilling = billingService.update(id, billingDTO);
        return ResponseEntity.ok(updatedBilling);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BillingDTO> updateBillingStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        BillingDTO updatedBilling = billingService.updateBillingStatus(id, status);
        return ResponseEntity.ok(updatedBilling);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Integer id) {
        billingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
