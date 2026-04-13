package com.erp.service;

import com.erp.dto.request.SalesOrderRequest;
import com.erp.dto.response.SalesOrderResponse;
import java.util.List;

public interface SalesOrderService {
    List<SalesOrderResponse> findAll();
    SalesOrderResponse findById(Integer id);
    List<SalesOrderResponse> findByCustomer(Integer customerId);
    SalesOrderResponse create(SalesOrderRequest request);
    SalesOrderResponse update(Integer id, SalesOrderRequest request);
    SalesOrderResponse updateStatus(Integer id, String status);
    void delete(Integer id);
}
