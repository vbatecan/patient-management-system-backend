package com.vbatecan.patient_management_system.model.responses;

import com.vbatecan.patient_management_system.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulLoginResponse {

	@NotBlank(message = "Login successful so token cannot be blank.")
	private String token;

	@NotBlank(message = "Username cannot be blank when login is successful.")
	private String username;

	@NotBlank(message = "Role cannot be blank, even an unauthenticated user has a role of GUEST.")
	private Role role;
}
