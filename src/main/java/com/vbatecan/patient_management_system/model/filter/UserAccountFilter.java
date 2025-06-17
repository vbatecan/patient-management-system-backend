package com.vbatecan.patient_management_system.model.filter;

import com.vbatecan.patient_management_system.model.enums.Role;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserAccountFilter {

	private Integer id;

	@Null
	private String username;

	@Null
	private Role role;

	private LocalDateTime createdAtStartDate;
	private LocalDateTime createdAtEndDate;

	private LocalDateTime updatedAtStartDate;
	private LocalDateTime updatedAtEndDate;
}
