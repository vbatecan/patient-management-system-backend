package com.vbatecan.patient_management_system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
	private Integer id;
	private Integer userAccountId;
	private String firstName;
	private String lastName;
	private String specialty;
	private String contactNumber;
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
