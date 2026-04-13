package com.erp.service;

import com.erp.dto.request.SupplierRequest;
import com.erp.dto.response.SupplierResponse;
import java.util.List;

public interface SupplierService {
    List<SupplierResponse> findAll();
    SupplierResponse findById(Integer id);
    SupplierResponse create(SupplierRequest request);
    SupplierResponse update(Integer id, SupplierRequest request);
    void delete(Integer id);
}
