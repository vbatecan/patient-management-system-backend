package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {
    UserAccountDTO createUserAccount(UserAccountDTO userAccountDTO);
    Optional<UserAccountDTO> getUserAccountById(Integer id);
    Optional<UserAccountDTO> getUserAccountByUsername(String username);
    List<UserAccountDTO> getAllUserAccounts();
    UserAccountDTO updateUserAccount(Integer id, UserAccountDTO userAccountDTO);
    // updatePassword method might be separate and more secure
    void deleteUserAccount(Integer id);
}
