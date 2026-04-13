package com.erp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank @Size(max = 200)  private String name;
    @NotBlank @Size(max = 80)   private String sku;
    @Size(max = 100)            private String category;
    private String description;
    @NotNull @DecimalMin("0.0") private BigDecimal unitPrice;
    @NotNull @DecimalMin("0.0") private BigDecimal costPrice;
    @Min(0)                     private Integer stockQty;
    @Min(0)                     private Integer reorderLevel;
    @Size(max = 30)             private String unit;
    private Integer supplierId;
    private String status;
}
