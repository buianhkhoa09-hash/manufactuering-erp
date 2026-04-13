package com.erp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class InvoiceResponse {
    private Integer id;
    private String invoiceNumber;
    private Integer salesOrderId;
    private String customerName;
    private LocalDate issuedDate;
    private LocalDate dueDate;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal balance;
    private String notes;
    private LocalDateTime createdAt;
}
