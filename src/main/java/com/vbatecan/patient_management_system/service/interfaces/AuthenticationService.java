package com.vbatecan.patient_management_system.service.interfaces;

import com.vbatecan.patient_management_system.model.input.AuthenticationInput;
import com.vbatecan.patient_management_system.model.responses.SuccessfulLoginResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface AuthenticationService {

	Optional<SuccessfulLoginResponse> login(AuthenticationInput input) throws UsernameNotFoundException;

	Boolean isExpired(SecurityContext securityContext);

	Boolean isAuthenticated(SecurityContext securityContext);

	void logout();
}
