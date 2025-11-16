package org.fase2.dwf2.dto.EmployeeAction;

import lombok.Data;
import org.fase2.dwf2.enums.ActionStatus;
import org.fase2.dwf2.enums.ActionType;

import java.time.LocalDateTime;

@Data
public class EmployeeActionResponseDto {
    private Long id;
    private String employeeName;
    private String branchName;
    private ActionType actionType;
    private ActionStatus status;
    private LocalDateTime actionDate;
}
