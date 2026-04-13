package com.erp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class SalesOrderResponse {
    private Integer id;
    private Integer customerId;
    private String customerName;
    private String status;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private BigDecimal totalAmount;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<ItemResponse> items;

    @Data @Builder
    public static class ItemResponse {
        private Integer id;
        private Integer productId;
        private String productName;
        private String sku;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
    }
}
