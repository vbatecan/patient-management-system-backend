package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {
    List<Billing> findByPatientId(Integer patientId);
    List<Billing> findByAppointmentId(Integer appointmentId);
}
