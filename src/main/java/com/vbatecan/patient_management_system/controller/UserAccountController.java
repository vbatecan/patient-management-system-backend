package com.vbatecan.patient_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.model.dto.UserAccountDTO;
import com.vbatecan.patient_management_system.model.input.UserAccountInput;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.entities.UserAccount;
import com.vbatecan.patient_management_system.service.interfaces.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user-accounts")
@RequiredArgsConstructor
@Tag(name = "User Account Management", description = "APIs for managing user accounts")
public class UserAccountController {

	private final UserAccountService userAccountService;
	private final ObjectMapper objectMapper;

	@Operation(summary = "Create a new user account", description = "Creates a new user account in the system. The password will be hashed.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "User account created successfully. Response includes hashed password.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data (e.g., username already exists, missing required fields)",
			content = @Content(mediaType = "text/plain"))
	})
	@PostMapping
	public ResponseEntity<UserAccountDTO> createUserAccount(@Valid @RequestBody UserAccountInput input) {
		Optional<UserAccount> savedUserAccount = userAccountService.save(input);
		return new ResponseEntity<>(objectMapper.convertValue(savedUserAccount, UserAccountDTO.class), HttpStatus.CREATED);
	}

	@Operation(summary = "Get a user account by ID", description = "Retrieves a specific user account by its unique ID. Password is not included in the response.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User account found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class))),
		@ApiResponse(responseCode = "404", description = "User account not found",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/{id}")
	public ResponseEntity<UserAccountDTO> getUserAccountById(@PathVariable Integer id) {
		Optional<UserAccount> userAccountOptional = userAccountService.findById(id);
		return userAccountOptional
			.map(userAccount -> ResponseEntity.ok(objectMapper.convertValue(userAccount, UserAccountDTO.class)))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get a user account by username", description = "Retrieves a specific user account by its username. Password is not included in the response.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User account found",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class))),
		@ApiResponse(responseCode = "404", description = "User account not found",
			content = @Content(mediaType = "text/plain"))
	})
	@GetMapping("/username/{username}")
	public ResponseEntity<UserAccountDTO> getUserAccountByUsername(@PathVariable String username) {
		Optional<UserAccount> userAccountOptional = userAccountService.findByUsername(username);
		return userAccountOptional.map(userAccount -> ResponseEntity.ok(
			objectMapper.convertValue(userAccount, UserAccountDTO.class)
		)).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get all user accounts", description = "Retrieves a paginated list of all user accounts. Passwords are not included in the response.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of user accounts",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
	})
	@GetMapping
	public ResponseEntity<Page<UserAccountDTO>> getAllUserAccounts(Pageable pageable) {
		Page<UserAccount> userAccounts = userAccountService.findAll(pageable);
		Page<UserAccountDTO> userAccountDTOs = userAccounts.map(userAccount -> objectMapper.convertValue(userAccount, UserAccountDTO.class));
		return ResponseEntity.ok(userAccountDTOs);
	}

	@Operation(summary = "Update an existing user account", description = "Updates the details of an existing user account by its ID. If password is provided, it will be re-hashed.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User account updated successfully. Response includes hashed password if updated.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input data (e.g., username already exists)",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "404", description = "User account not found",
			content = @Content(mediaType = "text/plain"))
	})
	@PutMapping("/{id}")
	public ResponseEntity<UserAccountDTO> updateUserAccount(@PathVariable Integer id, @Valid @RequestBody UserAccountInput input) {
		Optional<UserAccount> updatedUserAccount = userAccountService.update(id, input);
		return ResponseEntity.ok(
			objectMapper.convertValue(updatedUserAccount, UserAccountDTO.class)
		);
	}

	@Operation(summary = "Delete a user account", description = "Deletes a user account by its ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "User account deleted successfully"),
		@ApiResponse(responseCode = "404", description = "User account not found",
			content = @Content(mediaType = "text/plain"))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUserAccount(@PathVariable Integer id) {
		userAccountService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}
