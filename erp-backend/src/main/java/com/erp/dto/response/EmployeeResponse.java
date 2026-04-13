package com.erp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class EmployeeResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private Integer departmentId;
    private String departmentName;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String status;
    private LocalDateTime createdAt;
}
