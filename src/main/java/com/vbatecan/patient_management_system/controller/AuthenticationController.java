package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.model.input.AuthenticationInput;
import com.vbatecan.patient_management_system.model.responses.MessageResponse;
import com.vbatecan.patient_management_system.model.responses.SuccessfulLoginResponse;
import com.vbatecan.patient_management_system.service.interfaces.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthenticationInput input, HttpServletResponse servletResponse) {
		try {
			Optional<SuccessfulLoginResponse> response = authenticationService.login(input);
			if ( response.isPresent() ) {
				// Set cookie
				Cookie cookie = new Cookie("token", response.get().getToken());
				cookie.setHttpOnly(true);
				cookie.setSecure(true);
				cookie.setPath("/");
				cookie.setMaxAge(24 * 60 * 60);
				cookie.setDomain("localhost");
				cookie.setAttribute("username", response.get().getUsername());
				cookie.setAttribute("role", response.get().getRole().name());
				servletResponse.addCookie(cookie);
				return ResponseEntity.ok().body(response.get());
			}

			return ResponseEntity.badRequest().body(new MessageResponse("Username or Password is incorrect", false));
		} catch ( UsernameNotFoundException e ) {
			return ResponseEntity.badRequest().body(new MessageResponse(
				"Username or Password is incorrect", false
			));
		} catch ( Exception e ) {
			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), false));
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ( authentication != null  && authentication.isAuthenticated() ) {
			return ResponseEntity.ok().body(authentication.getPrincipal());
		}

		return ResponseEntity.badRequest().body(new MessageResponse("You are not logged in", false));
	}
}
