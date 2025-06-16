package com.vbatecan.patient_management_system.model.input;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordInput {

	@NotNull(message = "Patient ID cannot be null.")
	private Integer patientId;

	@NotNull(message = "Record date cannot be null.")
	@PastOrPresent(message = "Record date must be in the past or present.")
	private LocalDateTime recordDate;

	@NotBlank(message = "Description cannot be blank.")
	@Size(max = 2000, message = "Description must be less than 2000 characters.")
	private String description;

	@Size(max = 255, message = "File path must be less than 255 characters.")
	private String filePath; // Optional field
}
