package com.erp.service;

import com.erp.dto.request.ProductRequest;
import com.erp.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse findById(Integer id);
    List<ProductResponse> findLowStock();
    ProductResponse create(ProductRequest request);
    ProductResponse update(Integer id, ProductRequest request);
    ProductResponse adjustStock(Integer id, int delta);
    void delete(Integer id);
}
