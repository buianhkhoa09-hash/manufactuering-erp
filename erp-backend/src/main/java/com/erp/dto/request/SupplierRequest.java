package com.erp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SupplierRequest {
    @NotBlank @Size(max = 150)        private String name;
    @Size(max = 150)                  private String contactName;
    @NotBlank @Email @Size(max = 150) private String email;
    @Size(max = 30)                   private String phone;
    private String address;
    @Size(max = 100)                  private String country;
    private String status;
}
