package com.erp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class ProductResponse {
    private Integer id;
    private String name;
    private String sku;
    private String category;
    private String description;
    private BigDecimal unitPrice;
    private BigDecimal costPrice;
    private Integer stockQty;
    private Integer reorderLevel;
    private String unit;
    private Integer supplierId;
    private String supplierName;
    private String status;
    private boolean lowStock;
    private LocalDateTime createdAt;
}
