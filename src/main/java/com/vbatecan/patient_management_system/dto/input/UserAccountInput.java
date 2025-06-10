package com.vbatecan.patient_management_system.dto.input;

import com.vbatecan.patient_management_system.enums.Role;
import com.vbatecan.patient_management_system.model.UserAccount;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountInput {
	private Integer id;
	private String username;
	private String password;
	private Role role;
}
