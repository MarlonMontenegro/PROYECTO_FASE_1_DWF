package org.fase2.dwf2.dto.EmployeeAction;

import lombok.Data;
import org.fase2.dwf2.enums.ActionType;

@Data
public class EmployeeActionRequestDto {
    private Long employeeId;
    private String branchName;
    private ActionType actionType;
}
