package com.vbatecan.patient_management_system.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationInput {

	@NotBlank
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
	private String username;

	@NotBlank
	@Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long")
	private String password;
}
