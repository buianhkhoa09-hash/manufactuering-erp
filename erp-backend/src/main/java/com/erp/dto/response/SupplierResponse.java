package com.erp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class SupplierResponse {
    private Integer id;
    private String name;
    private String contactName;
    private String email;
    private String phone;
    private String address;
    private String country;
    private String status;
    private LocalDateTime createdAt;
}
