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
@Table(name = "doctor")
@Indexed
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_account_id")
	
	private UserAccount userAccount;

	@Column(name = "first_name", nullable = false)
	@FullTextField
	private String firstName;

	@Column(name = "last_name", nullable = false)
	@FullTextField
	private String lastName;

	@FullTextField
	private String specialty;

	@FullTextField
	private String contactNumber;

	@FullTextField
	private String email;

	@Column(name = "created_at")
	@GenericField
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@GenericField
	private LocalDateTime updatedAt;
} 