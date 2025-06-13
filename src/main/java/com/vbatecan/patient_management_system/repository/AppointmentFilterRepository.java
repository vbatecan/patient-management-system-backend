package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.filter.AppointmentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentFilterRepository {
	Page<Appointment> filter(AppointmentFilter filter, Pageable pageable);
}
