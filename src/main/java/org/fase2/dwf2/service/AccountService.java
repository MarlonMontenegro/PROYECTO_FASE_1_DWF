package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.Account.AccountRequestDto;
import org.fase2.dwf2.dto.ManagedAccount.ManagedAccountDto;
import org.fase2.dwf2.dto.UserDto;
import org.fase2.dwf2.entities.Account;
import org.fase2.dwf2.dto.Login.RegisterRequestDto;
import org.fase2.dwf2.repository.IUserRepository;
import org.fase2.dwf2.enums.Role;
import org.fase2.dwf2.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final IAccountRepository accountRepository;
    private final UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    public AccountService(IAccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }


    @Transactional(readOnly = true)
    public List<AccountRequestDto> findByUserEmail(String email) {
        return accountRepository.findByUserEmail(email).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountRequestDto createAccount(AccountRequestDto accountRequestDto) {
        if (!userService.existsByDui(accountRequestDto.getDui())) {
            throw new IllegalArgumentException("User with this DUI does not exist");
        }
        Account account = new Account();
        account.setUser(userService.getUserByEmail(accountRequestDto.getUserEmail()));
        account.setBalance(accountRequestDto.getBalance());
        return convertToDto(accountRepository.save(account));
    }

    @Transactional
    public ManagedAccountDto createManagedAccount(AccountRequestDto accountRequestDto) {
        // Step 1: Create the managed user using the register logic
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setEmail(accountRequestDto.getUserEmail());
        registerRequest.setPassword(accountRequestDto.getPassword());
        registerRequest.setName("Managed Client"); // Or fetch from accountRequestDto if provided
        registerRequest.setDui(accountRequestDto.getDui());
        registerRequest.setRole(Role.CLIENT);

        // Use the register method to create the user
        UserDto newUser = userService.save(registerRequest);

        // Step 2: Create the account and associate it with the newly created user
        Account account = new Account();
        account.setAccountNumber(generateUniqueAccountNumber()); // Generate unique account number
        account.setBalance(accountRequestDto.getBalance());
        account.setManagedByDui(accountRequestDto.getDui()); // Link to the DEPENDIENTE's DUI
        account.setUser(userRepository.findById(newUser.getId()).orElseThrow(() ->
                new IllegalArgumentException("Failed to link user to account"))); // Link the created user to the account

        Account savedAccount = accountRepository.save(account);

        // Step 3: Map to ManagedAccountDto and return
        return mapToManagedAccountDto(savedAccount);
    }

    @Transactional(readOnly = true)
    public List<ManagedAccountDto> getManagedAccounts(String dependienteDui) {
        List<Account> accounts = accountRepository.findByManagedByDui(dependienteDui);
        return accounts.stream()
                .map(this::mapToManagedAccountDto)
                .collect(Collectors.toList());
    }

    private ManagedAccountDto mapToManagedAccountDto(Account account) {
        ManagedAccountDto dto = new ManagedAccountDto();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setClientName(account.getUser().getName());
        dto.setClientEmail(account.getUser().getEmail());
        dto.setManagedByDui(account.getManagedByDui());
        return dto;
    }

    private AccountRequestDto convertToDto(Account account) {
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setAccountNumber(account.getAccountNumber());
        accountRequestDto.setUserEmail(account.getUser().getEmail());
        accountRequestDto.setBalance(account.getBalance());
        accountRequestDto.setDui(account.getUser().getDui());
        return accountRequestDto;
    }

    // To create a random account number
    private String generateUniqueAccountNumber() {
        Random random = new Random();
        String accountNumber;

        do {
            accountNumber = String.format("%08d", random.nextInt(100000000));
        } while (accountRepository.findByAccountNumber(accountNumber) != null);

        return accountNumber;
    }


}
