package org.fase2.dwf2.dto.Account;

import lombok.Data;

@Data
public class AccountResponseDto {
    private Long id;
    private String accountNumber;
    private String userEmail;
    private Double balance;
}
