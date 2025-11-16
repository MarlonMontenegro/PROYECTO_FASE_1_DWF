package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.Transaction.TransactionRequestDto;
import org.fase2.dwf2.entities.Account;
import org.fase2.dwf2.entities.Transaction;
import org.fase2.dwf2.enums.TransactionType;
import org.fase2.dwf2.repository.IAccountRepository;
import org.fase2.dwf2.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;

    @Autowired
    public TransactionService(ITransactionRepository transactionRepository, IAccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionRequestDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionRequestDto deposit(TransactionRequestDto transactionRequestDto) {
        if (transactionRequestDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumber());

        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance() + transactionRequestDto.getAmount());
        accountRepository.save(account);

        transactionRequestDto.setTransactionType(TransactionType.DEPOSIT.toString());

        return transactionRequestDto;
    }

    @Transactional
    public TransactionRequestDto withdraw(TransactionRequestDto transactionRequestDto) {
        if (transactionRequestDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumber());

        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        if (account.getBalance() < transactionRequestDto.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance() - transactionRequestDto.getAmount());
        accountRepository.save(account);

        transactionRequestDto.setTransactionType(TransactionType.WITHDRAW.toString());

        return transactionRequestDto;
    }

    @Transactional
    public TransactionRequestDto transfer(TransactionRequestDto transactionRequestDto) {
        // Validate input amount
        if (transactionRequestDto.getAmount() == null || transactionRequestDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        // Retrieve source account
        Account accountFrom = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumber());
        if (accountFrom == null) {
            throw new IllegalArgumentException("Source account does not exist");
        }

        Account accountTo = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumberTo());
        boolean isInternalTransaction = accountTo != null;

        // Handle internal transactions
        if (isInternalTransaction) {
            // Prevent transferring to the same account
            if (accountFrom.getAccountNumber().equals(accountTo.getAccountNumber())) {
                throw new IllegalArgumentException("Cannot transfer to the same account");
            }

            // Check if the source account has sufficient funds
            if (accountFrom.getBalance() < transactionRequestDto.getAmount()) {
                throw new IllegalArgumentException("Insufficient funds");
            }

            // Update target account balance
            accountTo.setBalance(accountTo.getBalance() + transactionRequestDto.getAmount());
            accountRepository.save(accountTo);
        } else {
            // Handle external transactions
            // Ensure the target account number is numeric
            if (!transactionRequestDto.getAccountNumberTo().matches("\\d+")) {
                throw new IllegalArgumentException("Invalid external account number. Must contain numbers only.");
            }
        }

        // Deduct funds from the source account
        accountFrom.setBalance(accountFrom.getBalance() - transactionRequestDto.getAmount());
        accountRepository.save(accountFrom);

        // Log the transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAccount(accountFrom);
        transaction.setAccountTo(isInternalTransaction ? accountTo : null); // Set accountTo only for internal transactions
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Return updated transaction details
        transactionRequestDto.setTransactionType(TransactionType.TRANSFER.toString());
        return transactionRequestDto;
    }

    @Transactional(readOnly = true)
    public List<TransactionRequestDto> getTransactions(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        List<Transaction> transactions = transactionRepository.getTransactionsByAccount_AccountNumber(accountNumber);
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

private TransactionRequestDto mapToDto(Transaction transaction) {
    TransactionRequestDto dto = new TransactionRequestDto();
    dto.setAmount(transaction.getAmount());
    dto.setTransactionType(transaction.getTransactionType().toString());
    dto.setAccountNumber(transaction.getAccount().getAccountNumber());
    dto.setAccountNumberTo(transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null);
    dto.setDate(transaction.getTimestamp());

    System.out.println("DTO: " + dto);
    return dto;
}

    @Transactional
    public TransactionRequestDto depositToManagedAccount(TransactionRequestDto transactionRequestDto) {
        if (transactionRequestDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }

        Account account = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumber());
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }
        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAccount(account);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Update account balance
        account.setBalance(account.getBalance() + transactionRequestDto.getAmount());
        accountRepository.save(account);

        transactionRequestDto.setTransactionType(TransactionType.DEPOSIT.toString());
        return transactionRequestDto;
    }

    @Transactional
    public TransactionRequestDto withdrawFromManagedAccount(TransactionRequestDto transactionRequestDto) {
        if (transactionRequestDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0");
        }

        Account account = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumber());
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        if (account.getBalance() < transactionRequestDto.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setAccount(account);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Update account balance
        account.setBalance(account.getBalance() - transactionRequestDto.getAmount());
        accountRepository.save(account);

        transactionRequestDto.setTransactionType(TransactionType.WITHDRAW.toString());
        return transactionRequestDto;
    }



}
