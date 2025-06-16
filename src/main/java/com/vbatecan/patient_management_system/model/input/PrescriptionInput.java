package com.vbatecan.patient_management_system.model.input;

import com.vbatecan.patient_management_system.model.dto.AppointmentDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionInput {

	@NotNull(message = "Appointment cannot be null.")
	@Valid
	private AppointmentDTO appointment;

	@NotBlank(message = "Medication cannot be blank.")
	@Size(min = 2, max = 100, message = "Medication must be between 2 and 100 characters.")
	private String medication;

	@NotBlank(message = "Dosage cannot be blank.")
	@Size(min = 1, max = 50, message = "Dosage must be between 1 and 50 characters.")
	private String dosage;

	@NotBlank(message = "Instructions cannot be blank.")
	@Size(min = 5, max = 500, message = "Instructions must be between 5 and 500 characters.")
	private String instructions;
}
