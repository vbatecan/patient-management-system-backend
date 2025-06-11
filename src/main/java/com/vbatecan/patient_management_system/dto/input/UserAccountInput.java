package com.vbatecan.patient_management_system.dto.input;

import com.vbatecan.patient_management_system.enums.Role;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountInput {
	@NonNull
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
	private String username;

	@NonNull
	@Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long")
	private String password;

	@NonNull
	private Role role;
}
