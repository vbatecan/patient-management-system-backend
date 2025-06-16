package com.vbatecan.patient_management_system.service.interfaces;

import com.vbatecan.patient_management_system.model.dto.BillingDTO;
import com.vbatecan.patient_management_system.model.entities.Billing;
import com.vbatecan.patient_management_system.model.input.BillingInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BillingService {
	Billing save(BillingInput billingDTO);

	Optional<Billing> findById(Integer id);

	Page<Billing> findAll(Pageable pageable);

	Page<Billing> findByPatientId(Integer patientId, Pageable pageable);

	Page<Billing> findByAppointmentId(Integer appointmentId, Pageable pageable);

	Billing update(Integer id, BillingDTO billingDTO);

	Billing updateBillingStatus(Integer id, String status);

	void delete(Integer id);
}
