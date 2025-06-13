package com.vbatecan.patient_management_system.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
	private String firstName;

	@NotBlank(message = "Last name cannot be blank.")
	private String lastName;

	@NotNull(message = "Date of birth cannot be null.")
	@Past(message = "Date of birth must be in the past.")
	private LocalDate dateOfBirth;

	@NotBlank(message = "Gender cannot be blank.")
	private String gender;

	@NotBlank(message = "Contact number cannot be blank.")
	private String contactNumber;

	@NotBlank(message = "Email cannot be blank.")
	@Email(message = "Email should be valid.")
	private String email;

	@NotBlank(message = "Address cannot be blank.")
	private String address;

	@NotBlank(message = "Emergency contact cannot be blank.")
	private String emergencyContact;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
