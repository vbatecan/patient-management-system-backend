package com.vbatecan.patient_management_system.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointment")
@Indexed
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	@IndexedEmbedded
	private Patient patient;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	@IndexedEmbedded
	private Doctor doctor;

	@Column(name = "appointment_date", nullable = false)
	@GenericField
	private LocalDateTime appointmentDate;

	@FullTextField
	private String reason;

	@GenericField
	private String status = "SCHEDULED";

	@Column(name = "created_at")
	@GenericField
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@GenericField
	private LocalDateTime updatedAt;
} 