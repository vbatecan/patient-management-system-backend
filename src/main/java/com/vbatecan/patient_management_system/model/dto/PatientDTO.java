package com.vbatecan.patient_management_system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
	// add validation for PatientDTO. AI!
	private Integer id;
	private UserAccountDTO userAccount;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	private String contactNumber;
	private String email;
	private String address;
	private String emergencyContact;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
} 