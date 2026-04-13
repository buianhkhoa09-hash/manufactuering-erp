package com.erp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeRequest {
    @NotBlank @Size(max = 100)  private String firstName;
    @NotBlank @Size(max = 100)  private String lastName;
    @NotBlank @Email @Size(max = 150) private String email;
    @Size(max = 30)             private String phone;
    @NotBlank @Size(max = 100)  private String role;
    @NotNull                    private Integer departmentId;
    @NotNull                    private LocalDate hireDate;
    @NotNull @DecimalMin("0.0") private BigDecimal salary;
    private String status;
}
