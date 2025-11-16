package org.fase2.dwf2.repository;

import org.fase2.dwf2.entities.Transaction;
import org.fase2.dwf2.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> getTransactionsByAccount_AccountNumber(String accountNumber);
    List<Transaction> getTransactionsByAccount_AccountNumberAndTransactionType(String accountNumber, TransactionType transactionType);
}
