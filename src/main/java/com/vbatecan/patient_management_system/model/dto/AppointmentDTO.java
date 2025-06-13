package com.vbatecan.patient_management_system.model.dto;

import com.vbatecan.patient_management_system.model.enums.AppointmentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
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

	@NotNull(message = "Appointment date cannot be null.")
	@FutureOrPresent(message = "Appointment date must be in the present or future.")
	private LocalDateTime appointmentDate;

	@NotBlank(message = "Reason cannot be blank.")
	private String reason;

	@NotNull(message = "Status cannot be null.")
	private AppointmentStatus status;

	@NotNull(message = "Patient cannot be null.")
	@Valid
	private PatientDTO patient;

	@NotNull(message = "Doctor cannot be null.")
	@Valid
	private DoctorDTO doctor;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
