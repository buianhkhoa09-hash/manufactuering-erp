package com.erp.service.impl;

import com.erp.dto.request.PaymentRequest;
import com.erp.dto.response.PaymentResponse;
import com.erp.model.Invoice;
import com.erp.model.Payment;
import com.erp.repository.InvoiceRepository;
import com.erp.repository.PaymentRepository;
import com.erp.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PaymentResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<PaymentResponse> findByInvoice(Integer invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public PaymentResponse create(PaymentRequest req) {
        Invoice invoice = invoiceRepository.findById(req.getInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + req.getInvoiceId()));

        if ("PAID".equals(invoice.getStatus()) || "CANCELLED".equals(invoice.getStatus()))
            throw new IllegalArgumentException("Cannot add payment to a " + invoice.getStatus() + " invoice");

        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(req.getAmount())
                .paymentDate(req.getPaymentDate() != null ? req.getPaymentDate() : LocalDate.now())
                .method(req.getMethod())
                .reference(req.getReference())
                .notes(req.getNotes())
                .build();

        paymentRepository.save(payment);

        // Update invoice paid amount and status
        BigDecimal totalPaid = paymentRepository.sumAmountByInvoiceId(invoice.getId());
        invoice.setPaidAmount(totalPaid);

        if (totalPaid.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus("PAID");
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus("PARTIALLY_PAID");
        }
        invoiceRepository.save(invoice);

        return toResponse(payment);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        paymentRepository.delete(getOrThrow(id));
    }

    private Payment getOrThrow(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + id));
    }

    private PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .invoiceId(p.getInvoice().getId())
                .invoiceNumber(p.getInvoice().getInvoiceNumber())
                .amount(p.getAmount())
                .paymentDate(p.getPaymentDate())
                .method(p.getMethod())
                .reference(p.getReference())
                .notes(p.getNotes())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
