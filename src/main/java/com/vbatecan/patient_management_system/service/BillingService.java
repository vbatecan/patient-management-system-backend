package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.BillingDTO;
import java.util.List;
import java.util.Optional;

public interface BillingService {
    BillingDTO save(BillingDTO billingDTO);
    Optional<BillingDTO> findById(Integer id);
    List<BillingDTO> findAll();
    List<BillingDTO> findByPatientId(Integer patientId);
    List<BillingDTO> findByAppointmentId(Integer appointmentId);
    BillingDTO update(Integer id, BillingDTO billingDTO);
    BillingDTO updateBillingStatus(Integer id, String status);
    void delete(Integer id);
}
