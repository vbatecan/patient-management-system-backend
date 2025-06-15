package com.vbatecan.patient_management_system.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_record")
@Indexed
public class MedicalRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	
	private Patient patient;

	@Column(name = "record_date", nullable = false)
	@GenericField
	private LocalDateTime recordDate;

	@FullTextField
	private String description;

	@FullTextField
	private String filePath;

	@Column(name = "created_at")
	@GenericField
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@GenericField
	private LocalDateTime updatedAt;
} 