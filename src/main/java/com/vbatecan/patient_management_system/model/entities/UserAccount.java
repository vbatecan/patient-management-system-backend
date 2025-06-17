package com.vbatecan.patient_management_system.model.entities;

import com.vbatecan.patient_management_system.model.enums.Role;
import com.vbatecan.patient_management_system.model.dto.UserAccountDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_account")
@Indexed
public class UserAccount implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true)
	@FullTextField
	private String username;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@GenericField
	private Role role = Role.GUEST;

	@Column(name = "created_at")
	@GenericField
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@GenericField
	private LocalDateTime updatedAt;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new GrantedAuthority[]{new SimpleGrantedAuthority(role.name())});
	}

	public UserAccountDTO toDTO() {
		return new UserAccountDTO(
				id,
				username,
				role,
				createdAt,
				updatedAt
		);
	}
}