package com.vbatecan.patient_management_system.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
	@Nonnull
	private Integer id;
	private LocalDateTime appointmentDate;
	private String reason;
	private String status;
	private PatientDTO patient;
	private DoctorDTO doctor;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
