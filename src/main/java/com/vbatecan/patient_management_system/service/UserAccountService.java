package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {
    UserAccountDTO save(UserAccountDTO userAccountDTO); // Renamed from createUserAccount
    Optional<UserAccountDTO> getUserAccountById(Integer id);
    Optional<UserAccountDTO> getUserAccountByUsername(String username);
    List<UserAccountDTO> getAllUserAccounts();
    UserAccountDTO update(Integer id, UserAccountDTO userAccountDTO); // Renamed from updateUserAccount
    // updatePassword method might be separate and more secure
    void delete(Integer id); // Renamed from deleteUserAccount
}
