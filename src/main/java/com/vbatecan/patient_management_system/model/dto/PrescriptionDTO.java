package com.vbatecan.patient_management_system.model.dto;

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
public class PrescriptionDTO {

	@NotNull(message = "ID cannot be null.")
	private Integer id;

	@NotNull(message = "Appointment ID cannot be null.")
	private Integer appointmentId;

	@NotBlank(message = "Medication cannot be blank.")
	@Size(min = 2, max = 100, message = "Medication must be between 2 and 100 characters.")
	private String medication;

	@NotBlank(message = "Dosage cannot be blank.")
	@Size(min = 1, max = 50, message = "Dosage must be between 1 and 50 characters.")
	private String dosage;

	@NotBlank(message = "Instructions cannot be blank.")
	@Size(min = 5, max = 500, message = "Instructions must be between 5 and 500 characters.")
	private String instructions;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
