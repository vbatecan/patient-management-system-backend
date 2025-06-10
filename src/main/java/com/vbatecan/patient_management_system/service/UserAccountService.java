package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import com.vbatecan.patient_management_system.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserAccountService {
    UserAccount save(UserAccountDTO userAccountDTO);

    Optional<UserAccount> findById(Integer id);

    Optional<UserAccount> findByUsername(String username);

    Page<UserAccount> findAll(Pageable pageable);

    UserAccount update(Integer id, UserAccountDTO userAccountDTO);

    void delete(Integer id);
}
