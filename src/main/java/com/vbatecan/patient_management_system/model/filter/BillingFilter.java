package com.vbatecan.patient_management_system.model.filter;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BillingFilter {

	private Integer id;

	@Null
	private Integer patientId;

	@Null
	private Integer appointmentId;

	private BigDecimal minAmount;
	private BigDecimal maxAmount;

	@Null
	private String status;

	private LocalDateTime billingDateStart;
	private LocalDateTime billingDateEnd;

	private LocalDateTime createdAtStartDate;
	private LocalDateTime createdAtEndDate;

	private LocalDateTime updatedAtStartDate;
	private LocalDateTime updatedAtEndDate;
}
