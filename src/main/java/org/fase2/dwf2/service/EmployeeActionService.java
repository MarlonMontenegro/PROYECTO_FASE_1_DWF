package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.EmployeeAction.EmployeeActionRequestDto;
import org.fase2.dwf2.dto.EmployeeAction.EmployeeActionResponseDto;
import org.fase2.dwf2.dto.Login.RegisterRequestDto;
import org.fase2.dwf2.dto.UserDto;
import org.fase2.dwf2.entities.EmployeeAction;
import org.fase2.dwf2.entities.User;
import org.fase2.dwf2.entities.Account;
import org.fase2.dwf2.enums.ActionStatus;
import org.fase2.dwf2.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.fase2.dwf2.repository.IEmployeeActionRepository;

@Service
public class EmployeeActionService {

    private final IEmployeeActionRepository actionRepository;
    private final IUserRepository userRepository;
    private final IEmployeeActionRepository employeeActionRepository;

    @Autowired
    public EmployeeActionService(IEmployeeActionRepository actionRepository, IUserRepository userRepository, IEmployeeActionRepository employeeActionRepository) {
        this.actionRepository = actionRepository;
        this.userRepository = userRepository;
        this.employeeActionRepository = employeeActionRepository;
    }

    @Transactional
    public EmployeeActionResponseDto createAction(EmployeeActionRequestDto requestDto) {
        User employee = userRepository.findById(requestDto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        EmployeeAction action = new EmployeeAction();
        action.setActionType(requestDto.getActionType());
        action.setStatus(ActionStatus.PENDING);
        action.setActionDate(LocalDateTime.now());
        action.setEmployee(employee);

        EmployeeAction savedAction = actionRepository.save(action);
        return mapToResponseDto(savedAction);
    }

    @Transactional(readOnly = true)
    public List<EmployeeActionResponseDto> getPendingActions() {
        List<EmployeeAction> actions = actionRepository.findByStatus(ActionStatus.PENDING);
        return actions.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    public long countPendingEmployeeActions() {
        return employeeActionRepository.countByStatus(ActionStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<EmployeeActionResponseDto> getPendingEmployeeActions() {
        List<EmployeeAction> actions = employeeActionRepository.findByStatus(ActionStatus.PENDING);
        return actions.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public EmployeeActionResponseDto updateEmployeeActionStatus(Long id, ActionStatus status) {
        EmployeeAction action = employeeActionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Action not found"));
        action.setStatus(status);
        action.setActionDate(LocalDateTime.now());
        EmployeeAction updatedAction = employeeActionRepository.save(action);
        return mapToResponseDto(updatedAction);
    }

    private EmployeeActionResponseDto mapToResponseDto(EmployeeAction action) {
        EmployeeActionResponseDto dto = new EmployeeActionResponseDto();
        dto.setId(action.getId());
        dto.setEmployeeName(action.getEmployee().getName());
        dto.setBranchName(action.getBranch() != null ? action.getBranch().getName() : "N/A");
        dto.setActionType(action.getActionType());
        dto.setStatus(action.getStatus());
        dto.setActionDate(action.getActionDate());
        return dto;
    }
}
