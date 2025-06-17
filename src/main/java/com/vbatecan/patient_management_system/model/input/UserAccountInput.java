package com.vbatecan.patient_management_system.model.input;

import com.vbatecan.patient_management_system.model.enums.Role;
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
public class UserAccountInput {
	@NotNull
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
	private String username;

	@NotNull
	@Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long")
	private String password;

	@NotNull
	private Role role;
}
