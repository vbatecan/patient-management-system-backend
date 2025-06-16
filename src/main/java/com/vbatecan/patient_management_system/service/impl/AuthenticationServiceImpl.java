package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.model.dto.UserAccountDTO;
import com.vbatecan.patient_management_system.model.entities.UserAccount;
import com.vbatecan.patient_management_system.model.input.AuthenticationInput;
import com.vbatecan.patient_management_system.model.responses.SuccessfulLoginResponse;
import com.vbatecan.patient_management_system.security.JwtService;
import com.vbatecan.patient_management_system.service.interfaces.AuthenticationService;
import com.vbatecan.patient_management_system.service.interfaces.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserAccountService userService;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Optional<SuccessfulLoginResponse> login(AuthenticationInput input) throws UsernameNotFoundException {
		UserAccount account = userService.findByUsername(input.getUsername())
			.orElseThrow(
				() -> new UsernameNotFoundException(String.format("Username %s not found", input.getUsername()))
			);

		try {
			if ( account.getPassword() != null && passwordEncoder.matches(input.getPassword(), account.getPassword()) ) {
				String token = jwtService.generateToken(account);
				return Optional.of(new SuccessfulLoginResponse(token, account.getUsername(), account.getRole()));
			}

			return Optional.empty();
		} catch ( ClassCastException e ) {
			throw new RuntimeException("Failed to generate token, user details is invalid.");
		}
	}

	@Override
	public Boolean isAuthenticated(Authentication authentication) {
		try {
			UserAccountDTO userAccount = (UserAccountDTO) authentication.getPrincipal();
			return userAccount != null && authentication.isAuthenticated();
		} catch (ClassCastException e) {
			return false;
		}
	}
}
