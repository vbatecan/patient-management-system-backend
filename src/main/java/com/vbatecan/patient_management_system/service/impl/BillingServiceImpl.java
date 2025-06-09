package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.BillingDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Appointment;
import com.vbatecan.patient_management_system.model.Billing;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.repository.AppointmentRepository;
import com.vbatecan.patient_management_system.repository.BillingRepository;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public BillingDTO save(BillingDTO billingDTO) {
        Billing billing = convertToEntity(billingDTO);
        billing.setCreatedAt(LocalDateTime.now());
        billing.setUpdatedAt(LocalDateTime.now());
        if (billingDTO.getStatus() != null) {
            billing.setStatus(billingDTO.getStatus());
        }
        // If DTO status is null, the entity's default status "PENDING" will be used.
        Billing savedBilling = billingRepository.save(billing);
        return convertToDTO(savedBilling);
    }

    @Override
    public Optional<BillingDTO> getBillingById(Integer id) {
        return billingRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<BillingDTO> getAllBillings() {
        return billingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillingDTO> getBillingsByPatientId(Integer patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return billingRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillingDTO> getBillingsByAppointmentId(Integer appointmentId) {
         if (!appointmentRepository.existsById(appointmentId)) {
            throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
        }
        return billingRepository.findByAppointmentId(appointmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BillingDTO update(Integer id, BillingDTO billingDTO) {
        Billing existingBilling = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billing not found with id: " + id));

        if (billingDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(billingDTO.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + billingDTO.getPatientId()));
            existingBilling.setPatient(patient);
        }
         if (billingDTO.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(billingDTO.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + billingDTO.getAppointmentId()));
            existingBilling.setAppointment(appointment);
        }

        existingBilling.setAmount(billingDTO.getAmount());
        if (billingDTO.getStatus() != null) {
            existingBilling.setStatus(billingDTO.getStatus());
        }
        existingBilling.setBillingDate(billingDTO.getBillingDate());
        existingBilling.setUpdatedAt(LocalDateTime.now());

        Billing updatedBilling = billingRepository.save(existingBilling);
        return convertToDTO(updatedBilling);
    }

    @Override
    @Transactional
    public BillingDTO updateBillingStatus(Integer id, String status) {
        Billing existingBilling = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billing not found with id: " + id));
        existingBilling.setStatus(status);
        existingBilling.setUpdatedAt(LocalDateTime.now());
        Billing updatedBilling = billingRepository.save(existingBilling);
        return convertToDTO(updatedBilling);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!billingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Billing not found with id: " + id);
        }
        billingRepository.deleteById(id);
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

    private Billing convertToEntity(BillingDTO billingDTO) {
        Billing billing = new Billing();

        if (billingDTO.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID cannot be null for a new billing record.");
        }
        Patient patient = patientRepository.findById(billingDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + billingDTO.getPatientId()));
        billing.setPatient(patient);

        if (billingDTO.getAppointmentId() == null) {
             throw new IllegalArgumentException("Appointment ID cannot be null for a new billing record.");
        }
        Appointment appointment = appointmentRepository.findById(billingDTO.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + billingDTO.getAppointmentId()));
        billing.setAppointment(appointment);

        billing.setAmount(billingDTO.getAmount());
        billing.setBillingDate(billingDTO.getBillingDate());
        return billing;
    }
}
