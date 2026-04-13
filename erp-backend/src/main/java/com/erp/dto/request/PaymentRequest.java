package com.erp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequest {
    @NotNull                    private Integer invoiceId;
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    private LocalDate paymentDate;
    @NotBlank                   private String method;
    private String reference;
    private String notes;
}
