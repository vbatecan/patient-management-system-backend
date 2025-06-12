package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.input.UserAccountInput;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Optional<UserAccount> save(UserAccountInput input) throws IllegalArgumentException {
		UserAccount userAccount = new UserAccount(
			null,
			input.getUsername(),
			passwordEncoder.encode(input.getPassword()),
			input.getRole(),
			LocalDateTime.now(),
			LocalDateTime.now()
		);

		if ( userAccountRepository.existsByUsername(input.getUsername()) ) {
			return Optional.empty();
		}

		return Optional.of(userAccountRepository.save(userAccount));
	}

	@Override
	public Optional<UserAccount> findById(Integer id) {
		return userAccountRepository.findById(id);
	}

	@Override
	public Optional<UserAccount> findByUsername(String username) {
		return userAccountRepository.findByUsername(username);
	}

	@Override
	public Page<UserAccount> findAll(Pageable pageable) {
		return userAccountRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Optional<UserAccount> update(Integer id, UserAccountInput input) throws ResourceNotFoundException, IllegalArgumentException {
		// Check if the username the user is trying to update already exists
		Optional<UserAccount> userAccountOptional = userAccountRepository.findByUsername(input.getUsername());

		if ( userAccountOptional.isPresent() ) {
			// Username must be unique, if it exists and is not the same as the current user's username, return empty
			return Optional.empty();
		}

		// TODO: We need to make sure that username we are updating is the user who is currently logged in.
		// TODO: Complete the API Endpoint security first.
		Optional<UserAccount> existingUserAccount = userAccountRepository.findById(id);

		if ( existingUserAccount.isEmpty() ) {
			throw new ResourceNotFoundException("UserAccount not found with id: " + id);
		}

		UserAccount userAccount = existingUserAccount.get();
		userAccount.setUsername(input.getUsername()); // Updating the username is safe as it was already validated to be non existent.
		if ( !input.getPassword().isEmpty() ) { // If the password is empty or null, we do not update it
			userAccount.setPassword(passwordEncoder.encode(input.getPassword())); // Only update password if provided
		}

		userAccount.setRole(input.getRole());
		userAccount.setUpdatedAt(LocalDateTime.now());
		return Optional.of(userAccountRepository.save(userAccount));
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		if ( !userAccountRepository.existsById(id) ) {
			throw new ResourceNotFoundException("UserAccount not found with id: " + id);
		}

		userAccountRepository.deleteById(id);
	}
}
