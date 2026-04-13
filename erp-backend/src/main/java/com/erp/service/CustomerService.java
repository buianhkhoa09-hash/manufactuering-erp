package com.erp.service;

import com.erp.dto.request.CustomerRequest;
import com.erp.dto.response.CustomerResponse;
import java.util.List;

public interface CustomerService {
    List<CustomerResponse> findAll();
    CustomerResponse findById(Integer id);
    CustomerResponse create(CustomerRequest request);
    CustomerResponse update(Integer id, CustomerRequest request);
    void delete(Integer id);
}
