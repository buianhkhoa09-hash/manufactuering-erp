package com.erp.service;

import com.erp.dto.request.InvoiceRequest;
import com.erp.dto.response.InvoiceResponse;
import java.util.List;

public interface InvoiceService {
    List<InvoiceResponse> findAll();
    InvoiceResponse findById(Integer id);
    List<InvoiceResponse> findByStatus(String status);
    InvoiceResponse create(InvoiceRequest request);
    InvoiceResponse updateStatus(Integer id, String status);
    void delete(Integer id);
}
