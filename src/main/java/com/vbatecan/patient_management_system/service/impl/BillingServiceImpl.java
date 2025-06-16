package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.BillingDTO;
import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.entities.Billing;
import com.vbatecan.patient_management_system.model.entities.Patient;
import com.vbatecan.patient_management_system.repository.AppointmentRepository;
import com.vbatecan.patient_management_system.repository.BillingRepository;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.service.interfaces.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

	private final BillingRepository billingRepository;
	private final PatientRepository patientRepository;
	private final AppointmentRepository appointmentRepository;

	@Override
	@Transactional
	public Billing save(BillingDTO billingDTO) {
		Billing billing = convertToEntity(billingDTO);
		billing.setCreatedAt(LocalDateTime.now());
		billing.setUpdatedAt(LocalDateTime.now());
		if ( billingDTO.getStatus() != null ) {
			billing.setStatus(billingDTO.getStatus());
		}
		return billingRepository.save(billing);
	}

	@Override
	public Optional<Billing> findById(Integer id) {
		return billingRepository.findById(id);
	}

	@Override
	public Page<Billing> findAll(Pageable pageable) {
		return billingRepository.findAll(pageable);
	}

	@Override
	public Page<Billing> findByPatientId(Integer patientId, Pageable pageable) {
		if ( !patientRepository.existsById(patientId) ) {
			throw new ResourceNotFoundException("Patient not found with id: " + patientId);
		}
		return billingRepository.findByPatientId(patientId, pageable);
	}

	@Override
	public Page<Billing> findByAppointmentId(Integer appointmentId, Pageable pageable) {
		if ( !appointmentRepository.existsById(appointmentId) ) {
			throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
		}
		return billingRepository.findByAppointmentId(appointmentId, pageable);
	}

	@Override
	@Transactional
	public Billing update(Integer id, BillingDTO billingDTO) {
		Billing existingBilling = billingRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Billing not found with id: " + id));

		if ( billingDTO.getPatientId() != null ) {
			Patient patient = patientRepository.findById(billingDTO.getPatientId())
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + billingDTO.getPatientId()));
			existingBilling.setPatient(patient);
		}
		if ( billingDTO.getAppointmentId() != null ) {
			Appointment appointment = appointmentRepository.findById(billingDTO.getAppointmentId())
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + billingDTO.getAppointmentId()));
			existingBilling.setAppointment(appointment);
		}

		existingBilling.setAmount(billingDTO.getAmount());
		if ( billingDTO.getStatus() != null ) {
			existingBilling.setStatus(billingDTO.getStatus());
		}
		existingBilling.setBillingDate(billingDTO.getBillingDate());
		existingBilling.setUpdatedAt(LocalDateTime.now());

		return billingRepository.save(existingBilling);
	}

	@Override
	@Transactional
	public Billing updateBillingStatus(Integer id, String status) {
		Billing existingBilling = billingRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Billing not found with id: " + id));
		existingBilling.setStatus(status);
		existingBilling.setUpdatedAt(LocalDateTime.now());
		return billingRepository.save(existingBilling);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if ( !billingRepository.existsById(id) ) {
			throw new ResourceNotFoundException("Billing not found with id: " + id);
		}
		billingRepository.deleteById(id);
	}

	// This DTO conversion method is kept for potential internal use or by other layers,
	// but it's not used by the public methods of this service anymore.
	private BillingDTO convertToDTO(Billing billing) {
		BillingDTO dto = new BillingDTO();
		dto.setId(billing.getId());
		if ( billing.getPatient() != null ) {
			dto.setPatientId(billing.getPatient().getId());
		}
		if ( billing.getAppointment() != null ) {
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

		if ( billingDTO.getPatientId() == null ) {
			throw new IllegalArgumentException("Patient ID cannot be null for a new billing record.");
		}
		Patient patient = patientRepository.findById(billingDTO.getPatientId())
			.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + billingDTO.getPatientId()));
		billing.setPatient(patient);

		if ( billingDTO.getAppointmentId() == null ) {
			throw new IllegalArgumentException("Appointment ID cannot be null for a new billing record.");
		}
		Appointment appointment = appointmentRepository.findById(billingDTO.getAppointmentId())
			.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + billingDTO.getAppointmentId()));
		billing.setAppointment(appointment);

		billing.setAmount(billingDTO.getAmount());
		billing.setBillingDate(billingDTO.getBillingDate());
		// Timestamps (createdAt, updatedAt) are handled in save/update methods.
		return billing;
	}
}
