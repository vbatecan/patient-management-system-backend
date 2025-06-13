package com.vbatecan.patient_management_system.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

	@NotNull(message = "ID cannot be null.")
	private Integer id;

	@NotNull(message = "User account ID cannot be null.")
	private UserAccountDTO userAccount;

	@NotBlank(message = "First name cannot be blank.")
	@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
	private String firstName;

	@NotBlank(message = "Last name cannot be blank.")
	@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
	private String lastName;

	@NotBlank(message = "Specialty cannot be blank.")
	@Size(min = 2, max = 100, message = "Specialty must be between 2 and 100 characters.")
	private String specialty;

	@NotBlank(message = "Contact number cannot be blank.")
	@Size(min = 10, max = 15, message = "Contact number must be between 10 and 15 characters.")
	private String contactNumber;

	@NotBlank(message = "Email cannot be blank.")
	@Email(message = "Email should be valid.")
	@Size(min = 5, max = 254, message = "Email must be between 5 and 254 characters.")
	private String email;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
