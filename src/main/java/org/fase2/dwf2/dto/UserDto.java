package org.fase2.dwf2.dto;

import lombok.Data;
import org.fase2.dwf2.enums.Role;
import org.fase2.dwf2.dto.Account.AccountResponseDto;
import org.fase2.dwf2.enums.UserStatus;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String dui;
    private String email;
    private String password;
    private Role role;
    private UserStatus status;
    private List<AccountResponseDto> accounts;
}
