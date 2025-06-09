package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.service.AppointmentService;
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
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO savedAppointment = appointmentService.save(appointmentDTO);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Integer id) {
        Optional<AppointmentDTO> appointmentDTO = appointmentService.findById(id);
        return appointmentDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentDTO>> getAllAppointments(Pageable pageable) {
        Page<AppointmentDTO> appointments = appointmentService.findAll(pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByPatientId(@PathVariable Integer patientId, Pageable pageable) {
        Page<AppointmentDTO> appointments = appointmentService.findByPatientId(patientId, pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByDoctorId(@PathVariable Integer doctorId, Pageable pageable) {
        Page<AppointmentDTO> appointments = appointmentService.findByDoctorId(doctorId, pageable);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Integer id, @RequestBody AppointmentDTO appointmentDTO) {
        try {
            AppointmentDTO updatedAppointment = appointmentService.update(id, appointmentDTO);
            return ResponseEntity.ok(updatedAppointment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Or throw a custom exception
        }
        try {
            AppointmentDTO updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
            return ResponseEntity.ok(updatedAppointment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
        try {
            appointmentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
             return ResponseEntity.notFound().build();
        }
    }
}
