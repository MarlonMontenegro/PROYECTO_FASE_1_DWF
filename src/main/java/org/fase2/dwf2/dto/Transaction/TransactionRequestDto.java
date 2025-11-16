package org.fase2.dwf2.dto.Transaction;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRequestDto {
    private Double amount;
    private String transactionType;
    private String accountNumber;
    private String accountNumberTo;
    private LocalDateTime date;
}
