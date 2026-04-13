package com.erp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SalesOrderRequest {
    @NotNull private Integer customerId;
    private String status;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String notes;
    private Integer createdById;
    @NotNull @Size(min = 1) private List<SalesOrderItemRequest> items;

    @Data
    public static class SalesOrderItemRequest {
        @NotNull          private Integer productId;
        @NotNull @Min(1)  private Integer quantity;
        @NotNull @DecimalMin("0.0") private BigDecimal unitPrice;
    }
}
