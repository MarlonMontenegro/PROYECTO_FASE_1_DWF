package org.fase2.dwf2.dto.Loan;

import lombok.Data;

@Data
public class LoanRequestDto {
    private String email;
    private Double amount;
    private Double interestRate;
    private Integer termInYears;
    private Double monthlyPayment;
}

