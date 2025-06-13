package com.vbatecan.patient_management_system.model.filter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AppointmentFilter {

	@Null
	private Integer patientId;

	@Null
	private Integer doctorId;

	private LocalDate appointmentStartDate;
	private LocalDate appointmentEndDate;

	@Null
	private String reason;

	@Null
	private String status;
}
