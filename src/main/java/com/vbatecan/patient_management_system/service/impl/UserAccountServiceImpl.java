package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.UserAccountService;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.password.PasswordEncoder; // For password hashing, if you implement it
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    // private final PasswordEncoder passwordEncoder; // Inject for password hashing

    @Override
    @Transactional
    public UserAccountDTO save(UserAccountDTO userAccountDTO) {
        // Validate username uniqueness at service level before attempting to save,
        // even if there's a DB constraint, to provide a clearer error message.
        if (userAccountRepository.findByUsername(userAccountDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + userAccountDTO.getUsername());
        }

        UserAccount userAccount = convertToEntity(userAccountDTO);
        // TODO: Implement password hashing before saving, e.g.:
        // userAccount.setPassword(passwordEncoder.encode(userAccountDTO.getPassword()));
        userAccount.setCreatedAt(LocalDateTime.now());
        userAccount.setUpdatedAt(LocalDateTime.now());

        if (userAccountDTO.getRole() != null) {
            userAccount.setRole(userAccountDTO.getRole());
        }
        // If DTO role is null, the entity's default role (GUEST) will be used.

        UserAccount savedUserAccount = userAccountRepository.save(userAccount);
        return convertToDTO(savedUserAccount);
    }

    @Override
    public Optional<UserAccountDTO> findById(Integer id) { // Renamed from getUserAccountById
        // By default, password is not included in DTOs from GET operations for security.
        return userAccountRepository.findById(id).map(this::convertToDTOWithoutPassword);
    }

    @Override
    public Optional<UserAccountDTO> findByUsername(String username) { // Renamed from getUserAccountByUsername
        // By default, password is not included in DTOs from GET operations for security.
        return userAccountRepository.findByUsername(username).map(this::convertToDTOWithoutPassword);
    }

    @Override
    public List<UserAccountDTO> findAll() { // Renamed from getAllUserAccounts
        // By default, password is not included in DTOs from GET operations for security.
        return userAccountRepository.findAll().stream()
                .map(this::convertToDTOWithoutPassword)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAccountDTO update(Integer id, UserAccountDTO userAccountDTO) {
        UserAccount existingUserAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + id));

        // Changing username might have implications if it's used as a stable identifier elsewhere.
        if (userAccountDTO.getUsername() != null && !existingUserAccount.getUsername().equals(userAccountDTO.getUsername())) {
            if (userAccountRepository.findByUsername(userAccountDTO.getUsername()).isPresent()) {
                 throw new IllegalArgumentException("Username already exists: " + userAccountDTO.getUsername());
            }
            existingUserAccount.setUsername(userAccountDTO.getUsername());
        }

        // Password updates should ideally be handled via a separate, more secure mechanism
        // (e.g., a dedicated "changePassword" endpoint requiring current password).
        if (userAccountDTO.getPassword() != null && !userAccountDTO.getPassword().isEmpty()) {
            // TODO: Implement password hashing before saving, e.g.:
            // existingUserAccount.setPassword(passwordEncoder.encode(userAccountDTO.getPassword()));
            existingUserAccount.setPassword(userAccountDTO.getPassword()); // Storing as-is currently; ensure this is secured.
        }

        if (userAccountDTO.getRole() != null) {
            existingUserAccount.setRole(userAccountDTO.getRole());
        }
        existingUserAccount.setUpdatedAt(LocalDateTime.now());

        UserAccount updatedUserAccount = userAccountRepository.save(existingUserAccount);
        return convertToDTO(updatedUserAccount); // DTO includes password here; be cautious with client exposure.
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!userAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("UserAccount not found with id: " + id);
        }
        // Consider the implications of deleting a user account, e.g., on related Patient or Doctor records.
        // A soft delete (marking as inactive) might be a safer approach in many systems.
        userAccountRepository.deleteById(id);
    }

    private UserAccountDTO convertToDTO(UserAccount userAccount) {
        UserAccountDTO dto = new UserAccountDTO();
        dto.setId(userAccount.getId());
        dto.setUsername(userAccount.getUsername());
        // Password is included here. This DTO might be used internally or for responses
        // where the password (even if hashed) is needed. For general client responses,
        // prefer convertToDTOWithoutPassword.
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
        dto.setPassword(null); // Explicitly exclude password for security.
        dto.setRole(userAccount.getRole());
        dto.setCreatedAt(userAccount.getCreatedAt());
        dto.setUpdatedAt(userAccount.getUpdatedAt());
        return dto;
    }


    private UserAccount convertToEntity(UserAccountDTO userAccountDTO) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(userAccountDTO.getUsername());
        // Password is set as-is from DTO; hashing should be implemented in the save/update service methods.
        userAccount.setPassword(userAccountDTO.getPassword());
        userAccount.setRole(userAccountDTO.getRole() == null ? UserAccount.Role.GUEST : userAccountDTO.getRole());
        // createdAt and updatedAt are set in the service methods (save/update)
        return userAccount;
    }
}
