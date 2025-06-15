package com.vbatecan.patient_management_system.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

	@NotNull(message = "ID cannot be null.")
	private Integer id;

	@NotNull(message = "User account cannot be null.")
	@Valid
	private UserAccountDTO userAccount;

	@NotBlank(message = "First name cannot be blank.")
	@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
	private String firstName;

	@NotBlank(message = "Last name cannot be blank.")
	@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
	private String lastName;

	@NotNull(message = "Date of birth cannot be null.")
	@Past(message = "Date of birth must be in the past.")
	private LocalDate dateOfBirth;

	@NotBlank(message = "Gender cannot be blank.")
	@Size(min = 1, max = 20, message = "Gender must be between 1 and 20 characters.")
	private String gender;

	@NotBlank(message = "Contact number cannot be blank.")
	@Size(min = 10, max = 15, message = "Contact number must be between 10 and 15 characters.")
	private String contactNumber;

	@NotBlank(message = "Email cannot be blank.")
	@Email(message = "Email should be valid.")
	@Size(min = 5, max = 254, message = "Email must be between 5 and 254 characters.")
	private String email;

	@NotBlank(message = "Address cannot be blank.")
	@Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters.")
	private String address;

	@NotBlank(message = "Emergency contact cannot be blank.")
	@Size(min = 10, max = 100, message = "Emergency contact must be between 10 and 100 characters.")
	private String emergencyContact;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
