package com.vbatecan.patient_management_system.dto.input;

import com.vbatecan.patient_management_system.enums.Role;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountInput {
	@Nonnull
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
	private String username;

	@Nonnull
	@Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long")
	private String password;

	@Nonnull
	private Role role;
}
