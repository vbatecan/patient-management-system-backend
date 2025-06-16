package com.vbatecan.patient_management_system.model.filter;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PatientFilter {

	private Integer id;

	@Null
	private Integer userAccountId;

	@Null
	private String firstName;

	@Null
	private String lastName;

	@Null
	private LocalDate dateOfBirth; // For exact match or checking if null

	@Null
	private String gender;

	@Null
	private String contactNumber;

	@Null
	private String email;

	@Null
	private String address;

	@Null
	private String emergencyContact;

	private LocalDateTime createdAtStartDate;
	private LocalDateTime createdAtEndDate;

	private LocalDateTime updatedAtStartDate;
	private LocalDateTime updatedAtEndDate;
}
