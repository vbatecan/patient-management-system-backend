package com.vbatecan.patient_management_system.model.input;

import com.vbatecan.patient_management_system.model.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.model.dto.PatientDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingInput {

	@NotNull(message = "Patient cannot be null.")
	@Valid
	private PatientDTO patient;

	@NotNull(message = "Appointment cannot be null.")
	@Valid
	private AppointmentDTO appointment;

	@NotNull(message = "Amount cannot be null.")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero.")
	private BigDecimal amount;

	@NotBlank(message = "Status cannot be blank.")
	@Size(max = 50, message = "Status must be less than 50 characters.")
	private String status;

	@NotNull(message = "Billing date cannot be null.")
	@PastOrPresent(message = "Billing date must be in the past or present.")
	private LocalDateTime billingDate;
}
