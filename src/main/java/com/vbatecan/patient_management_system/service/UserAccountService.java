package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {
    UserAccountDTO save(UserAccountDTO userAccountDTO);
    Optional<UserAccountDTO> findById(Integer id); // Renamed from getUserAccountById
    Optional<UserAccountDTO> findByUsername(String username); // Renamed from getUserAccountByUsername
    List<UserAccountDTO> findAll(); // Renamed from getAllUserAccounts
    UserAccountDTO update(Integer id, UserAccountDTO userAccountDTO);
    // updatePassword method might be separate and more secure
    void delete(Integer id);
}
