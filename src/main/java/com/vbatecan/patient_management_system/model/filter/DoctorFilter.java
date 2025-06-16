package com.vbatecan.patient_management_system.model.filter;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DoctorFilter {

	private Integer id;

	@Null
	private Integer userAccountId;

	@Null
	private String firstName;

	@Null
	private String lastName;

	@Null
	private String specialty;

	@Null
	private String contactNumber;

	@Null
	private String email;

	private LocalDateTime createdAtStartDate;
	private LocalDateTime createdAtEndDate;

	private LocalDateTime updatedAtStartDate;
	private LocalDateTime updatedAtEndDate;
}
