package com.vbatecan.patient_management_system.service.interfaces.filter;

import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.filter.AppointmentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentFilterService {

	Page<Appointment> filter(AppointmentFilter appointmentFilter, Pageable pageable);
}
