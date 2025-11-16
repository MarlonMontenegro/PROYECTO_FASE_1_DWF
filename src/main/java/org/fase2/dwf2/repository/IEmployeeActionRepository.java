package org.fase2.dwf2.repository;

import org.fase2.dwf2.entities.EmployeeAction;
import org.fase2.dwf2.enums.ActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeActionRepository extends JpaRepository<EmployeeAction, Long> {

    List<EmployeeAction> findByStatus(ActionStatus status);
    long countByStatus(ActionStatus status);

}
