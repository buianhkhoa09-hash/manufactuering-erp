package com.erp.service.impl;

import com.erp.dto.request.InvoiceRequest;
import com.erp.dto.response.InvoiceResponse;
import com.erp.model.Invoice;
import com.erp.model.SalesOrder;
import com.erp.repository.InvoiceRepository;
import com.erp.repository.SalesOrderRepository;
import com.erp.service.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public List<InvoiceResponse> findAll() {
        return invoiceRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public InvoiceResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<InvoiceResponse> findByStatus(String status) {
        return invoiceRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public InvoiceResponse create(InvoiceRequest req) {
        SalesOrder order = salesOrderRepository.findById(req.getSalesOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Sales order not found: " + req.getSalesOrderId()));

        if (invoiceRepository.findBySalesOrderId(req.getSalesOrderId()).isPresent())
            throw new IllegalArgumentException("Invoice already exists for order: " + req.getSalesOrderId());

        String invoiceNumber = generateInvoiceNumber();

        Invoice invoice = Invoice.builder()
                .salesOrder(order)
                .invoiceNumber(invoiceNumber)
                .issuedDate(LocalDate.now())
                .dueDate(req.getDueDate())
                .status("UNPAID")
                .totalAmount(order.getTotalAmount())
                .paidAmount(BigDecimal.ZERO)
                .notes(req.getNotes())
                .build();

        return toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public InvoiceResponse updateStatus(Integer id, String status) {
        Invoice invoice = getOrThrow(id);
        invoice.setStatus(status);
        return toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        invoiceRepository.delete(getOrThrow(id));
    }

    private String generateInvoiceNumber() {
        String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        long count = invoiceRepository.count() + 1;
        return String.format("INV-%s-%04d", year, count);
    }

    private Invoice getOrThrow(Integer id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + id));
    }

    private InvoiceResponse toResponse(Invoice i) {
        BigDecimal balance = i.getTotalAmount().subtract(i.getPaidAmount());
        String customerName = i.getSalesOrder().getCustomer() != null
                ? i.getSalesOrder().getCustomer().getName() : null;

        return InvoiceResponse.builder()
                .id(i.getId())
                .invoiceNumber(i.getInvoiceNumber())
                .salesOrderId(i.getSalesOrder().getId())
                .customerName(customerName)
                .issuedDate(i.getIssuedDate())
                .dueDate(i.getDueDate())
                .status(i.getStatus())
                .totalAmount(i.getTotalAmount())
                .paidAmount(i.getPaidAmount())
                .balance(balance)
                .notes(i.getNotes())
                .createdAt(i.getCreatedAt())
                .build();
    }
}
