package com.vbatecan.patient_management_system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingDTO {
	private Integer id;
	private Integer patientId;
	private Integer appointmentId;
	private BigDecimal amount;
	private String status;
	private LocalDateTime billingDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
