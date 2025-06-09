package com.vbatecan.patient_management_system.dto;

import com.vbatecan.patient_management_system.model.UserAccount;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {
    private Integer id;
    private String username;
    private String password; // Note: Be cautious about exposing passwords in DTOs returned to clients.
    private UserAccount.Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
