package com.vbatecan.patient_management_system.repository;

import com.vbatecan.patient_management_system.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
	Optional<UserAccount> findByUsername(String username);
}
