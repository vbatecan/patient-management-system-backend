package com.vbatecan.patient_management_system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private Integer id;
    private Integer appointmentId;
    private String medication;
    private String dosage;
    private String instructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
