package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.Branch.BranchResponseDto;
import org.fase2.dwf2.entities.Branch;
import org.fase2.dwf2.entities.User;
import org.fase2.dwf2.enums.Role;
import org.fase2.dwf2.repository.IBranchRepository;
import org.fase2.dwf2.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {

    private final IBranchRepository branchRepository;
    private final IUserRepository userRepository;

    public BranchService(IBranchRepository branchRepository, IUserRepository userRepository) {
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
    }

    public List<BranchResponseDto> getAllBranches() {
        return branchRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BranchResponseDto createBranch(BranchResponseDto branchResponseDto) {
        if (branchRepository.existsByName(branchResponseDto.getName())) {
            throw new IllegalArgumentException("Branch with this name already exists");
        }

        Branch branch = new Branch();
        branch.setName(branchResponseDto.getName());
        branch.setLocation(branchResponseDto.getLocation());

        Branch savedBranch = branchRepository.save(branch);
        return mapToDto(savedBranch);
    }

    @Transactional
    public Branch assignManagerToBranch(Long branchId, Long managerId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        if (!manager.getRole().equals(Role.GERENTE_SUCURSAL)) {
            throw new IllegalArgumentException("User is not a franchise manager");
        }

        System.out.println("Assigning manager: " + manager.getName() + " to branch: " + branch.getName());
        branch.setManager(manager);
        return branchRepository.save(branch); // Save the branch with the new manager
    }

    private BranchResponseDto mapToDto(Branch branch) {
        BranchResponseDto dto = new BranchResponseDto();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setLocation(branch.getLocation());
        if (branch.getManager() != null) {
            dto.setManagerName(branch.getManager().getName());
            dto.setManagerEmail(branch.getManager().getEmail());
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<BranchResponseDto> getUnassignedBranches() {
        List<Branch> unassignedBranches = branchRepository.findUnassignedBranches();
        return unassignedBranches.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
