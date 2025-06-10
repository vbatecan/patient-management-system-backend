package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.UserAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserAccountService {
	UserAccountDTO save(UserAccountDTO userAccountDTO);

	Optional<UserAccountDTO> findById(Integer id);

	Optional<UserAccountDTO> findByUsername(String username);

	Page<UserAccountDTO> findAll(Pageable pageable);

	UserAccountDTO update(Integer id, UserAccountDTO userAccountDTO);

	void delete(Integer id);
}
