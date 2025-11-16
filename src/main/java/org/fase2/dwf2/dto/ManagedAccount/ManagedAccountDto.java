package org.fase2.dwf2.dto.ManagedAccount;

import lombok.Data;

@Data
public class ManagedAccountDto {
    private String accountNumber;
    private Double balance;
    private String clientName; // The name of the user owning the account
    private String clientEmail;
    private String managedByDui; // DEPENDIENTE's DUI managing this account
}


