package com.vbatecan.patient_management_system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.dto.BillingDTO;
import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.entities.Billing;
import com.vbatecan.patient_management_system.model.entities.Patient;
import com.vbatecan.patient_management_system.model.input.BillingInput;
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
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public Billing save(BillingInput billingInput) {
		Billing billing = mapper.convertValue(billingInput, Billing.class);
		billing.setCreatedAt(LocalDateTime.now());
		billing.setUpdatedAt(LocalDateTime.now());
		if ( billingInput.getStatus() != null ) {
			billing.setStatus(billingInput.getStatus());
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
}
