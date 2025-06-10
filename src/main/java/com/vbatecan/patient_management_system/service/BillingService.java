package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.BillingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BillingService {
	BillingDTO save(BillingDTO billingDTO);

	Optional<BillingDTO> findById(Integer id);

	Page<BillingDTO> findAll(Pageable pageable);

	Page<BillingDTO> findByPatientId(Integer patientId, Pageable pageable);

	Page<BillingDTO> findByAppointmentId(Integer appointmentId, Pageable pageable);

	BillingDTO update(Integer id, BillingDTO billingDTO);

	BillingDTO updateBillingStatus(Integer id, String status);

	void delete(Integer id);
}
