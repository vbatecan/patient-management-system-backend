package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.model.input.AuthenticationInput;
import com.vbatecan.patient_management_system.model.responses.MessageResponse;
import com.vbatecan.patient_management_system.model.responses.SuccessfulLoginResponse;
import com.vbatecan.patient_management_system.service.interfaces.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;


	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthenticationInput input) {
		try {
			Optional<SuccessfulLoginResponse> response = authenticationService.login(input);
			if ( response.isPresent() ) {
				return ResponseEntity.ok(response.get());
			}
			return ResponseEntity.badRequest().build();
		} catch ( UsernameNotFoundException e ) {
			return ResponseEntity.badRequest().body(new MessageResponse(
				"Username or Password is incorrect", false
			));
		}
	}
}
