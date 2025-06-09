package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatientId(Integer patientId);
    List<Appointment> findByDoctorId(Integer doctorId);
}
