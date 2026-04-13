package com.erp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class DepartmentResponse {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private int employeeCount;
}
