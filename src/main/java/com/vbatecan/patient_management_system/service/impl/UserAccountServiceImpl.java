package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.UserAccountService;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.password.PasswordEncoder; // For password hashing
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
    public UserAccountDTO createUserAccount(UserAccountDTO userAccountDTO) {
        // Add validation for username uniqueness if not handled by DB constraint at service level
        if (userAccountRepository.findByUsername(userAccountDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + userAccountDTO.getUsername());
        }

        UserAccount userAccount = convertToEntity(userAccountDTO);
        // userAccount.setPassword(passwordEncoder.encode(userAccountDTO.getPassword())); // Hash password
        userAccount.setCreatedAt(LocalDateTime.now());
        userAccount.setUpdatedAt(LocalDateTime.now());

        // Entity default role "GUEST" will be used if DTO role is null
        if (userAccountDTO.getRole() != null) {
            userAccount.setRole(userAccountDTO.getRole());
        }


        UserAccount savedUserAccount = userAccountRepository.save(userAccount);
        return convertToDTO(savedUserAccount);
    }

    @Override
    public Optional<UserAccountDTO> getUserAccountById(Integer id) {
        return userAccountRepository.findById(id).map(this::convertToDTOWithoutPassword); // Or with password if internal
    }

    @Override
    public Optional<UserAccountDTO> getUserAccountByUsername(String username) {
        return userAccountRepository.findByUsername(username).map(this::convertToDTOWithoutPassword); // Or with password if internal
    }

    @Override
    public List<UserAccountDTO> getAllUserAccounts() {
        return userAccountRepository.findAll().stream()
                .map(this::convertToDTOWithoutPassword) // Or with password if internal
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAccountDTO updateUserAccount(Integer id, UserAccountDTO userAccountDTO) {
        UserAccount existingUserAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + id));

        // Username change might need careful consideration (e.g., if it's an identifier)
        if (userAccountDTO.getUsername() != null && !existingUserAccount.getUsername().equals(userAccountDTO.getUsername())) {
            if (userAccountRepository.findByUsername(userAccountDTO.getUsername()).isPresent()) {
                 throw new IllegalArgumentException("Username already exists: " + userAccountDTO.getUsername());
            }
            existingUserAccount.setUsername(userAccountDTO.getUsername());
        }

        // Password update should ideally be a separate, secure endpoint.
        // If password is in DTO and not null/empty, update it.
        if (userAccountDTO.getPassword() != null && !userAccountDTO.getPassword().isEmpty()) {
            // existingUserAccount.setPassword(passwordEncoder.encode(userAccountDTO.getPassword()));
            existingUserAccount.setPassword(userAccountDTO.getPassword()); // Storing as-is for now
        }

        if (userAccountDTO.getRole() != null) {
            existingUserAccount.setRole(userAccountDTO.getRole());
        }
        existingUserAccount.setUpdatedAt(LocalDateTime.now());

        UserAccount updatedUserAccount = userAccountRepository.save(existingUserAccount);
        return convertToDTO(updatedUserAccount);
    }

    @Override
    @Transactional
    public void deleteUserAccount(Integer id) {
        if (!userAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("UserAccount not found with id: " + id);
        }
        // Consider implications: what happens to Patients/Doctors linked to this UserAccount?
        // Soft delete might be preferable, or cascading deletes if appropriate.
        userAccountRepository.deleteById(id);
    }

    private UserAccountDTO convertToDTO(UserAccount userAccount) {
        UserAccountDTO dto = new UserAccountDTO();
        dto.setId(userAccount.getId());
        dto.setUsername(userAccount.getUsername());
        // dto.setPassword(null); // IMPORTANT: Do not expose password in DTOs returned to most clients
        dto.setPassword(userAccount.getPassword()); // Included for completeness based on DTO, but be cautious
        dto.setRole(userAccount.getRole());
        dto.setCreatedAt(userAccount.getCreatedAt());
        dto.setUpdatedAt(userAccount.getUpdatedAt());
        return dto;
    }
    
    private UserAccountDTO convertToDTOWithoutPassword(UserAccount userAccount) {
        UserAccountDTO dto = new UserAccountDTO();
        dto.setId(userAccount.getId());
        dto.setUsername(userAccount.getUsername());
        dto.setPassword(null); // Explicitly set password to null
        dto.setRole(userAccount.getRole());
        dto.setCreatedAt(userAccount.getCreatedAt());
        dto.setUpdatedAt(userAccount.getUpdatedAt());
        return dto;
    }


    private UserAccount convertToEntity(UserAccountDTO userAccountDTO) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(userAccountDTO.getUsername());
        userAccount.setPassword(userAccountDTO.getPassword()); // Password will be hashed in service method
        userAccount.setRole(userAccountDTO.getRole() == null ? UserAccount.Role.GUEST : userAccountDTO.getRole());
        // createdAt and updatedAt are set in the service method
        return userAccount;
    }
}
