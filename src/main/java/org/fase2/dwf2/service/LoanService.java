package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.Loan.LoanRequestDto;
import org.fase2.dwf2.dto.Loan.LoanResponseDto;
import org.fase2.dwf2.entities.Loan;
import org.fase2.dwf2.entities.User;
import org.fase2.dwf2.enums.LoanStatus;
import org.fase2.dwf2.repository.ILoanRepository;
import org.fase2.dwf2.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final ILoanRepository loanRepository;
    private final IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public LoanService(ILoanRepository loanRepository, PasswordEncoder passwordEncoder, IUserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<LoanResponseDto> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoanResponseDto> getLoansByStatus(LoanStatus status) {
        List<Loan> loans = loanRepository.findByStatus(status);
        return loans.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateLoanStatus(Long loanId, LoanStatus status) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.setStatus(status);
        loanRepository.save(loan);
    }


    @Transactional
    public LoanResponseDto createLoan(LoanRequestDto loanRequestDto) {
        // Fetch the user by email
        User user = userRepository.findByEmail(loanRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create a new loan
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAmount(loanRequestDto.getAmount());
        loan.setInterestRate(loanRequestDto.getInterestRate());
        loan.setTermInYears(loanRequestDto.getTermInYears());
        loan.setMonthlyPayment(loanRequestDto.getMonthlyPayment());
        loan.setStatus(LoanStatus.PENDING); // Default status
        loan.setStartDate(LocalDate.now()); // Set start date

        Loan savedLoan = loanRepository.save(loan);
        return mapToResponseDto(savedLoan);
    }

    @Transactional(readOnly = true)
    public long getPendingLoansCount() {
        return loanRepository.countByStatus(LoanStatus.PENDING);
    }

    private LoanResponseDto mapToResponseDto(Loan loan) {
        LoanResponseDto responseDto = new LoanResponseDto();
        responseDto.setId(loan.getId());
        responseDto.setEmail(loan.getUser().getEmail());
        responseDto.setAmount(loan.getAmount());
        responseDto.setInterestRate(loan.getInterestRate());
        responseDto.setTermInYears(loan.getTermInYears());
        responseDto.setMonthlyPayment(loan.getMonthlyPayment());
        responseDto.setStatus(loan.getStatus().toString()); // Convert enum to string
        responseDto.setStartDate(loan.getStartDate());
        return responseDto;
    }


    private LoanResponseDto mapToDto(Loan loan) {
        LoanResponseDto dto = new LoanResponseDto();
        System.out.println("User Name: " + loan.getUser().getName());
        dto.setId(loan.getId());
        dto.setClientName(loan.getUser().getName());
        dto.setEmail(loan.getUser().getEmail());
        dto.setAmount(loan.getAmount());
        dto.setInterestRate(loan.getInterestRate());
        dto.setTermInYears(loan.getTermInYears());
        dto.setMonthlyPayment(loan.getMonthlyPayment());
        dto.setStatus(loan.getStatus().toString());
        return dto;
    }
}

