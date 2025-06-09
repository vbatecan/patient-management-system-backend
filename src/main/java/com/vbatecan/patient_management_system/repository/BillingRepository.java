package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {
    Page<Billing> findByPatientId(Integer patientId, Pageable pageable);
    Page<Billing> findByAppointmentId(Integer appointmentId, Pageable pageable);
}
