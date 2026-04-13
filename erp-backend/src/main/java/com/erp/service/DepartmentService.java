package com.erp.service;

import com.erp.dto.request.DepartmentRequest;
import com.erp.dto.response.DepartmentResponse;
import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> findAll();
    DepartmentResponse findById(Integer id);
    DepartmentResponse create(DepartmentRequest request);
    DepartmentResponse update(Integer id, DepartmentRequest request);
    void delete(Integer id);
}
