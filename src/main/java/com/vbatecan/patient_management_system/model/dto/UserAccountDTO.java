package com.vbatecan.patient_management_system.model.dto;

import com.vbatecan.patient_management_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {
	private Integer id;
	private String username;
	private Role role;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
