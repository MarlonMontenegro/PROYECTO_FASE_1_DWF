package org.fase2.dwf2.dto.Loan;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanResponseDto {
    private Long id;
    private String clientName;
    private String email;
    private Double amount;
    private Double interestRate;
    private Integer termInYears;
    private Double monthlyPayment;
    private String status;           // Loan status (PENDING, APPROVED, REJECTED)
    private LocalDate startDate;
}


