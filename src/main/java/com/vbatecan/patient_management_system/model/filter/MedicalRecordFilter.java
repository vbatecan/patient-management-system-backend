package com.vbatecan.patient_management_system.model.filter;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MedicalRecordFilter {

	private Integer id;

	@Null
	private Integer patientId;

	private LocalDateTime recordDateStart;
	private LocalDateTime recordDateEnd;

	@Null
	private String description;

	@Null
	private String filePath;

	private LocalDateTime createdAtStartDate;
	private LocalDateTime createdAtEndDate;

	private LocalDateTime updatedAtStartDate;
	private LocalDateTime updatedAtEndDate;
}
