package org.fase2.dwf2.entities;
import lombok.*;
import jakarta.persistence.*;
import org.fase2.dwf2.enums.ActionStatus;
import org.fase2.dwf2.enums.ActionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_actions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActionType actionType; // HIRE, FIRE, PROMOTION

    @Enumerated(EnumType.STRING)
    private ActionStatus status; // PENDING, APPROVED, REJECTED

    private LocalDateTime actionDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
