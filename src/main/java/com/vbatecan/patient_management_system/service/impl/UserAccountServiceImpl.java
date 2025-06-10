package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import com.vbatecan.patient_management_system.dto.input.UserAccountInput;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.UserAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional
    public UserAccount save(UserAccountDTO userAccountDTO) {
        if (userAccountRepository.findByUsername(userAccountDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + userAccountDTO.getUsername());
        }
        
        UserAccount userAccount = convertToEntity(userAccountDTO);
        userAccount.setPassword(passwordEncoder.encode(userAccountDTO.getPassword()));
        userAccount.setCreatedAt(LocalDateTime.now());
        userAccount.setUpdatedAt(LocalDateTime.now());
        
        return userAccountRepository.save(userAccount);
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
    public UserAccount update(Integer id, UserAccountDTO userAccountDTO) {
        UserAccount existingUserAccount = userAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + id));
        
        // Update username if changed and not already taken by another user
        if (!existingUserAccount.getUsername().equals(userAccountDTO.getUsername())) {
            Optional<UserAccount> userWithSameUsername = userAccountRepository.findByUsername(userAccountDTO.getUsername());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
                throw new IllegalArgumentException("Username already exists: " + userAccountDTO.getUsername());
            }
            existingUserAccount.setUsername(userAccountDTO.getUsername());
        }
        
        // Update password if provided
        if (userAccountDTO.getPassword() != null && !userAccountDTO.getPassword().isEmpty()) {
            existingUserAccount.setPassword(passwordEncoder.encode(userAccountDTO.getPassword()));
        }
        
        // Update role if provided
        if (userAccountDTO.getRole() != null) {
            existingUserAccount.setRole(userAccountDTO.getRole());
        }
        
        existingUserAccount.setUpdatedAt(LocalDateTime.now());
        
        return userAccountRepository.save(existingUserAccount);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!userAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("UserAccount not found with id: " + id);
        }
        userAccountRepository.deleteById(id);
    }

    private UserAccount convertToEntity(UserAccountDTO userAccountDTO) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(userAccountDTO.getUsername());
        userAccount.setPassword(userAccountDTO.getPassword()); // Password will be encoded before saving
        userAccount.setRole(userAccountDTO.getRole() == null ? UserAccount.Role.GUEST : userAccountDTO.getRole());
        return userAccount;
    }
}
