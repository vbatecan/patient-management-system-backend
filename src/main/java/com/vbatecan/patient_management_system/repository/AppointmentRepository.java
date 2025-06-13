package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.filter.AppointmentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	Page<Appointment> findByPatientId(Integer patientId, Pageable pageable);

	Page<Appointment> findByDoctorId(Integer doctorId, Pageable pageable);
}
