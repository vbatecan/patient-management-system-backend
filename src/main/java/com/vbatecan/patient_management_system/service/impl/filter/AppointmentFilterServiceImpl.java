package com.vbatecan.patient_management_system.service.impl.filter;

import com.vbatecan.patient_management_system.model.entities.Appointment;
import com.vbatecan.patient_management_system.model.filter.AppointmentFilter;
import com.vbatecan.patient_management_system.service.interfaces.filter.AppointmentFilterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AppointmentFilterServiceImpl implements AppointmentFilterService {

	@Override
	public Page<Appointment> filter(AppointmentFilter appointmentFilter, Pageable pageable) {
		return null;
	}
}
