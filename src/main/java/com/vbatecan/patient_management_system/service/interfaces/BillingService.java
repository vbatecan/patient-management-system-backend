package com.vbatecan.patient_management_system.service.interfaces;

import com.vbatecan.patient_management_system.model.dto.BillingDTO;
import com.vbatecan.patient_management_system.model.entities.Billing; // Added import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BillingService {
	Billing save(BillingDTO billingDTO); // Changed return type

	Optional<Billing> findById(Integer id); // Changed return type

	Page<Billing> findAll(Pageable pageable); // Changed return type

	Page<Billing> findByPatientId(Integer patientId, Pageable pageable); // Changed return type

	Page<Billing> findByAppointmentId(Integer appointmentId, Pageable pageable); // Changed return type

	Billing update(Integer id, BillingDTO billingDTO); // Changed return type

	Billing updateBillingStatus(Integer id, String status); // Changed return type

	void delete(Integer id);
}
