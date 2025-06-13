package com.vbatecan.patient_management_system.service.interfaces;

import com.vbatecan.patient_management_system.model.input.UserAccountInput;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.entities.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserAccountService {
	Optional<UserAccount> save(UserAccountInput input) throws IllegalArgumentException;

	Optional<UserAccount> findById(Integer id);

	Optional<UserAccount> findByUsername(String username);

	Page<UserAccount> findAll(Pageable pageable);

	Optional<UserAccount> update(Integer id, UserAccountInput userAccountDTO) throws ResourceNotFoundException, IllegalArgumentException;

	void delete(Integer id);
}
