package org.fase2.dwf2.dto.Account;

import lombok.Data;

@Data
public class AccountRequestDto {
    private String accountNumber;
    private String userEmail;
    private Double balance;
    private String dui; // DEPENDIENTE's DUI
    private String password; // New field for the password
}

