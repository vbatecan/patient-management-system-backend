package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    Page<Prescription> findByAppointmentId(Integer appointmentId, Pageable pageable);
}
