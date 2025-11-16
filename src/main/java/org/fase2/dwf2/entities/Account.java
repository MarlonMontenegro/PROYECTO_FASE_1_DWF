package org.fase2.dwf2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber = generateAccountNumber();

    @Min(0)
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @Column(unique = true)
    @Pattern(regexp = "\\d{8}-\\d", message = "El formato del DUI debe ser XXXXXXXX-X")
    private String managedByDui;

    @PrePersist
    private void ensureAccountNumber() {
        if (this.accountNumber == null || this.accountNumber.isEmpty()) {
            this.accountNumber = generateAccountNumber();
        }
    }

    private String generateAccountNumber() {
        return String.format("%010d", (long) (Math.random() * 1_000_000_0000L));
    }

}
