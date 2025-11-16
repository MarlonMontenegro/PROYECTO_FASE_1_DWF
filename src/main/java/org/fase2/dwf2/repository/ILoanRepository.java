package org.fase2.dwf2.repository;

import org.fase2.dwf2.entities.Loan;
import org.fase2.dwf2.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAll();
    long countByStatus(LoanStatus status);
    List<Loan> findByStatus(LoanStatus status);
}

