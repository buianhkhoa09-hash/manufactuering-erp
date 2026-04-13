package com.erp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class InvoiceRequest {
    @NotNull private Integer salesOrderId;
    @NotNull private LocalDate dueDate;
    private String notes;
}
