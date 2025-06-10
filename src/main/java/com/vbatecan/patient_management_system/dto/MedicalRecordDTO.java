package com.vbatecan.patient_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO {
	private Integer id;
	private Integer patientId;
	private LocalDateTime recordDate;
	private String description;
	private String filePath;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
} 