package com.vbatecan.patient_management_system.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingDTO {

	@NotNull(message = "ID cannot be null.")
	private Integer id;

	@NotNull(message = "Patient ID cannot be null.")
	private Integer patientId;

	@NotNull(message = "Appointment ID cannot be null.")
	private Integer appointmentId;

	@NotNull(message = "Amount cannot be null.")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero.")
	private BigDecimal amount;

	@NotBlank(message = "Status cannot be blank.")
	@Size(max = 50, message = "Status must be less than 50 characters.")
	private String status;

	@NotNull(message = "Billing date cannot be null.")
	@PastOrPresent(message = "Billing date must be in the past or present.")
	private LocalDateTime billingDate;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
