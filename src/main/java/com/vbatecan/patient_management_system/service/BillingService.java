package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.BillingDTO;
import java.util.List;
import java.util.Optional;

public interface BillingService {
    BillingDTO save(BillingDTO billingDTO); // Renamed from createBilling
    Optional<BillingDTO> getBillingById(Integer id);
    List<BillingDTO> getAllBillings();
    List<BillingDTO> getBillingsByPatientId(Integer patientId);
    List<BillingDTO> getBillingsByAppointmentId(Integer appointmentId);
    BillingDTO update(Integer id, BillingDTO billingDTO); // Renamed from updateBilling
    BillingDTO updateBillingStatus(Integer id, String status);
    void delete(Integer id); // Renamed from deleteBilling
}
