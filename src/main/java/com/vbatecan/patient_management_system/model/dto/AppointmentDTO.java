package com.vbatecan.patient_management_system.model.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
	@NotNull
	private Integer id;
	private LocalDateTime appointmentDate;
	private String reason;

	@NotNull
	private String status;
	private PatientDTO patient;
	private DoctorDTO doctor;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
