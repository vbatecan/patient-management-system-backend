package com.vbatecan.patient_management_system.model.update;

import com.vbatecan.patient_management_system.model.dto.UserAccountDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientUpdate {
	@Null
	private UserAccountDTO userAccount;

	@NotNull
	@Size(min = 1, max = 100)
	private String firstName;

	@NotNull
	@Size(min = 1, max = 100)
	private String lastName;

	@PastOrPresent
	@NotNull
	private LocalDate dateOfBirth;

	@NotNull
	@Size(min = 1, max = 16)
	private String gender;

	@Null
	@Size(min = 1, max = 24)
	private String contactNumber;

	@Null
	@Size(min = 1, max = 100)
	private String email;

	@Null
	@Size(min = 1, max = 65535)
	private String address;

	@Null
	@Size(min = 1, max = 24)
	private String emergencyContact;

	@PastOrPresent
	private LocalDateTime createdAt;

	@PastOrPresent
	private LocalDateTime updatedAt;
}
