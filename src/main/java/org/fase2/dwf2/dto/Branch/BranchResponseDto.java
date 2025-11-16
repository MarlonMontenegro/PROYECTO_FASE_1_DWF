package org.fase2.dwf2.dto.Branch;

import lombok.Data;

@Data
public class BranchResponseDto {
    private Long id;
    private String name;
    private String location;
    private String managerName;
    private String managerEmail;
}
