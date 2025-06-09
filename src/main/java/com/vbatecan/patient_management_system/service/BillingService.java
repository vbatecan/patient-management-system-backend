package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.BillingDTO;
import java.util.List;
import java.util.Optional;

public interface BillingService {
    BillingDTO createBilling(BillingDTO billingDTO);
    Optional<BillingDTO> getBillingById(Integer id);
    List<BillingDTO> getAllBillings();
    List<BillingDTO> getBillingsByPatientId(Integer patientId);
    List<BillingDTO> getBillingsByAppointmentId(Integer appointmentId);
    BillingDTO updateBilling(Integer id, BillingDTO billingDTO);
    BillingDTO updateBillingStatus(Integer id, String status);
    void deleteBilling(Integer id);
}
