package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {
    UserAccountDTO save(UserAccountDTO userAccountDTO);
    Optional<UserAccountDTO> findById(Integer id);
    Optional<UserAccountDTO> findByUsername(String username);
    List<UserAccountDTO> findAll();
    UserAccountDTO update(Integer id, UserAccountDTO userAccountDTO);
    // A separate, more secure method for password updates might be considered.
    void delete(Integer id);
}
