package com.vbatecan.patient_management_system.model.filter;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PrescriptionFilter {

	private Integer id;

	@Null
	private Integer appointmentId;

	@Null
	private String medication;

	@Null
	private String dosage;

	@Null
	private String instructions;

	private LocalDateTime createdAtStartDate;
	private LocalDateTime createdAtEndDate;

	private LocalDateTime updatedAtStartDate;
	private LocalDateTime updatedAtEndDate;
}
