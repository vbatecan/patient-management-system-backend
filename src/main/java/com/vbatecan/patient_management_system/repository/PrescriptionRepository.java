package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByAppointmentId(Integer appointmentId);
}
