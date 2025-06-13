package com.vbatecan.patient_management_system.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO {

	@NotNull(message = "ID cannot be null.")
	private Integer id;

	@NotNull(message = "Patient ID cannot be null.")
	private Integer patientId;

	@NotNull(message = "Record date cannot be null.")
	@PastOrPresent(message = "Record date must be in the past or present.")
	private LocalDateTime recordDate;

	@NotBlank(message = "Description cannot be blank.")
	@Size(max = 2000, message = "Description must be less than 2000 characters.")
	private String description;

	@Size(max = 255, message = "File path must be less than 255 characters.")
	private String filePath; // File path can be optional

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
