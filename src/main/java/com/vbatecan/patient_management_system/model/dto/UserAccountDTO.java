package com.vbatecan.patient_management_system.model.dto;

import com.vbatecan.patient_management_system.enums.Role;
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
public class UserAccountDTO {

	@NotNull(message = "ID cannot be null.")
	private Integer id;

	@NotBlank(message = "Username cannot be blank.")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
	private String username;

	@NotNull(message = "Role cannot be null.")
	private Role role;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
