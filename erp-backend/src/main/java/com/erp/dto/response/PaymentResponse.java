package com.erp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class PaymentResponse {
    private Integer id;
    private Integer invoiceId;
    private String invoiceNumber;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String method;
    private String reference;
    private String notes;
    private LocalDateTime createdAt;
}
