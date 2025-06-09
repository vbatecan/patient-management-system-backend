package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.BillingDTO;
import java.util.List;
import java.util.Optional;

public interface BillingService {
    BillingDTO save(BillingDTO billingDTO);
    Optional<BillingDTO> findById(Integer id); // Renamed from getBillingById
    List<BillingDTO> findAll(); // Renamed from getAllBillings
    List<BillingDTO> findByPatientId(Integer patientId); // Renamed from getBillingsByPatientId
    List<BillingDTO> findByAppointmentId(Integer appointmentId); // Renamed from getBillingsByAppointmentId
    BillingDTO update(Integer id, BillingDTO billingDTO);
    BillingDTO updateBillingStatus(Integer id, String status);
    void delete(Integer id);
}
