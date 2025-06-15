package com.vbatecan.patient_management_system.model.entities;

import com.vbatecan.patient_management_system.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
@Indexed
public class Patient {
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

	@Column(name = "date_of_birth", nullable = false)
	@GenericField
	private LocalDate dateOfBirth;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	@GenericField
	private Gender gender;

	@Column(name = "contact_number")
	@FullTextField
	private String contactNumber;

	@Column(name = "email")
	@FullTextField
	private String email;

	@Column(name = "address")
	@FullTextField
	private String address;

	@Column(name = "emergency_contact")
	@FullTextField
	private String emergencyContact;

	@Column(name = "created_at")
	@GenericField
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@GenericField
	private LocalDateTime updatedAt;
} 