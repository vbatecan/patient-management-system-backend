package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

	private final UserAccountRepository userAccountRepository;
	// private final PasswordEncoder passwordEncoder; // TODO: Inject for password hashing

	@Override
	@Transactional
	public UserAccountDTO save(UserAccountDTO userAccountDTO) {
		if ( userAccountRepository.findByUsername(userAccountDTO.getUsername()).isPresent() ) {
			throw new IllegalArgumentException("Username already exists: " + userAccountDTO.getUsername());
		}

		UserAccount userAccount = convertToEntity(userAccountDTO);
		// TODO: Implement password hashing before saving
		userAccount.setCreatedAt(LocalDateTime.now());
		userAccount.setUpdatedAt(LocalDateTime.now());

		if ( userAccountDTO.getRole() != null ) {
			userAccount.setRole(userAccountDTO.getRole());
		}

		UserAccount savedUserAccount = userAccountRepository.save(userAccount);
		return convertToDTO(savedUserAccount);
	}

	@Override
	public Optional<UserAccountDTO> findById(Integer id) {
		return userAccountRepository.findById(id).map(this::convertToDTOWithoutPassword);
	}

	@Override
	public Optional<UserAccountDTO> findByUsername(String username) {
		return userAccountRepository.findByUsername(username).map(this::convertToDTOWithoutPassword);
	}

	@Override
	public Page<UserAccountDTO> findAll(Pageable pageable) {
		return userAccountRepository.findAll(pageable).map(this::convertToDTOWithoutPassword);
	}

	@Override
	@Transactional
	public UserAccountDTO update(Integer id, UserAccountDTO userAccountDTO) {
		UserAccount existingUserAccount = userAccountRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + id));

		if ( userAccountDTO.getUsername() != null && !existingUserAccount.getUsername().equals(userAccountDTO.getUsername()) ) {
			if ( userAccountRepository.findByUsername(userAccountDTO.getUsername()).isPresent() ) {
				throw new IllegalArgumentException("Username already exists: " + userAccountDTO.getUsername());
			}
			existingUserAccount.setUsername(userAccountDTO.getUsername());
		}

		if ( userAccountDTO.getPassword() != null && !userAccountDTO.getPassword().isEmpty() ) {
			// TODO: Implement password hashing before saving
			existingUserAccount.setPassword(userAccountDTO.getPassword());
		}

		if ( userAccountDTO.getRole() != null ) {
			existingUserAccount.setRole(userAccountDTO.getRole());
		}
		existingUserAccount.setUpdatedAt(LocalDateTime.now());

		UserAccount updatedUserAccount = userAccountRepository.save(existingUserAccount);
		return convertToDTO(updatedUserAccount);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if ( !userAccountRepository.existsById(id) ) {
			throw new ResourceNotFoundException("UserAccount not found with id: " + id);
		}
		userAccountRepository.deleteById(id);
	}

	private UserAccountDTO convertToDTO(UserAccount userAccount) {
		UserAccountDTO dto = new UserAccountDTO();
		dto.setId(userAccount.getId());
		dto.setUsername(userAccount.getUsername());
		dto.setPassword(userAccount.getPassword());
		dto.setRole(userAccount.getRole());
		dto.setCreatedAt(userAccount.getCreatedAt());
		dto.setUpdatedAt(userAccount.getUpdatedAt());
		return dto;
	}

	private UserAccountDTO convertToDTOWithoutPassword(UserAccount userAccount) {
		UserAccountDTO dto = new UserAccountDTO();
		dto.setId(userAccount.getId());
		dto.setUsername(userAccount.getUsername());
		dto.setPassword(null);
		dto.setRole(userAccount.getRole());
		dto.setCreatedAt(userAccount.getCreatedAt());
		dto.setUpdatedAt(userAccount.getUpdatedAt());
		return dto;
	}


	private UserAccount convertToEntity(UserAccountDTO userAccountDTO) {
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(userAccountDTO.getUsername());
		userAccount.setPassword(userAccountDTO.getPassword());
		userAccount.setRole(userAccountDTO.getRole() == null ? UserAccount.Role.GUEST : userAccountDTO.getRole());
		return userAccount;
	}
}
