package org.fase2.dwf2.repository;

import org.fase2.dwf2.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
    List<Account> findByUserEmail(String email);
    List<Account> findByManagedByDui(String managedByDui);
}
