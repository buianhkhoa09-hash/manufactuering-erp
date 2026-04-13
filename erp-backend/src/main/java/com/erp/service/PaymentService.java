package com.erp.service;

import com.erp.dto.request.PaymentRequest;
import com.erp.dto.response.PaymentResponse;
import java.util.List;

public interface PaymentService {
    List<PaymentResponse> findAll();
    PaymentResponse findById(Integer id);
    List<PaymentResponse> findByInvoice(Integer invoiceId);
    PaymentResponse create(PaymentRequest request);
    void delete(Integer id);
}
